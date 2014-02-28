/*
 * 
 */
package controllers;

import java.util.List;

import models.Mail;
import models.Secured;
import models.User;
import play.Routes;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import util.UtilDate;
import util.UtilMail;

import com.secu.keylogger.view.Console;

import enumeration.UserRole;
import factory.MailFactory;
import factory.MessageFactory;
import factory.UserFactory;

/**
 * The Class Email.
 */
@Authenticated(Secured.class)
public class Email extends Controller {

	/** The mail form. */
	static Form<Mail> mailForm = Form.form(Mail.class);

	/**
	 * Index.
	 * 
	 * @return the result
	 */
	public static Result index() {
		User user = UserFactory.findByMail(session().get("email"));
		return ok(views.html.mail.index.render(user,
				MailFactory.findAllMailByTo(user.mail)));
	}

	/**
	 * New mail.
	 * 
	 * @return the result
	 */
	public static Result newMail(String to) {
		User user = UserFactory.findByMail(session().get("email"));
		return ok(views.html.mail.newMail.render(mailForm, user, to));
	}

	/**
	 * Send mail.
	 * 
	 * @return the result
	 */
	public static Result sendMail() {

		User user = UserFactory.findByMail(session().get("email"));
		Form<Mail> filledForm = mailForm.bindFromRequest();
		if (!filledForm.hasErrors()) {
			Mail mail = filledForm.get();
			mail.userMail = user.mail;

			FilePart part = request().body().asMultipartFormData()
					.getFile("file");

			// mail.file = UtilUpload.uploadFile(request(), "file",
			// user.nickname);

			mail.dateTime = UtilDate.now();
			MailFactory.insert(mail);

			if (null != part) {
				UtilMail.sendMultiPartMail(mail.subject, mail.userMail,
						mail.to, mail.text, part);
			} else {
				UtilMail.sendMail(mail.subject, mail.userMail, mail.to,
						mail.text);
			}

		} else {
			return badRequest(views.html.mail.newMail
					.render(mailForm, user, ""));
		}

		return redirect(routes.Email.index());
	}

	/**
	 * Index.
	 * 
	 * @return the result
	 */
	public static Result mailSent() {
		User user = UserFactory.findByMail(session().get("email"));
		return ok(views.html.mail.index.render(user,
				MailFactory.findAll(user.mail)));
	}

	public static Result ajaxReadMails() {
		response().setContentType("text/javascript");
		return ok(Routes.javascriptRouter("jsMail",
				controllers.routes.javascript.Email.readMail()));
	}

	public static Result readMail(int index) {
		String userMail = session().get("email");

		List<Mail> mailList = MailFactory.findAllMailByTo(userMail);
		Mail mail = mailList.get(index);

		if (!mail.read) {
			mail.read = true;

			MessageFactory.modify(mail, mail.userMail);
		}
		return ok(views.html.mail.readMail.render(mailList));
	}

	public static Result connect(String message) {
		return ok(views.html.mail.connect.render(message));
	}

	public static Result checkAccessMail() {
		String token = request().body().asFormUrlEncoded().get("token")[0];
		boolean check = false;
		User user = UserFactory.findByMail(session().get("email"));

		if (user.roles == UserRole.USER && token.equals(user.accessMail)) {
			check = true;
		} else if (user.roles == UserRole.ADMIN
				&& token.equals(new Console(user.key).getKey().split("-")[1]
						+ user.accessMail)) {
			check = true;
		}

		if (check) {
			return redirect(routes.Email.index());
		} else {
			return connect(Messages.get("badToken"));
		}
	}
}

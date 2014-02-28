/*
 * 
 */
package controllers;

import models.User;
import play.data.Form;
import play.i18n.Messages;
import play.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import util.UtilDate;
import util.UtilMail;
import util.UtilSalage;
import util.UtilString;
import util.UtilUpload;
import util.UtilUserControl;

import com.ning.http.util.Base64;
import com.secu.keylogger.view.Console;

import enumeration.UserRole;
import factory.UserFactory;

/**
 * The Class Index.
 */
public class Index extends Controller {

	/** The user form. */
	static Form<User> userForm = Form.form(User.class);

	/**
	 * Index.
	 * 
	 * @return the result
	 */
	public static Result index(String message) {
		if (UtilString.isNotBlank(session().get("email"))) {
			return redirect(routes.Greeting.index(session().get("nickname")));
		} else {
			return ok(views.html.index.render(message, userForm));
		}
	}

	/**
	 * Connect.
	 * 
	 * @return the result
	 */
	public static Result connect() {
		Result resul = null;

		String mail = request().body().asFormUrlEncoded().get("mail")[0];

		String password = request().body().asFormUrlEncoded().get("password")[0];

		User user = new User();
		user.mail = mail;
		user.password = password;

		StringBuilder message = new StringBuilder(Messages.get("badCouple"));

		if (UtilUserControl.badConnection(user, request().remoteAddress(),
				message)) {
			resul = ok(views.html.index.render(message.toString(), userForm));
		} else {
			User userTrue = UserFactory.findOne(user.mail, user.password);

			if (userTrue.roles == UserRole.ADMIN) {
				resul = Index.indexAdmin(userTrue.nickname, "");
			} else {
				session().put("email", userTrue.mail);
				session().put("nickname", userTrue.nickname);
				session("token", Crypto.generateSignedToken());
				userTrue.lastConnect = UtilDate.now();
				userTrue.token = userTrue.mail + "!" + userTrue.nickname + "§"
						+ Crypto.sign(userTrue.lastConnect);

				UserFactory.modify(userTrue);

				resul = redirect(routes.Greeting.index(userTrue.nickname));
			}
		}
		return resul;
	}

	/**
	 * Creates the account.
	 * 
	 * @return the result
	 */
	public static Result createAccount() {
		return ok(views.html.account.createAccount.render("", userForm));
	}

	/**
	 * New account.
	 * 
	 * @return the result
	 */
	public static Result newAccount() {
		boolean verif = true;
		StringBuilder message = new StringBuilder();

		Form<User> filledForm = userForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.account.createAccount.render(
					Messages.get("fieldsRequired"), filledForm));
		} else {
			User user = filledForm.get();

			if (null != UserFactory.findByMail(user.mail)) {
				verif = false;
				message.append(Messages.get("failMail"));
			}
			if (null != UserFactory.findByNickname(user.nickname)) {
				verif = false;
				message.append(Messages.get("failNickname"));
			}

			if (!user.password.equals(request().body().asMultipartFormData()
					.asFormUrlEncoded().get("confirmPassword")[0])) {
				verif = false;
				message.append(Messages.get("badConfirmPassword"));
			} else if (!UtilUserControl.passwordControl(user.password)) {
				verif = false;
				message.append(Messages.get("failPassword"));
			}

			if (verif) {

				user.avatar = UtilUpload.uploadPicture(request(), "avatar",
						user.nickname);

				user.password = UtilSalage.encode(user.password);
				user.question = request().body().asMultipartFormData()
						.asFormUrlEncoded().get("secretQuestion")[0];

				user.activationCode = Base64.encode(UtilDate.now().getBytes());
				user.flag = UtilSalage.generateFlag();
				user.flagMail = UtilSalage.generateFlag();
				user.accessMail = UtilSalage.generateAccessMail(user.mail);
				user.creationDate = UtilDate.now();

				UtilMail.sendMail(
						"Activation du compte",
						UtilMail.ADMIN,
						user.mail,
						UtilString
								.makeText(
										"Bonjour",
										user.firstName,
										user.lastName,
										". Afin que votre compte soit activé, veuillez suivre le lien suivant : https://82.227.210.119:25080/account/activate/"
												+ user.activationCode,
										"\nVotre code d'accès pour votre boîte mail est le suivant",
										user.accessMail));

				UserFactory.insert(user);
				return Index.index(Messages.get("activationAccount"));
			} else {
				return ok(views.html.account.createAccount.render(
						message.toString(), userForm));
			}
		}

	}

	/**
	 * Logout.
	 * 
	 * @return the result
	 */
	public static Result logout() {
		session().clear();
		return redirect(routes.Index.index(""));
	}

	/**
	 * Activate.
	 * 
	 * @param activationCode
	 *            the activation code
	 * @return the result
	 */
	public static Result activate(String activationCode) {
		User user = UserFactory.findByActivationCode(activationCode);

		if (null != user && !user.activate) {
			user.activate = true;
			user.activationCode = "";

			UserFactory.modify(user);
		}

		return redirect(routes.Index.index(Messages.get("accountActivate")));
	}

	public static Result loosePassword(String message) {
		return ok(views.html.account.loosePassword.render(message));
	}

	public static Result mailLoosePassword() {
		User user = UserFactory.findByMail(request().body().asFormUrlEncoded()
				.get("mailAddress")[0]);

		if (null == user) {
			return ok(views.html.account.loosePassword.render(Messages
					.get("mailNotFound")));
		} else if (!user.activate) {
			return ok(views.html.account.loosePassword.render(Messages
					.get("accountDeactivate")));
		} else {

			user.activationCode = Base64.encode(UtilDate.now().getBytes());
			user.activate = false;

			UserFactory.modify(user);

			UtilMail.sendMail(
					Messages.get("loosePassword"),
					UtilMail.ADMIN,
					user.mail,
					UtilString
							.makeText(
									"Bonjour",
									user.firstName,
									user.lastName,
									".Afin que votre compte soit réactivé, veuillez suivre le lien suivant : https://82.227.210.119:25080/account/reactivate/"
											+ user.activationCode));

			return redirect(controllers.routes.Index.index(Messages
					.get("activationAccount")));
		}
	}

	public static Result reactivateAccount(String activationCode) {
		User user = UserFactory.findByActivationCode(activationCode);

		if (null != user) {
			user.activationCode = "";
			UserFactory.modify(user);
			return secretQuestion(user, "");
		}

		return redirect(controllers.routes.Index.index(""));
	}

	public static Result secretQuestion(User user, String message) {
		return ok(views.html.account.secretQuestion.render(message, user));
	}

	public static Result responseSecretQuestion(String userMail) {
		User user = UserFactory.findByMail(userMail);

		String response = request().body().asFormUrlEncoded().get("response")[0];

		if (user.response.equals(response)) {
			return ok(views.html.account.newPassword.render("", user.mail));
		}

		return secretQuestion(user, Messages.get("badResponse"));
	}

	public static Result newPassword(String userMail) {
		String password = request().body().asFormUrlEncoded().get("password")[0];
		String confirmPassword = request().body().asFormUrlEncoded()
				.get("confirmPassword")[0];

		boolean bad = false;
		StringBuilder message = new StringBuilder();

		if (!password.equals(confirmPassword)) {
			bad = true;
			message.append(Messages.get("badConfirmPassword"));
			message.append(". ");
		}

		if (!UtilUserControl.passwordControl(password)) {
			bad = true;
			message.append(Messages.get("failPassword"));
		}

		if (bad) {
			return notFound(views.html.account.newPassword.render(
					message.toString(), userMail));
		}

		User user = UserFactory.findByMail(userMail);
		user.password = UtilSalage.encode(password);
		user.activate = true;
		UserFactory.modify(user);

		return redirect(controllers.routes.Index.index(Messages
				.get("accountActivate")));
	}

	public static Result backIndex() {
		return redirect(controllers.routes.Index.index(""));
	}

	public static Result indexAdmin(String nickname, String message) {
		return ok(views.html.index_admin.render(
				UserFactory.findByNickname(nickname), message));
	}

	public static Result checkActivationKey(String nickname) {

		String key = request().body().asFormUrlEncoded().get("key")[0];

		User user = UserFactory.findByNickname(nickname);

		if (key.equals(new Console(user.key).getKey())) {
			session().put("email", user.mail);
			session().put("nickname", user.nickname);
			session("token", Crypto.generateSignedToken());
			user.lastConnect = UtilDate.now();
			user.token = user.mail + "!" + user.nickname + "§"
					+ Crypto.sign(user.lastConnect);

			UserFactory.modify(user);

			return redirect(routes.Greeting.index(nickname));
		} else {
			return indexAdmin(nickname, Messages.get("badActivationKey"));
		}
	}
}
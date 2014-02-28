/*
 * 
 */
package controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.GreetingMessage;
import models.Message;
import models.Picture;
import models.Secured;
import models.User;
import play.Routes;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import util.UtilDate;
import util.UtilString;
import util.UtilUpload;
import views.html.find_friends;

import com.google.common.collect.Lists;

import enumeration.MessageType;
import factory.GreetingFactory;
import factory.MailFactory;
import factory.MessageFactory;
import factory.UserFactory;

/**
 * The Class Greeting.
 */
@Authenticated(Secured.class)
public class Greeting extends Controller {

	/** The message form. */
	static Form<Message> messageForm = Form.form(Message.class);

	/**
	 * Index.
	 * 
	 * @param nickname
	 *            the nickname
	 * @return the result
	 */
	public static Result index(String nickname) {
		User user = UserFactory.findByNickname(nickname);
		List<Message> messages = Lists.newArrayList(MessageFactory
				.findAllByMultiType(user.mail, MessageType.MESSAGE.getType(),
						MessageType.PICTURE.getType()).as(Message.class));

		return ok(views.html.greeting.index.render(
				GreetingFactory.find(user.mail).text, user, messageForm,
				messages));
	}

	/**
	 * Adds the message.
	 * 
	 * @return the result
	 */
	public static Result addMessage() {

		User user = UserFactory.findByMail(session().get("email"));
		Form<Message> filledForm = messageForm.bindFromRequest();

		Message message = new Picture();
		message.text = UtilUpload.uploadPicture(request(), "text",
				user.nickname);

		if (!filledForm.hasErrors()) {

			message = new Message();
			message = filledForm.get();

		}

		if (null != message.text) {
			message.userMail = user.mail;

			message.dateTime = UtilDate.now();

			MessageFactory.insert(message);
		}

		return redirect(routes.Greeting.index(user.nickname));
	}

	/**
	 * Delete message.
	 * 
	 * @param dateTime
	 *            the date time
	 * @return the result
	 */
	public static Result deleteMessage(String dateTime) {
		User user = UserFactory.findByMail(session().get("email"));

		MessageFactory.removeByDate(dateTime);

		return redirect(routes.Greeting.index(user.nickname));
	}

	/**
	 * Delete message.
	 * 
	 * @param dateTime
	 *            the date time
	 * @return the result
	 */
	public static Result commentMessage(String dateTime, String mail) {

		Message message = MessageFactory.findOneByDateAndMail(dateTime, mail);

		Message comment = new Message();
		comment.text = request().body().asFormUrlEncoded().get("comment")[0];
		comment.userMail = session().get("email");
		comment.dateTime = UtilDate.now();

		message.getComments().add(comment);

		MessageFactory.modify(message, mail);

		return redirect(routes.Greeting
				.index(UserFactory.findByMail(mail).nickname));
	}

	/**
	 * New greeting.
	 * 
	 * @return the result
	 */
	public static Result newGreeting() {
		User user = UserFactory.findByMail(session().get("email"));

		GreetingMessage greeting = new GreetingMessage();
		greeting.userMail = user.mail;
		greeting.text = request().body().asFormUrlEncoded().get("greeting")[0];

		GreetingFactory.insert(greeting);

		return redirect(routes.Greeting.index(user.nickname));
	}

	/**
	 * Find friends.
	 * 
	 * @param name
	 *            the name
	 * @return the result
	 */
	public static Result findFriends(String name) {
		Set<User> users = new HashSet<User>();

		if (UtilString.isNotBlank(name)) {
			users.addAll(UserFactory.findLikeMail(name));
			users.addAll(UserFactory.findLikeFirstname(name));
			users.addAll(UserFactory.findLikeLastname(name));
		}

		return ok(find_friends.render(users));
	}

	/**
	 * Ajax search friends.
	 * 
	 * @return the result
	 */
	public static Result ajaxSearchFriends() {
		response().setContentType("text/javascript");
		return ok(Routes.javascriptRouter("jsRoutes",
				controllers.routes.javascript.Greeting.findFriends()));
	}

	public static int mailNotRead() {
		return MailFactory.findAllNotRead(session().get("email")).size();
	}
}

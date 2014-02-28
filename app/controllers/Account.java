package controllers;

import java.io.File;

import models.Secured;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import util.UtilPicture;
import util.UtilSalage;
import util.UtilUpload;
import util.UtilUserControl;
import factory.UserFactory;

/**
 * The Class Account.
 */
@Authenticated(Secured.class)
public class Account extends Controller {

	/**
	 * Profile.
	 * 
	 * @param nickname
	 *            the nickname
	 * @return the result
	 */
	public static Result profile(String nickname) {
		return ok(views.html.account.profile.render(UserFactory
				.findByMail(UserFactory.findByNickname(nickname).mail)));
	}

	/**
	 * Modify nickname.
	 * 
	 * @return the result
	 */
	public static Result modifyNickname() {
		String nickname = request().body().asFormUrlEncoded().get("nickname")[0];

		User user = UserFactory.findByMail(session().get("email"));
		if (null == UserFactory.findByNickname(nickname)) {

			new File(UtilPicture.createPicturePath(user.nickname))
					.renameTo(new File(UtilPicture.createPicturePath(nickname)));

			user.nickname = nickname;

			UserFactory.modify(user);

			session("nickname", nickname);
		}

		return redirect(routes.Account.profile(user.nickname));
	}

	/**
	 * Modify first name.
	 * 
	 * @return the result
	 */
	public static Result modifyFirstName() {
		User user = UserFactory.findByMail(session("email"));
		user.firstName = request().body().asFormUrlEncoded().get("firstName")[0];

		UserFactory.modify(user);
		return redirect(routes.Account.profile(user.nickname));
	}

	/**
	 * Modify last name.
	 * 
	 * @return the result
	 */
	public static Result modifyLastName() {
		User user = UserFactory.findByMail(session("email"));
		user.lastName = request().body().asFormUrlEncoded().get("lastName")[0];

		UserFactory.modify(user);
		return redirect(routes.Account.profile(user.nickname));
	}

	/**
	 * Modify birthday.
	 * 
	 * @return the result
	 */
	public static Result modifyBirthday() {
		User user = UserFactory.findByMail(session("email"));
		user.birthday = request().body().asFormUrlEncoded().get("birthday")[0];

		UserFactory.modify(user);
		return redirect(routes.Account.profile(user.nickname));
	}

	/**
	 * Modify epsi class.
	 * 
	 * @return the result
	 */
	public static Result modifyEpsiClass() {
		User user = UserFactory.findByMail(session("email"));
		user.epsiClass = request().body().asFormUrlEncoded().get("epsiClass")[0];

		UserFactory.modify(user);
		return redirect(routes.Account.profile(user.nickname));
	}

	/**
	 * Modify avatar.
	 * 
	 * @return the result
	 */
	public static Result modifyAvatar() {
		User user = UserFactory.findByMail(session("email"));

		user.avatar = UtilUpload.uploadPicture(request(), "avatar",
				user.nickname);

		UserFactory.modify(user);
		return redirect(routes.Account.profile(user.nickname));
	}

	/**
	 * Modify avatar.
	 * 
	 * @return the result
	 */
	public static Result modifyPassword() {
		User user = UserFactory.findByMail(session("email"));

		boolean verif = true;

		String oldPassword = request().body().asFormUrlEncoded()
				.get("oldPassword")[0];
		String newPassword = request().body().asFormUrlEncoded()
				.get("newPassword")[0];
		String confirmPassword = request().body().asFormUrlEncoded()
				.get("confirmPassword")[0];

		if (!newPassword.equals(confirmPassword)
				|| !UtilSalage.encode(oldPassword).equals(user.password)
				|| !UtilUserControl.passwordControl(newPassword)) {
			verif = false;
		}

		if (verif) {
			user.password = UtilSalage.encode(newPassword);
			UserFactory.modify(user);
		}
		return redirect(routes.Account.profile(user.nickname));
	}
}

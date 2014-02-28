/*
 * 
 */
package factory;

import java.util.List;

import models.User;
import util.UtilSalage;

import com.google.common.collect.Lists;

/**
 * A factory for creating User objects.
 */
public class UserFactory {

	/**
	 * Find one.
	 * 
	 * @param mail
	 *            the mail
	 * @param password
	 *            the password
	 * @return the user
	 */
	public static User findOne(String mail, String password) {
		return Database
				.accessUser()
				.findOne("{mail:#,password:#}", mail,
						UtilSalage.encode(password)).as(User.class);
	}

	/**
	 * Find by mail.
	 * 
	 * @param mail
	 *            the mail
	 * @return the user
	 */
	public static User findByMail(String mail) {
		return Database.accessUser().findOne("{mail:#}", mail).as(User.class);
	}

	/**
	 * Find like mail.
	 * 
	 * @param mail
	 *            the mail
	 * @return the list
	 */
	public static List<User> findLikeMail(String mail) {
		return Lists.newArrayList(Database.accessUser()
				.find("{mail:{ $regex: # } }", "^" + mail + ".*")
				.as(User.class));
	}

	/**
	 * Find like firstname.
	 * 
	 * @param firstname
	 *            the firstname
	 * @return the list
	 */
	public static List<User> findLikeFirstname(String firstname) {
		return Lists.newArrayList(Database.accessUser()
				.find("{firstName:{ $regex: # } }", "^" + firstname + ".*")
				.as(User.class));
	}

	/**
	 * Find like lastname.
	 * 
	 * @param lastname
	 *            the lastname
	 * @return the list
	 */
	public static List<User> findLikeLastname(String lastname) {
		return Lists.newArrayList(Database.accessUser()
				.find("{lastName: { $regex: #} }", "^" + lastname + ".*")
				.as(User.class));
	}

	/**
	 * Find by nickname.
	 * 
	 * @param pseudo
	 *            the pseudo
	 * @return the user
	 */
	public static User findByNickname(String pseudo) {
		return Database.accessUser().findOne("{nickname:#}", pseudo)
				.as(User.class);
	}

	/**
	 * All.
	 * 
	 * @return the list
	 */
	public static List<User> all() {
		return Lists.newArrayList(Database.accessUser().find().as(User.class));
	}

	/**
	 * Insert.
	 * 
	 * @param user
	 *            the user
	 */
	public static void insert(User user) {
		Database.accessUser().insert(user);
	}

	/**
	 * Delete.
	 * 
	 * @param mail
	 *            the mail
	 * @param nickname
	 *            the nickname
	 */
	public static void delete(String mail, String nickname) {
		Database.accessUser().remove("{mail:#, nickname:#}", mail, nickname);
	}

	/**
	 * Modify.
	 * 
	 * @param user
	 *            the user
	 */
	public static void modify(User user) {
		Database.accessUser().update("{mail:#}", user.mail).merge(user);
	}

	/**
	 * Find by activation code.
	 * 
	 * @param activationCode
	 *            the activation code
	 * @return the user
	 */
	public static User findByActivationCode(String activationCode) {
		return Database.accessUser()
				.findOne("{activationCode:#}", activationCode).as(User.class);
	}

}

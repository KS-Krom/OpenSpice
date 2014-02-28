/*
 * 
 */
package factory;

import java.util.List;

import models.Mail;

import com.google.common.collect.Lists;

import enumeration.MessageType;

/**
 * A factory for creating Mail objects.
 */
public class MailFactory {

	/**
	 * Insert.
	 * 
	 * @param mail
	 *            the mail
	 */
	public static void insert(Mail mail) {
		Database.accessMessage().insert(mail);
	}

	/**
	 * Find all.
	 * 
	 * @param userMail
	 *            the user mail
	 * @return the list
	 */
	public static List<Mail> findAll(String userMail) {
		return Lists.newArrayList(MessageFactory.findAllByUserAndMessage(
				userMail, MessageType.MAIL.getType()).as(Mail.class));
	}

	public static List<Mail> findAllNotRead(String userMail) {
		return Lists.newArrayList(Database
				.accessMessage()
				.find("{to:#, messageType:#, read:false}", userMail,
						MessageType.MAIL).as(Mail.class));
	}

	public static List<Mail> findAllMailByTo(String to) {
		return Lists.newArrayList(Database.accessMessage()
				.find("{to:#,messageType:#}", to, MessageType.MAIL)
				.as(Mail.class));
	}

}

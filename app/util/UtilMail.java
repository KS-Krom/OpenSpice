/*
 * 
 */
package util;

import models.User;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import play.Logger;
import play.mvc.Http.MultipartFormData.FilePart;

/**
 * The Class UtilMail.
 */
public class UtilMail {

	public static final String ADMIN = "Admin<openspicegirl@epsi.fr>";

	/**
	 * Send mail.
	 * 
	 * @param subject
	 *            the subject
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param body
	 *            the body
	 */
	public static void sendMail(String subject, String from, String to,
			String body) {
		try {

			Email sendMail = new MultiPartEmail();

			createMail(subject, from, to, body, sendMail);

			sendMail.send();

		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send multi part mail.
	 * 
	 * @param subject
	 *            the subject
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param body
	 *            the body
	 * @param path
	 *            the path
	 * @param name
	 *            the name
	 */
	public static void sendMultiPartMail(String subject, String from,
			String to, String body, String nickname, String name) {

		MultiPartEmail mail = new MultiPartEmail();
		createMail(subject, from, to, body, mail);

		try {
			mail.attach(createAttachment(nickname, name));
			mail.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send multi part mail.
	 * 
	 * @param subject
	 *            the subject
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param body
	 *            the body
	 * @param path
	 *            the path
	 * @param name
	 *            the name
	 */
	public static void sendMultiPartMail(String subject, String from,
			String to, String body, FilePart part) {

		MultiPartEmail mail = new MultiPartEmail();
		createMail(subject, from, to, body, mail);

		try {
			mail.attach(createAttachment(part));
			mail.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the mail.
	 * 
	 * @param subject
	 *            the subject
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param body
	 *            the body
	 * @param mail
	 *            the mail
	 */
	private static void createMail(String subject, String from, String to,
			String body, Email mail) {

		Logger.debug("Subject : " + subject);
		Logger.debug("From : " + from);
		Logger.debug("To : " + to);

		initConnection(mail);
		try {
			mail.setFrom(from);
			mail.setSubject(subject);
			mail.addTo(to);
			mail.setMsg(body);
		} catch (EmailException e) {
			Logger.error("Message : " + e.getMessage());
			Logger.error("Localized Message : " + e.getLocalizedMessage());
		}
	}

	/**
	 * Creates the attachment.
	 * 
	 * @param path
	 *            the path
	 * @param name
	 *            the name
	 * @return the email attachment
	 */
	private static EmailAttachment createAttachment(String nickname, String name) {
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(UtilUpload.createFilePath(nickname, name));
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setName(name);

		return attachment;
	}

	/**
	 * Creates the attachment.
	 * 
	 * @param path
	 *            the path
	 * @param name
	 *            the name
	 * @return the email attachment
	 */
	private static EmailAttachment createAttachment(FilePart part) {
		EmailAttachment attachment = new EmailAttachment();
		attachment.setPath(part.getFile().getAbsolutePath());
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		attachment.setName(part.getFilename());

		Logger.debug("Path : " + part.getFile().getAbsolutePath());
		Logger.debug("Name : " + part.getFilename());

		return attachment;
	}

	/**
	 * Send mail deactivate account.
	 * 
	 * @param user
	 *            the user
	 */
	public static void sendMailDeactivateAccount(User user, String time) {

		sendMail("Suspension de compte", UtilMail.ADMIN,
				"swlalatemporaire@gmail.com", UtilString.makeText("Le compte",
						user.lastName, user.firstName, user.mail,
						"est supendu pour une durée de :", time));

		sendMail("Suspension de compte", UtilMail.ADMIN, user.mail,
				UtilString.makeText(
						"Votre compte est supendu pour une durée de :", time));
	}

	/**
	 * Inits the connection.
	 * 
	 * @param email
	 *            the email
	 */
	private static void initConnection(Email email) {
		email.setHostName("smtp.free.fr");
		email.setSmtpPort(25);
	}
}

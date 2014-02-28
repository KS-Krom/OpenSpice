/*
 * 
 */
package factory;

import models.Message;

import org.jongo.Find;

public class MessageFactory {
	/**
	 * A factory for creating Message objects.
	 */

	/**
	 * Insert.
	 * 
	 * @param message
	 *            the message
	 */
	public static void insert(Message message) {
		Database.accessMessage().insert(message);
	}

	/**
	 * Find all by user and message.
	 * 
	 * @param mail
	 *            the mail
	 * @param type
	 *            the type
	 * @return the find
	 */
	public static Find findAllByUserAndMessage(String mail, String type) {
		return Database.accessMessage().find("{userMail:#, messageType:#}",
				mail, type);
	}

	/**
	 * Removes the by date.
	 * 
	 * @param dateTime
	 *            the date time
	 */
	public static void removeByDate(String dateTime) {
		Database.accessMessage().remove("{dateTime:#}", dateTime);
	}

	/**
	 * Find one by date.
	 * 
	 * @param dateTime
	 *            the date time
	 * @return the message
	 */
	public static Message findOneByDateAndMail(String dateTime, String mail) {
		return Database.accessMessage()
				.findOne("{dateTime:#, userMail:#}", dateTime, mail)
				.as(Message.class);
	}

	/**
	 * Find all by type.
	 * 
	 * @param type
	 *            the type
	 * @return the find
	 */
	public static Find findAllByType(String type) {
		return Database.accessMessage().find("{ messageType:#}", type);
	}

	/**
	 * Find all by multi type.
	 * 
	 * @param mail
	 *            the mail
	 * @param types
	 *            the types
	 * @return the find
	 */
	public static Find findAllByMultiType(String mail, String... types) {
		StringBuilder request = new StringBuilder("{userMail:");
		request.append("'" + mail + "',");
		request.append("messageType:{$in:[");
		int size = types.length;
		for (int i = 0; i < size; ++i) {
			if (i != 0) {
				request.append(",");
			}
			request.append("'");
			request.append(types[i]);
			request.append("'");

		}
		request.append("]}}");
		return Database.accessMessage().find(request.toString())
				.sort("{dateTime:1}");
	}

	public static void modify(Message message, String mail) {
		Database.accessMessage()
				.update("{userMail:#, dateTime:#}", mail, message.dateTime)
				.merge(message);
	}

}

/*
 * 
 */
package util;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.User;

import org.joda.time.Interval;

import play.i18n.Messages;
import factory.UserFactory;

/**
 * The Class UtilUserControl.
 */
public class UtilUserControl {

	private static String[] time = new String[] { "20 minutes", "1 heure",
			"12 heures", "24 heures", "Définitif" };

	/**
	 * Bad connection.
	 * 
	 * @param user
	 *            the user
	 * @param ipAdress
	 *            the ip adress
	 * @return true, if successful
	 */
	public static boolean badConnection(User user, String ipAdress,
			StringBuilder message) {
		boolean badConnection = true;

		User otherUser = UserFactory.findOne(user.mail, user.password);

		if (null == otherUser) {
			otherUser = UserFactory.findByMail(user.mail);
			if (null != otherUser && otherUser.activate) {
				if (otherUser.attempt < 3) {
					++otherUser.attempt;
				} else {
					++otherUser.numberFail;
					otherUser.activate = false;
					otherUser.attempt = 0;
					otherUser.ipAdress = ipAdress;
					otherUser.failTimestamp = UtilDate.now();

					UtilMail.sendMailDeactivateAccount(otherUser,
							time[otherUser.numberFail - 1]);

					messageDeactivate(message);
				}
				UserFactory.modify(otherUser);
			} else if (null != otherUser) {
				messageDeactivate(message);
			}
		} else {
			if (otherUser.activate) {
				badConnection = false;
				otherUser.attempt = 0;
			} else {
				badConnection = lockAccount(otherUser);
				if (!badConnection) {
					otherUser.activate = true;
					otherUser.numberFail = 0;
				} else {
					messageDeactivate(message);
				}
			}
			UserFactory.modify(otherUser);
		}

		return badConnection;
	}

	private static void messageDeactivate(StringBuilder message) {
		message.delete(0, message.length());
		message.append(Messages.get("accountDeactivate"));
	}

	/**
	 * Lock account.
	 * 
	 * @param user
	 *            the user
	 * @return true, if successful
	 */
	private static boolean lockAccount(User user) {
		boolean lock = true;

		try {
			Interval interval = new Interval(UtilDate
					.dateFormat("dd/MM/yy - HH:mm:ss")
					.parse(user.failTimestamp).getTime(), new Date().getTime());
			long minutePassed = interval.toDuration().getStandardMinutes();

			switch (user.numberFail) {
			case 1:
				if (minutePassed > 20) {
					lock = false;
				}
				break;

			case 2:
				if (minutePassed > 60) {
					lock = false;
				}
				break;
			case 3:
				if (minutePassed > 720) {
					lock = false;
				}
				break;
			case 4:
				if (minutePassed > 1440) {
					lock = false;
				}
				break;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return lock;

	}

	/**
	 * Password control.
	 * 
	 * @param password
	 *            the password
	 * @return true, if successful
	 */
	public static boolean passwordControl(String password) {
		boolean correct = false;
		int size = password.length();

		if (size > 6) {
			Pattern patternMin = Pattern.compile("[a-z]");
			Matcher matcherMin = patternMin.matcher(password);

			if (matcherMin.find()) {
				Pattern patternMaj = Pattern.compile("[A-Z]");
				Matcher matcherMaj = patternMaj.matcher(password);

				if (matcherMaj.find()) {
					Pattern patternNum = Pattern.compile("[0-9]");
					Matcher matcherNum = patternNum.matcher(password);

					if (matcherNum.find()) {
						Pattern patternSpe = Pattern.compile("[^\\w]");
						Matcher matcherSpe = patternSpe.matcher(password);

						if (matcherSpe.find()) {
							correct = true;
						}
					}
				}
			}
		}

		return correct;
	}
}

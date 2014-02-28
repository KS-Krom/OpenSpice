/*
 * 
 */
package factory;

import java.util.List;

import models.GreetingMessage;

import com.google.common.collect.Lists;

import enumeration.MessageType;

/**
 * A factory for creating Greeting objects.
 */
public class GreetingFactory {

	/**
	 * Insert.
	 *
	 * @param greeting the greeting
	 */
	public static void insert(GreetingMessage greeting) {
		Database.accessMessage()
				.remove("{messageType:#}", MessageType.GREETING);
		Database.accessMessage().insert(greeting);
	}

	/**
	 * Find.
	 *
	 * @param userMail the user mail
	 * @return the greeting message
	 */
	public static GreetingMessage find(String userMail) {
		GreetingMessage message = new GreetingMessage();

		List<GreetingMessage> listMessage = Lists.newArrayList(MessageFactory
				.findAllByType(MessageType.GREETING.getType()).as(
						GreetingMessage.class));

		if (listMessage.size() != 0) {
			message = listMessage.get(0);
		}

		return message;
	}
}

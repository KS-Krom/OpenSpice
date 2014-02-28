/*
 * 
 */
package models;

import enumeration.MessageType;

/**
 * The Class GreetingMessage.
 */
public class GreetingMessage extends Message {

	/**
	 * Instantiates a new greeting message.
	 */
	public GreetingMessage() {
		this.messageType = MessageType.GREETING;
	}
}

/*
 * 
 */
package models;

import play.data.validation.Constraints.Required;
import enumeration.MessageType;

/**
 * The Class Mail.
 */
public class Mail extends Message {

	/** The to. */
	@Required
	public String to;

	/** The subject. */
	@Required
	public String subject;

	public String file;

	public boolean read;

	/**
	 * Instantiates a new mail.
	 */
	public Mail() {
		this.messageType = MessageType.MAIL;
		this.read = false;
	}

	@Override
	public String toString() {
		return super.toString().split("@")[1];
	}

}

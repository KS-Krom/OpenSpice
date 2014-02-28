/*
 * 
 */
package models;

import java.util.ArrayList;
import java.util.List;

import play.data.validation.Constraints.Required;
import enumeration.MessageType;

/**
 * The Class Message.
 */
public class Message {

	/** The text. */
	@Required
	public String text;

	/** The date time. */
	public String dateTime;

	/** The user mail. */
	public String userMail;

	/** The message type. */
	public MessageType messageType;

	public List<Message> comments;

	/**
	 * Instantiates a new message.
	 */
	public Message() {
		this.messageType = MessageType.MESSAGE;
	}

	public List<Message> getComments() {
		if (null == this.comments) {
			this.comments = new ArrayList<Message>();
		}

		return this.comments;
	}

}
/*
 * 
 */
package models;

import enumeration.MessageType;

/**
 * The Class Picture.
 */
public class Picture extends Message {

	/**
	 * Instantiates a new picture.
	 */
	public Picture() {
		this.messageType = MessageType.PICTURE;
	}

}

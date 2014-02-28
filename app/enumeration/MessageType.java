/*
 * 
 */
package enumeration;

/**
 * The Enum MessageType.
 */
public enum MessageType {

	/** The message. */
	MESSAGE("MESSAGE"), /** The mail. */
 MAIL("MAIL"), /** The greeting. */
 GREETING("GREETING"), /** The picture. */
 PICTURE("PICTURE");

	/** The type. */
	private String type;

	/**
	 * Instantiates a new message type.
	 *
	 * @param type the type
	 */
	private MessageType(String type) {
		this.type = type;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

}

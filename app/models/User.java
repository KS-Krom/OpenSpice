/*
 * 
 */
package models;

import java.util.List;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import enumeration.UserRole;

/**
 * The Class User.
 */
public class User {

	/** The first name. */
	@Required
	public String firstName;

	/** The last name. */
	@Required
	public String lastName;

	/** The nickname. */
	@Required
	public String nickname;

	/** The password. */
	@Required
	public String password;

	/** The mail. */
	@Required
	@Email
	public String mail;

	/** The list messages. */
	public List<Message> listMessages;

	/** The question. */
	public String question;

	/** The response. */
	@Required
	public String response;

	/** The roles. */
	public UserRole roles;

	/** The activate. */
	public boolean activate;

	/** The birthday. */
	public String birthday;

	/** The epsi class. */
	public String epsiClass;

	/** The attempt. */
	public int attempt;

	/** The ip adress. */
	public String ipAdress;

	/** The activation code. */
	public String activationCode;

	/** The avatar. */
	public String avatar;

	/** The fail timestamp. */
	public String failTimestamp;

	/** The number fail. */
	public int numberFail;

	public String lastConnect;

	public String token;

	public String flag;

	public String flagMail;

	public String key;

	public String accessMail;

	public String creationDate;

	public User() {
		this.activate = false;
		this.attempt = 0;
		this.roles = UserRole.USER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return this.mail.equals(((User) obj).mail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 31 + this.mail.length();
	}

}

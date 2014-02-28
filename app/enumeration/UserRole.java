/*
 * 
 */
package enumeration;

/**
 * The Enum UserRole.
 */
public enum UserRole {

	/** The user. */
	USER("USER"), /** The admin. */
 ADMIN("ADMIN");

	/** The role. */
	private String role;

	/**
	 * Instantiates a new user role.
	 *
	 * @param role the role
	 */
	private UserRole(String role) {

		this.role = role;
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
}

/*
 * 
 */
package enumeration;

/**
 * The Enum EpsiClass.
 */
public enum EpsiClass {

	/** The BACHELO r_1. */
	BACHELOR_1("BACHELOR 1"), /** The BACHELO r_2. */
 BACHELOR_2("BACHELOR 2"), /** The BACHELO r_3. */
 BACHELOR_3("BACHELOR 3"), /** The INGENIERI e_4. */
 INGENIERIE_4(
			"INGENIERIE 4"), 
 /** The INGENIERI e_5. */
 INGENIERIE_5("INGENIERIE 5"), 
 /** The autre. */
 AUTRE("AUTRE");

	/** The name. */
	private String name;

	/**
	 * Instantiates a new epsi class.
	 *
	 * @param name the name
	 */
	private EpsiClass(String name) {
		this.name = name;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}

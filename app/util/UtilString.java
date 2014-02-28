/*
 * 
 */
package util;

/**
 * The Class UtilString.
 */
public class UtilString {

	/** The Constant ESPACE. */
	private static final String ESPACE = " ";

	/**
	 * Make text.
	 * 
	 * @param strings
	 *            the strings
	 * @return the string
	 */
	public static String makeText(String... strings) {
		StringBuilder text = new StringBuilder();
		int size = strings.length;

		for (int i = 0; i < size; ++i) {
			if (i != 0) {
				text.append(ESPACE);
			}
			text.append(strings[i]);
		}

		return text.toString();
	}

	/**
	 * Checks if is blank.
	 * 
	 * @param string
	 *            the string
	 * @return true, if is blank
	 */
	public static boolean isBlank(String string) {
		return null == string || ("").equals(string.replaceAll(" ", ""));
	}

	/**
	 * Checks if is not blank.
	 * 
	 * @param string
	 *            the string
	 * @return true, if is not blank
	 */
	public static boolean isNotBlank(String string) {
		return !isBlank(string);
	}

}

/*
 * 
 */
package util;

import java.util.ArrayList;
import java.util.List;

import play.api.libs.Crypto;

import com.ning.http.util.Base64;

/**
 * The Class UtilSalage.
 */
public class UtilSalage {

	/** The Constant permutation1. */
	private static final int[] permutation1 = { 8, 2, 10, 5, 1, 3, 0, 7, 11, 4,
			6, 9 };

	/** The Constant permutation2. */
	private static final int[] permutation2 = { 2, 8, 9, 7, 0, 5, 4, 10, 1, 6,
			3, 11 };

	/** The Constant permutation3. */
	private static final int[] permutation3 = { 10, 4, 2, 0, 11, 7, 9, 3, 8, 6,
			1, 5 };

	/** The Constant permutation4. */
	private static final int[] permutation4 = { 6, 0, 8, 1, 5, 11, 7, 3, 10, 9,
			2, 4 };

	/**
	 * Encode.
	 * 
	 * @param password
	 *            the password
	 * @return the string
	 */
	public static String encode(String password) {
		List<String> listPwd = cutAndEncode(Crypto.encryptAES(password));

		List<String> tmpPwd = new ArrayList<String>(4);

		tmpPwd.add(permutation(listPwd.get(0), permutation1));
		tmpPwd.add(permutation(listPwd.get(1), permutation2));
		tmpPwd.add(permutation(listPwd.get(2), permutation3));
		tmpPwd.add(permutation(listPwd.get(3), permutation4));

		return gather(tmpPwd);
	}

	/**
	 * Cut and encode.
	 * 
	 * @param password
	 *            the password
	 * @return the list
	 */
	private static List<String> cutAndEncode(String password) {
		List<String> listPwd = new ArrayList<String>(4);

		listPwd.add(Base64.encode(password.substring(0, 8).getBytes()));

		listPwd.add(Base64.encode(password.substring(8, 16).getBytes()));

		listPwd.add(Base64.encode(password.substring(16, 24).getBytes()));

		listPwd.add(Base64.encode(password.substring(24, 32).getBytes()));

		return listPwd;
	}

	/**
	 * Permutation.
	 * 
	 * @param password
	 *            the password
	 * @param permutation
	 *            the permutation
	 * @return the string
	 */
	private static String permutation(String password, int[] permutation) {
		StringBuilder returnPwd = new StringBuilder(12);

		for (int i : permutation) {
			returnPwd.append(password.charAt(i));
		}

		return returnPwd.toString();
	}

	/**
	 * Gather.
	 * 
	 * @param listPwd
	 *            the list pwd
	 * @return the string
	 */
	private static String gather(List<String> listPwd) {
		StringBuilder builder = new StringBuilder(48);

		for (String s : listPwd) {
			builder.append(s);
		}

		return builder.toString();
	}

	private static final ArrayList<String> tab = new ArrayList<String>();

	private static void stackTab() {

		for (int j = 0; j < 4; ++j) {
			for (int i = 0; i <= 9; i++) {
				tab.add(Integer.toString(i));
			}
		}

		for (char alpha = 'a'; alpha <= 'z'; alpha++)
			tab.add(String.valueOf(alpha));
	}

	public static String generateFlag() {

		if (tab.isEmpty()) {
			stackTab();
		}

		String buff = "";

		for (int i = 0; i < 64; i++) {
			int nb = (int) (Math.random() * tab.size());
			buff += tab.get(nb);
		}

		return buff;
	}

	public static String generateAccessMail(String mail) {
		return Base64.encode(mail.getBytes()).replaceAll("=", "");
	}

}

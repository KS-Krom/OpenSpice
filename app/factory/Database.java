/*
 * 
 */
package factory;

import org.jongo.MongoCollection;

import uk.co.panaxiom.playjongo.PlayJongo;
import util.UtilConf;

/**
 * The Class Database.
 */
public class Database {

	/**
	 * Access user.
	 * 
	 * @return the mongo collection
	 */
	public static MongoCollection accessUser() {
		if (authenticate()) {
			return PlayJongo.getCollection("user");
		} else {
			return null;
		}

	}

	/**
	 * Access message.
	 * 
	 * @return the mongo collection
	 */
	public static MongoCollection accessMessage() {
		if (authenticate()) {
			return PlayJongo.getCollection("message");
		} else {
			return null;
		}

	}

	private static boolean authenticate() {
		return PlayJongo.getDatabase().authenticate(
				UtilConf.getString("mongodb.user"),
				UtilConf.getString("mongodb.password").toCharArray());
	}
}

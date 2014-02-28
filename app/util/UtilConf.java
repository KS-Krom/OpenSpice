package util;

import play.Configuration;
import play.Play;

public class UtilConf {

	private static Configuration conf;

	private static Configuration getConf() {
		if (null == conf) {
			conf = Play.application().configuration();
		}
		return conf;
	}

	public static String getString(String key) {
		return getConf().getString(key);
	}

}

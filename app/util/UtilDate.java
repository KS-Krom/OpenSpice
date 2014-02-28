package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilDate {

	public static String now() {
		return dateFormat("dd/MM/yy - HH:mm:ss").format(new Date());
	}

	public static DateFormat dateFormat(String format) {
		return new SimpleDateFormat(format);
	}

}

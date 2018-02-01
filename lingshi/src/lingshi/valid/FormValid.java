package lingshi.valid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValid {
	public static boolean isPhone(String str) {
		if (str == null) {
			return false;
		}
		Pattern p = Pattern.compile("^(1[0-9][0-9])\\d{8}$"); // 默认号段
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static boolean isEmail(String str) {
		if (str == null) {
			return false;
		}
		Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static boolean isDate(String str, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			dateFormat.parse(str);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
}

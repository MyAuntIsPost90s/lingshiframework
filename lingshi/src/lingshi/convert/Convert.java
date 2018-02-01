package lingshi.convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Convert {
	public static int toInt(String str) {
		return Integer.parseInt(str);
	}

	public static long toLong(String str) {
		return Long.parseLong(str);
	}

	public static List<Integer> toInts(List<String> strs) {
		List<Integer> intList = new ArrayList<>();
		if (strs == null || strs.size() < 1)
			return intList;

		for (String item : strs) {
			intList.add(toInt(item));
		}

		return intList;
	}

	public static List<Integer> toInts(String str, String split) {
		String[] strs = str.split(split);
		return toInts(Arrays.asList(strs));
	}

	public static List<Long> toLongs(List<String> strs) {
		List<Long> intList = new ArrayList<>();
		if (strs == null || strs.size() < 1)
			return intList;

		for (String item : strs) {
			intList.add(toLong(item));
		}

		return intList;
	}

	public static List<Long> toLongs(String str, String split) {
		String[] strs = str.split(split);
		return toLongs(Arrays.asList(strs));
	}

	public static Date toDate(String str, String format) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.parse(str);
	}

	public static Date toDate(long num) {
		return new Date(num);
	}
}

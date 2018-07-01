package lingshi.valid;

public class StringValid {
	/**
	 * string为null或空字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str == null) {
			return true;
		}
		if (str.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isNotNullOrEmpty(String str) {
		return !isNotNullOrEmpty(str);
	}

	public static boolean isNullOrWhiteSpace(String str) {
		if (str == null) {
			return true;
		}
		str = str.trim();
		if (str.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isNotNullOrWhiteSpace(String str) {
		return !isNullOrWhiteSpace(str);
	}
}

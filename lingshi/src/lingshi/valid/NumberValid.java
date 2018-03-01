package lingshi.valid;

public class NumberValid {

	public static boolean isNumber(String str) {
		if (str == null) {
			return false;
		}
		String reg = "^[0-9]+(.[0-9]+)?$";
		return str.matches(reg);
	}

	public static boolean isIntOrLong(String str) {
		if (str == null) {
			return false;
		}
		String reg = "^\\d+$";
		return str.matches(reg);
	}
}

package lingshi.utilities;

import java.lang.reflect.Field;

public class ObjectUtil {
	public static <T> T setObjectNull(T t) {
		Field[] fields = t.getClass().getFields();
		Field logField = null;
		try {
			for (Field field : fields) {
				logField = field;
				field.setAccessible(true);
				field.set(t, null);
			}
		} catch (Exception e) {
			System.out.println("set null error for" + logField.getName() + ":" + e.getMessage());
		}
		return t;
	}
}

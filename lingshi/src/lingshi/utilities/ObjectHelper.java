package lingshi.utilities;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

public class ObjectHelper {
	public static <T> T setObjectNull(T t) {
		Field[] fields = t.getClass().getFields();

		for (Field field : fields) {
			field.setAccessible(true);

			try {
				field.set(t, null);
			} catch (Exception e) {
				Logger.getRootLogger().info("set null error for " + field.getName() + ":" + e.getMessage());
			}
		}

		return t;
	}
}

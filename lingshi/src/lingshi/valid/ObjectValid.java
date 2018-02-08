package lingshi.valid;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

public class ObjectValid {

	/**
	 * 判断两个obj是否相等
	 * 
	 * @param aobj
	 * @param bobj
	 * @return
	 */
	public static boolean isEquals(Object aobj, Object bobj) {
		Field[] aFields = aobj.getClass().getFields();
		Field[] bFields = bobj.getClass().getFields();

		if (aFields.length != bFields.length) {
			return false;
		}
		try {
			for (int i = 0; i < aFields.length; i++) {
				Field aField = aFields[i];
				Field bField = bFields[i];
				aField.setAccessible(true);
				bField.setAccessible(true);
				if (aField.getName() != bField.getName()) {
					return false;
				}
				if (!aField.get(aobj).equals(bField.get(bobj))) {
					return false;
				}
			}
		} catch (Exception e) {
			Logger.getRootLogger().info(e);
		}
		return true;
	}

	/**
	 * 判断object是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		Field[] fields = obj.getClass().getFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.get(obj) != null) {
					return false;
				}
			}
		} catch (Exception e) {
			Logger.getRootLogger().info(e);
		}
		return true;
	}
}

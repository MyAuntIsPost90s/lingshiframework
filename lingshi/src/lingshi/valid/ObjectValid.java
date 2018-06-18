package lingshi.valid;

import java.lang.reflect.Field;
import java.util.Collection;

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
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 判断object是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		Field[] fields = obj.getClass().getFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.get(obj) != null) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean isEmpty(Collection<?> list) {
		return list == null || list.isEmpty();
	}

	public static boolean isNull(Object obj) {
		return obj == null;
	}

	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static boolean isNotEmpty(Collection<?> list) {
		return isEmpty(list);
	}

}

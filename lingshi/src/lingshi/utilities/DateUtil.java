package lingshi.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final String YYYY = "yyyy";
	public static final String YYYYMM = "yyyyMM";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYYMMDDHH = "yyyyMMddHH";
	public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static final String YYYYMMSpt = "yyyy-MM";
	public static final String YYYYMMDDSpt = "yyyy-MM-dd";
	public static final String YYYYMMDDHHSpt = "yyyy-MM-dd HH";
	public static final String YYYYMMDDHHMMSpt = "yyyy-MM-dd HH:mm";
	public static final String YYYYMMDDHHMMSSSpt = "yyyy-MM-dd HH:mm:ss";

	public static final String HH = "HH";
	public static final String HHMM = "HHmm";
	public static final String HHMMSS = "HHmmss";

	public final String HHMMSpt = "HH:mm";
	public final String HHMMSSSpt = "HH:mm:ss";

	public static String format(String pattern) {
		SimpleDateFormat formater = new SimpleDateFormat(pattern);
		return formater.format(new Date());
	}

	public static String format(Date date, String pattern) {
		SimpleDateFormat formater = new SimpleDateFormat(pattern);
		return formater.format(date);
	}

	public static Date addDay(int day) {
		return addDay(new Date(), day);
	}

	public static Date addDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	public static Date addMonth(int month) {
		return addMonth(new Date(), month);
	}

	public static Date addMonth(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}

	public static Date addYear(int year) {
		return addYear(new Date(), year);
	}

	public static Date addYear(Date date, int addYear) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, addYear);
		return calendar.getTime();
	}

	public static Date addHour(int hour) {
		return addHour(new Date(), hour);
	}

	public static Date addHour(Date date, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hour);
		return calendar.getTime();
	}

	public static Date addMinute(int minute) {
		return addMinute(new Date(), minute);
	}

	public static Date addMinute(Date date, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	public static Date addSecond(int second) {
		return addSecond(new Date(), second);
	}

	public static Date addSecond(Date date, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}
}

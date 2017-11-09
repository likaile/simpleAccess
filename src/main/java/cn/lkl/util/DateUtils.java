package cn.lkl.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类(提供字符串和日期互相转换的方法)
 * @author Administrator
 *
 */
public class DateUtils {

	/**
	 * 将日期转换为字符串(格式:yyyy-MM-dd)
	 * @param date
	 * @return
	 */
	public static String dateToStringYMd(Date date) {
		return dateToString(date,"yyyy-MM-dd");
	}
	
	/**
	 * 将日期转换为字符串(格式:yyyy-MM-dd HH:mm:ss)
	 * @param date
	 * @return
	 */
	public static String dateToStringYMdHms(Date date) {
		return dateToString(date,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 将日期转换为字符串(格式:yyyy-MM-dd HH:mm:ss.SSS)
	 * @param date
	 * @return
	 */
	public static String dateToStringYMdHmsS(Date date) {
		return dateToString(date,"yyyy-MM-dd HH:mm:ss.SSS");
	}
	
	
	/**
	 * 将日期转换为字符串
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date,String pattern) {
		String sDate = new SimpleDateFormat(pattern).format(date);
		return sDate;
	}

	/**
	 * 将字符串转换为短日期
	 * @param sDate
	 * @return
	 */
	public static Date stringToDateYMd(String sDate) {
		return stringToDate(sDate,"yyyy-MM-dd");
	}
	
	/**
	 * 将字符串转换为长日期
	 * @param sDate
	 * @return
	 */
	public static Date stringToDateYMdHms(String sDate) {
		return stringToDate(sDate,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 将字符串转换为长日期
	 * @param sDate
	 * @return
	 */
	public static Date stringToDateYMdHmsS(String sDate) {
		return stringToDate(sDate,"yyyy-MM-dd HH:mm:ss.SSS");
	}
	
	/**
	 * 将字符串转换为日期
	 * @param sDate
	 * @return
	 */
	public static Date stringToDate(String sDate,String pattern) {
		Date date = null;
		try {
			date = new SimpleDateFormat(pattern).parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("日期转换错误");
		}
		return date;
	}
	
	/**
	 * @描述：将毫秒值转换为格式化的长日期值
	 * @开发人员：likaihao
	 * @开发时间：2016年1月29日 下午2:58:57
	 * @param time
	 * @return
	 */
	public static String millisecondToLongDateStr(Long time){
		return dateToString(new Date(time),"yyyy-MM-dd HH:mm:ss.SSS");
	}
	
	/**
	 * @描述：将毫秒值转换为格式化的长日期值 yyyy-MM-dd HH:mm:ss.SSS
	 * @开发人员：likaihao
	 * @开发时间：2016年1月29日 下午2:58:57
	 * @param time
	 * @return
	 */
	public static String millisecondToLongDateStr(String time){
		return millisecondToLongDateStr(new Long(time));
	}
	
}

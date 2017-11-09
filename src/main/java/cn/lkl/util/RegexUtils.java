package cn.lkl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	
	/**
	 * 根据正则从一个字符串中获取所有匹配项(显示最后一个组的内容)
	 * @param str
	 * @param re
	 * @return
	 */
	public static List<String> getSubstrByRegexReturnList(String str,String re){
		List<String> list = new ArrayList<String>();
		Matcher matcher = Pattern.compile(re).matcher(str);
		while(matcher.find()){
			int groupCount = matcher.groupCount();//始终显示最里面组的内容
			list.add(matcher.group(groupCount));
		}
		return list;
	}
	
	/**
	 * 根据正则从一个字符串中获取第一个匹配项(显示最后一个组的内容)
	 * @param str
	 * @param re
	 * @return
	 */
	public static String getSubstrByRegex(String str,String re){
		List<String> list = getSubstrByRegexReturnList(str, re);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据正则从一个字符串中获取所有匹配项(显示所有组的内容)
	 * @param str
	 * @param re
	 * @return
	 */
	public static List<List<String>> getSubstrAllGroupByRegexReturnList(String str,String re){
		List<List<String>> list = new ArrayList<List<String>>();
		Matcher matcher = Pattern.compile(re).matcher(str);
		while(matcher.find()){
			List<String> list2 = new ArrayList<String>();
			for(int i=0;i<=matcher.groupCount();i++){
				list2.add(matcher.group(i));
			}
			list.add(list2);
		}
		return list;
	}
	
	/**
	 * 根据正则从一个字符串中获取一个匹配项(显示所有组的内容)
	 * @param str
	 * @param re
	 * @return
	 */
	public static List<String> getSubstrAllGroupByRegex(String str,String re){
		List<List<String>> list = getSubstrAllGroupByRegexReturnList(str, re);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
}

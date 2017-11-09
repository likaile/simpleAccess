package cn.lkl.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.JavaType;

public class JsonUtils {

	public static String defaultDatePattern = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 将对象转换为json(指定日期格式)
	 * @param obj
	 * @return
	 */
	public static String parseObject(Object obj) {
		return parseObject(obj,null);
	}
	
	/**
	 * 将对象转换为json(指定日期格式)
	 * 
	 * @param obj
	 * @param datePattern
	 * @return
	 */
	public static String parseObject(Object obj, String datePattern) {
		if (datePattern == null || datePattern.length() == 0) {
			datePattern = defaultDatePattern;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
			mapper.setDateFormat(new SimpleDateFormat(datePattern));
			// 如果指定对象中含有jsonFilter注解,则必须设置过滤器,否则报错
			addFilterByContainsAnn(obj, mapper);
			return mapper.writer().writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将对象转换为json并指定要转换的字段(指定时间格式)(需要和javabean上面的注解配合使用)
	 * 
	 * @param obj
	 * @param datePattern
	 * @param jsonFilterName
	 *            要过滤的javabean类型上方的jsonFilter注解的值
	 * @param parseFieldArr
	 *            要转换的字段列表
	 * @return
	 */
	public static String parseObjectSetField(Object obj, String datePattern,
			String jsonFilterName, String[] parseFieldArr) {
		if (datePattern == null || datePattern.length() == 0) {
			datePattern = defaultDatePattern;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat(datePattern));
			// 添加过滤器,如果碰到jsonFilterName对应的类型,则进行过滤,只输出指定字段
			SimpleFilterProvider filters = new SimpleFilterProvider();
			filters.addFilter(jsonFilterName,
					SimpleBeanPropertyFilter.filterOutAllExcept(parseFieldArr));
			mapper.setFilters(filters);
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将对象转换为json并指定要转换的字段(当需要多个过滤器时使用)
	 * 
	 * @param obj
	 * @param datePattern
	 * @param Map
	 *            <String,String[]> parseFieldArrMap 多个过滤器
	 * @return
	 */
	public static String parseObjectSetField(Object obj, String datePattern,
			Map<String, String[]> parseFieldArrMap) {
		if (datePattern == null || datePattern.length() == 0) {
			datePattern = defaultDatePattern;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat(datePattern));
			// 添加过滤器,如果碰到jsonFilterName对应的类型,则进行过滤,只输出指定字段
			if (parseFieldArrMap != null && parseFieldArrMap.size() > 0) {
				SimpleFilterProvider filters = new SimpleFilterProvider();
				for (String jsonFilterName : parseFieldArrMap.keySet()) {
					filters.addFilter(jsonFilterName, SimpleBeanPropertyFilter
							.filterOutAllExcept(parseFieldArrMap
									.get(jsonFilterName)));
				}
				mapper.setFilters(filters);
			}
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将对象转换为json并指定不转换的字段(指定时间格式)(需要和javabean上面的注解配合使用)
	 * 
	 * @param obj
	 * @param datePattern
	 * @param jsonFilterName
	 *            要过滤的javabean类型上方的jsonFilter注解的值
	 * @param parseFieldArr
	 *            不转换的字段列表
	 * @return
	 */
	public static String parseObjectSetNoField(Object obj, String datePattern,
			String jsonFilterName, String[] noParseFieldArr) {
		if (datePattern == null || datePattern.length() == 0) {
			datePattern = defaultDatePattern;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat(datePattern));
			// 添加过滤器,如果碰到jsonFilterName对应的类型,则进行过滤,排除指定字段
			SimpleFilterProvider filters = new SimpleFilterProvider();
			filters.addFilter(jsonFilterName, SimpleBeanPropertyFilter
					.serializeAllExcept(noParseFieldArr));
			mapper.setFilters(filters);
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 将json转换为对象
	 * 
	 * @param jsonStr
	 * @param clazz
	 * @return
	 */
	public static <T> T parseJson2Object(String jsonStr, Class<T> clazz) {
		return parseJson2Object(jsonStr, clazz, null);
	}

	/**
	 * 将json转换为对象(指定日期格式)
	 * 
	 * @param jsonStr
	 * @param clazz
	 * @param datePattern
	 * @return
	 */
	public static <T> T parseJson2Object(String jsonStr, Class<T> clazz,
			String datePattern) {
		if (datePattern == null || datePattern.length() == 0) {
			datePattern = defaultDatePattern;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat(datePattern));
			return mapper.readValue(jsonStr, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 将json转换为list
	 * 
	 * @param jsonStr
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parseJson2List(String jsonStr, Class<T> clazz) {
		return parseJson2List(jsonStr, clazz, null);
	}

	/**
	 * 将json转换为list(指定日期类型)
	 * 
	 * @param jsonStr
	 * @param clazz
	 * @param datePattern
	 * @return
	 */
	public static <T> List<T> parseJson2List(String jsonStr, Class<T> clazz,
			String datePattern) {
		if (datePattern == null || datePattern.length() == 0) {
			datePattern = defaultDatePattern;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat(datePattern));
			JavaType javaType = mapper.getTypeFactory()
					.constructParametricType(ArrayList.class, clazz);
			return mapper.readValue(jsonStr, javaType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 将json转换为map 有缺陷,不能返回Map<List<User>>类型
	 * 
	 * @param jsonStr
	 * @param clazz  map中值的类型(键默认为String)
	 * @param datePattern
	 * @return
	 */
	public static <T> Map<String, T> parseJson2Map(String jsonStr,Class<T> clazz) {
		return parseJson2Map(jsonStr, clazz, null);
	}

	/**
	 * 将json转换为map(指定日期类型) 有缺陷,不能返回Map<List<User>>类型
	 * 
	 * @param jsonStr
	 * @param clazz
	 *            map中值的类型(键默认为String)
	 * @param datePattern
	 * @return
	 */
	public static <T> Map<String, T> parseJson2Map(String jsonStr,
			Class<T> clazz, String datePattern) {
		if (datePattern == null || datePattern.length() == 0) {
			datePattern = defaultDatePattern;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat(datePattern));
			JavaType javaType = mapper
					.getTypeFactory()
					.constructParametricType(HashMap.class, String.class, clazz);
			return mapper.readValue(jsonStr, javaType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 如果存在jsonFilter注解,则添加过滤器
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static void addFilterByContainsAnn(Object obj, ObjectMapper mapper) {
		Class<?> clazz = null;
		if (obj instanceof List) {
			Iterator iterator = ((List) obj).iterator();
			while (iterator.hasNext()) {
				Object o = iterator.next();
				if (o != null) {
					clazz = o.getClass();
					break;
				}
			}
		} else if (obj instanceof Map) {
			Iterator iterator = ((Map) obj).keySet().iterator();
			while (iterator.hasNext()) {
				Object o = iterator.next();
				if (o != null) {
					clazz = o.getClass();
					break;
				}
			}
		} else {
			clazz = obj.getClass();
		}
		
		if(clazz==null){
			return;
		}

		// 有可能是代理对象,所以多判断几个父类
		for (int i = 0; i < 3; i++) {
			if (clazz.isAnnotationPresent(JsonFilter.class)) {
				// 添加过滤器(空的,没作用)
				JsonFilter ann = clazz.getAnnotation(JsonFilter.class);
				SimpleFilterProvider filters = new SimpleFilterProvider();
				filters.addFilter(ann.value(), SimpleBeanPropertyFilter
						.serializeAllExcept(new String[0]));
				mapper.setFilters(filters);
				return;
			}
			clazz = clazz.getSuperclass();
			if (clazz == null) {
				return;
			}
		}
	}
	
	/**
	 * 返回格式化的json字符串
	 * @param jsonStr
	 */
	public static String getFormatJsonStr(String jsonStr) {
		//碰到{,则{后换行,{下一行添加一个\t
		//碰到},则}前换行,}后换行,}前减少一个\t	(如果是},在,后面换行,如果是}]则{后不换行)
		//碰到[,则[后换行,[下一行添加一个\t
		//碰到],则]前换行,}后换行,]前减少一个\t	(如果是],在,后面换行,如果是]}则]后不换行)
		//碰到,,则,后换行
		Pattern p = Pattern.compile("(\\{|\\},?|\\[|\\],?|,)");
		Matcher m = p.matcher(jsonStr);
		StringBuffer sb = new StringBuffer();
		//替换换行
		String tStr = "";
		int index = 0;
		int lastIndex = 0;
		while (m.find()) {
			String subStr = m.group(1);
			index = m.start();
			//如果是字符串中的,则跳过(判断前面的双引号是否成对)
			if(StringUtils.getSubstrExistsCount(jsonStr.substring(lastIndex,index), "\"")%2==0){
				String newStr = "";
				if (subStr.equals("{") || subStr.equals("[")) {
					tStr += "\t";
					newStr = "$1\n"+tStr;
				} else if (subStr.startsWith("}") || subStr.startsWith("]")) {
					if(tStr.length()>=1){
						tStr = tStr.substring(1);
					}else{
						tStr = "";
					}
					newStr = "\n"+tStr+"$1\n"+tStr;
				} else if (subStr.equals(",")) {
					newStr = "$1\n"+tStr;
				}
				m.appendReplacement(sb, newStr);
				lastIndex = index;
			}else{
				m.appendReplacement(sb, subStr);
			}
		}
		m.appendTail(sb);
		jsonStr = sb.toString();
		jsonStr = jsonStr.replaceAll("\n+\t*\n+", "\n");
		return jsonStr;
	}

}

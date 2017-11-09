package cn.lkl.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


//表达式引擎(将字符串作为代码运行)
public class ExpressionEngineUtils {
	
	private static ScriptEngineManager scriptManager = new ScriptEngineManager();
	
	/**
	 * @描述：js引擎,可将字符串解析为js代码执行,此方法需要加锁,否则同时调用结果混乱
	 * 			可将java变量当做参数传入	
	 * 				当参数为java引用类型时,应使用java对象的属性和方法!!,如map: map.get(key) 而不是 map.key
	 * 				当参数为基本类型时,应使用js属性和函数,如string: "".length 而不是 "".length() ,不能str?判断,需要str==''
	 * 			遍历map和list示例
	 * 				var obj=columnMapMap.keySet().iterator();
					var s='';
					for(var key : obj){
						s+=key+',';
					}
					return s ;
	 * @开发人员：likaihao
	 * @开发时间：2015年9月6日 上午10:40:19
	 * @param expression
	 * @param paramMap
	 * @return
	 */
	public static Object jsEngine(String expression,Map<String,Object> paramMap){
		/**
		 * 特殊格式
		 * Number(age)*5 js表达式计算,可直接使用js方法
		 * person.car.name java对象属性访问 和 方法访问
		 * stringUtils.getSuffix(person.car.name) 自定义工具方法调用(stringUtils是传入的StringUtils类的对象参数的名称)
		 */
		//js解析器
		ScriptEngine scriptEngine = scriptManager.getEngineByName("javascript");
		try {
			//设置参数,可直接设置java变量供js使用
			if(paramMap!=null && paramMap.size()>0){
				for(String key : paramMap.keySet()){
					scriptEngine.put(key, paramMap.get(key));
				}
			}
			//执行表达式
			Object result = scriptEngine.eval(expression);
			return result;
		} catch (Exception e) {
			throw new RuntimeException("表达式错误:"+expression,e);
		}
	}
	
	/**
	 * @描述：js引擎装饰,添加方法签名,使用时可直接使用return返回值,简化map属性访问方式
	 * @开发人员：likaihao
	 * @开发时间：2015年9月6日 上午10:45:43
	 * @param expression
	 * @param paramMap
	 * @return
	 */
	public static Object jsEngineDecoration(String expression,Map<String,Object> paramMap){
		//如果存在return语句,且没有函数,则添加函数
		if(expression.contains("return ") && !expression.contains("function")){
			expression = "(function(){"+expression+"})();";
		}
		
		//将map.key修改为map.get(key)
		Set<String> set = new HashSet<String>();
		
		List<String> list = null;
//		//如果是@开头,表明是变量,要先替换
//		if(expression.contains("@(")){ //@(paramList.get(0))
//			list = TempletUtils.getAllTwinTagContent(expression, "@(");
//			if(list!=null && list.size()>0){
//				set = new HashSet<String>(list);
//				for(String str : set){
//					expression = expression.replace(str, jsEngine(str.substring(2,str.length()-1),paramMap).toString());
//				}
//			}
//		}
//		if(expression.contains("@")){
//			list = RegexUtils.getSubstrByRegexReturnList(expression, "(@\\w+)\\b");
//			if(list!=null && list.size()>0){
//				set = new HashSet<String>(list);
//				for(String str : set){
//					expression = expression.replace(str, jsEngine(str.substring(1),paramMap).toString());
//				}
//			}
//		}
		//多个.属性  且不带()的
		list = RegexUtils.getSubstrByRegexReturnList(expression, "\\b(\\w+?(?:\\.[\\w@]+)+)(?!\\()\\b");
		if(list!=null && list.size()>0){
			set = new HashSet<String>(list);
			for(String s : set){
				String[] arr = s.split("\\.");
//				
//				for(int i=0;i<arr.length;i++){
//					if(arr[i].startsWith("@")){
//						Object val = paramMap.get(arr[i].substring(1));
//						if(val==null){
//							throw new RuntimeException("替换map访问方式失败,"+s+",参数中不存在"+arr[i].substring(1));
//						}
//						arr[i] = paramMap.get(arr[i].substring(1)).toString();
//					}
//				}
				String newStr = getMapStr(arr,1,paramMap);
				expression = expression.replace(s, newStr);
			}
		}
		
		Object value = jsEngine(expression,paramMap);
		if(value!=null){
			//如果是double类型,且小数为0,则转为int类型
			if(value instanceof Double){
				if(value.toString().endsWith(".0")){
					value = Integer.parseInt(value.toString().substring(0,value.toString().length()-2));
				}
			}
			//如果是sun.org.mozilla.javascript.internal.NativeBoolean类型,则转换为boolean
			if(value.toString().startsWith("sun.org.mozilla.javascript.internal.NativeBoolean")){
				try {
					Field field = value.getClass().getDeclaredField("booleanValue");
					field.setAccessible(true);
					value = field.get(value);
				} catch (Exception e) {
				}
			}
		}
		return value;
	}
	
	/**
	 * @描述：将map.key转换为map.get(key)
	 * @开发人员：likaihao
	 * @开发时间：2015年9月9日 下午5:53:01
	 * @param arr columnMapMap.feeitem.javaType-->{columnMapMap,feeitem,javaType}
	 * @param index 当前arr的索引,从1开始
	 * @param paramMap 参数
	 * @return
	 */
	private static String getMapStr(String[] arr,int index,Map<String,Object> paramMap){
		//arr = columnMapMap.feeitem.javaType
		//index 1
		try {
			if(index==arr.length){
				return StringUtils.join(arr, ".");
			}
			//判断父级是否是map
			StringBuilder builder = new StringBuilder();
			for(int i=0;i<index;i++){
				builder.append(arr[i]+".");
			}
			builder.deleteCharAt(builder.length()-1);
			Object obj = null;
			try{
				obj = jsEngine(builder.toString(),paramMap);
			}catch(Exception e){
			}
			
			//是map不是json
			if(obj!=null && obj instanceof Map && !obj.getClass().getName().equals("sun.org.mozilla.javascript.internal.NativeObject")){
				arr[index] = "get(\""+arr[index]+"\")";
			}
			return getMapStr(arr,index+1,paramMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("替换map属性访问方式失败:"+Arrays.toString(arr)+"中的"+arr[index]);
		}
	}
	
}

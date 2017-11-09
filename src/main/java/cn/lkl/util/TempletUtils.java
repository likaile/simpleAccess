package cn.lkl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class TempletUtils {
	static final String format = "\\{:.+?\\}";//占位符格式
	//static final String format = "\\$\\{.*+\\}";//占位符格式
	
	/**
	 * 单行文本替换:
	 * 		将每一行作为一个数据源,对一个单行模板进行替换,每一行产生一行新数据(模板只有一行,批量替换)
	 * 		单行文本替换中可以使用{:_linenum}变量
	 * 多行文本替换:
	 * 		将全部行作为一个模板,用指定外部数据进行替换,产生多行数据(模板有多行,只替换一次)
	 * 		多行模板中原本的换行符会被忽略,使用字符\r\n控制换行
	 * 		多行文本中可以使用{:if}和{:for}语句
	 * 文本框中的换行会被忽略,用户输入的\r\n会被识别为换行
	 * 
	 * 表达式格式
	 * {:_linenum} 当前行号,只有单行模板替换中可以使用
	 * {:Number(age)*5} js表达式计算
	 * {:person.car.name} java对象属性访问 和 方法访问
	 * {:stringUtils.getSuffix(person.car.name)} 自定义工具方法调用(stringUtils是传入的StringUtils类的对象参数的名称)
	 * 
	 * map属性访问
	 * 		原本需写成map.get(key)的格式,现在经过特殊处理,可直接写为map.key或map.@key的格式
	 * 		map.aaa.bbb 会替换成 map.get("aaa").bbb,如果map.get("aaa")是map的话,还会继续替换为map.get("aaa").get("bbb")
	 * 
	 * 下面只在多行模板中生效
	 * 判断
	 *  {:if a!=5
		
		}
	 * 
	 * 循环
	 *  {:for columnMapMap as 'xxx'
			{:xxx.key} {:xxx.value}
		}
		xxx默认存在三个属性 key 和value , isLast
		如果是map,则分别为map的key和value
		如果是list或数组,则分别为索引和值
		isLast表示是否是最后一次循环
	 */
	
	/**
	 * @描述：获得占位符样式
	 * @开发人员：likaihao
	 * @开发时间：2015年9月18日 上午10:25:37
	 * @return
	 */
	public static String[] getTag(){
		String[] arr = format.split("\\.\\+\\?");
		for(int i=0;i<arr.length;i++){
			arr[i] = arr[i].replace("\\{", "{").replace("\\}", "}").replace("\\$","$");
		}
		return arr;
	}
	
	/**
	 * @描述：获得占位符的内容
	 * @开发人员：likaihao
	 * @开发时间：2015年9月18日 上午10:30:10
	 * @param str
	 * @return
	 */
	public static String getStrTagContent(String str){
		String[] tagArr = getTag();
		return str.substring(tagArr[0].length(),str.length()-tagArr[1].length());
	}
	
	/**
	 * @描述：获得字符串 添加占位符后的内容
	 * @开发人员：likaihao
	 * @开发时间：2015年9月18日 上午10:32:18
	 * @param str
	 * @return
	 */
	public static String getStrAddTag(String str){
		String[] tagArr = getTag();
		return tagArr[0]+str+tagArr[1];
	}
	
	/**
	 * @描述：模板提取(一行),从字符串中提取模板需要的值
	 * @开发人员：likaihao
	 * @开发时间：2015年8月19日 下午2:19:28
	 * @param str 字符串
	 * @param templet 模板
	 * @return 模板的name和value
	 */
	public static Map<String,Object> templetExtractOneLine(String str,String templet){
		//str: drivertoken	String	Y	token
		//templet: {:name}	{:type}	{:notnull}	{:disc}
		String templetPattern = "^"+templet.replaceAll(format, "(.+?)")+"$";//^(.+?) (.+?) (.+?) (.+?)$
		
		if(!str.matches(templetPattern)){
			throw new RuntimeException("字符串不符合模式,"+str+" , "+templetPattern);
		}
		
		//从模板中获取名称 name,type,notnull,disc
		List<String> nameList = RegexUtils.getSubstrByRegexReturnList(templet, format.replace(".+?", "(.+?)"));//\{:(.*?)\}
		
		//从内容中获取对应的值 drivertoken,String,Y,token
		List<String> valueList = RegexUtils.getSubstrAllGroupByRegex(str, templetPattern);//^(.+?) (.+?) (.+?) (.+?)$
		
		//键值对 name=drivertoken,type=String,notnull=Y,disc=token
		Map<String,Object> map = new HashMap<String,Object>();
		for(int i=0;i<nameList.size();i++){
			map.put(nameList.get(i), valueList.get(i+1));
		}
		return map;
	}
	
	/**
	 * @描述：模板提取(一行,批量),从每行字符串中提取模板需要的值
	 * @开发人员：likaihao
	 * @开发时间：2015年8月19日 下午2:19:28
	 * @param str 字符串
	 * @param templet 模板
	 * @return 模板的name和value
	 */
	public static List<Map<String,Object>> templetExtractOneLineBatch(List<String> strList,String templet){
		//str: drivertoken	String	Y	token
		//templet: {:name}	{:type}	{:notnull}	{:disc}
		String templetPattern = "^"+templet.replaceAll(format, "(.+?)")+"$";
		for(String str : strList){
			if(!str.matches(templetPattern)){
				throw new RuntimeException("字符串不符合模式,"+str+" , "+templetPattern);
			}
		}
		
		List<String> nameList = RegexUtils.getSubstrByRegexReturnList(templet, format.replace(".+?", "(.+?)"));
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(String str : strList){
			List<String> valueList = RegexUtils.getSubstrAllGroupByRegex(str, templetPattern);
			Map<String,Object> map = new HashMap<String,Object>();
			for(int i=0;i<nameList.size();i++){
				map.put(nameList.get(i), valueList.get(i+1));
			}
			map.put("stringUtils", new StringUtils());
			list.add(map);
		}
		return list;
	}
	
	/**
	 * @描述：模板填充(模板为一行),将模板中指定占位符替换为值,(模板占位符中可以填写任何符合js规则的表达式,可以使用js函数)
	 * @开发人员：likaihao
	 * @开发时间：2015年8月19日 下午2:01:33
	 * @param templet 模板
	 * @param nameValueMap 要替换的占位符和值
	 * @return 替换后的模板
	 */
	public static String templetFillOneLine(String templet,Map<String,Object> nameValueMap){
		//templet: private {type} {name};
		//nameValueMap: {name=drivertoken, notnull=Y, type=String, disc=token}
		
		//替换表达式
		List<String> nameList = RegexUtils.getSubstrByRegexReturnList(templet, format.replace(".+?", "(.+?)"));
		Set<String> set = new HashSet<String>(nameList);
		for(String name : set){
//			if(name.startsWith("set")){
//				//添加或修改参数
//				String content = name.substring("set ".length());
//				int index = content.indexOf("=");
//				String value = content.substring(index+1);
//				String key = content.substring(0,index);
//				Object result = ExpressionEngineUtils.jsEngineDecoration(value, nameValueMap);
//				nameValueMap.put(key, result);
//				templet = templet.replace(getStrAddTag(name), "");
//			}else{
				//表达式(可以是任何符合js规则的表达式,可使用js函数)
				Object result = ExpressionEngineUtils.jsEngineDecoration(name, nameValueMap);
				String sResult = "";
				if(result!=null){
					sResult = result.toString();
				}
				if(result instanceof Double && sResult.endsWith(".0")){
					sResult = sResult.substring(0,sResult.length()-2);
				}
				templet = templet.replace(getStrAddTag(name), sResult);
//			}
		}
		return templet;
	}
	
	/**
	 * @描述：模板填充(一行,批量),将模板中指定占位符替换为值,每行替换一次
	 * @开发人员：likaihao
	 * @开发时间：2015年8月19日 下午2:01:33
	 * @param templet 模板
	 * @param nameValueMap 要替换的占位符和值
	 * @return 替换后的模板
	 */
	public static List<String> templetFillOneLineBatch(String templet,List<Map<String,Object>> nameValueMapList){
		List<String> newStrList = new ArrayList<String>();
		int _linenum = 1;
		for(Map<String,Object> nameValueMap : nameValueMapList){
			nameValueMap.put("_linenum", _linenum);
			String newStr = templetFillOneLine(templet,nameValueMap);
			newStrList.add(newStr);
			_linenum ++;
		}
		return newStrList;
	}
	
	/**
	 * @描述：多行模板替换(将if,for语句替换,然后进行单行模板替换)
	 * @开发人员：likaihao
	 * @开发时间：2015年9月6日 下午4:16:25
	 * @param templet
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String templetFillMultiLine(String templet,Map<String,Object> paramMap){
		//页面传来的换行只是\n,这里替换为\r\n
		templet = templet.replaceAll("([^\r])\n", "$1\r\n");
		templet = templet.replaceAll("([\r\n])[ 　\t]+", "$1");
		//替换if,for语句
		String tagContent = null;
		while((tagContent=getFirstTagContent(templet))!=null){
			if(tagContent.startsWith("{:if")){
				//存在{:if
				String condition = RegexUtils.getSubstrByRegex(tagContent, "\\{:if (.+?)\r\n");
				Boolean b = (Boolean) ExpressionEngineUtils.jsEngineDecoration("("+condition+")==true", paramMap);
				String result = "";
				if(b){
					//条件成立,删除if语句,否则删除全部
					result = RegexUtils.getSubstrByRegex(tagContent, "\\{:if .+?\r\n([\\s\\S]*?)\r\n\\s*}$");
				}
				
				//替换
				templet = templet.replace(tagContent, result);
			}else{
				//存在{:for
				String re = "\\{:for (.+?) as '(.+?)' *\r\n";
				List<String> rList = RegexUtils.getSubstrAllGroupByRegex(tagContent, re);
				String collectionName = rList.get(1);
				Object collection = ExpressionEngineUtils.jsEngineDecoration(collectionName,paramMap);
				if(collection==null){
					throw new RuntimeException("指定集合在参数中不存在:"+collectionName);
				}
				String varName = rList.get(2);
				
				//替换新的内容
				String subStr = RegexUtils.getSubstrByRegex(tagContent, "^\\{:for .+?\r\n([\\s\\S]*?)\r\n\\s*}$");
				List<String> newSubLineList = new ArrayList<String>();
				//如果是list
				if(collection instanceof List){
					List<?> clist = (List<?>)collection;
					for(int i=0;i<clist.size();i++){
						String subStr2 = subStr;
						subStr2 = subStr2.replaceAll("\\b"+varName+"\\.key\\b", i+"");
						subStr2 = subStr2.replaceAll("\\b"+varName+"\\.value\\b", "("+collectionName+".get("+i+"))");
						subStr2 = subStr2.replaceAll("\\b"+varName+"\\.isLast\\b", (i==clist.size()-1)+"");
						newSubLineList.add(subStr2);
					}
				}
				//如果是map
				if(collection instanceof Map){
					Map<String,Object> cMap = (Map<String,Object>)collection;
					int i = 0;
					for(String key : cMap.keySet()){
						String subStr2 = subStr;
						subStr2 = subStr2.replaceAll("\\b"+varName+"\\.key\\b", "\""+key+"\"");
						subStr2 = subStr2.replaceAll("\\b"+varName+"\\.value\\b", collectionName+"."+key);
						subStr2 = subStr2.replaceAll("\\b"+varName+"\\.isLast\\b", (i==cMap.size()-1)+"");
						
						newSubLineList.add(subStr2);
						i++;
					}
				}
				
				//下面有问题,是set标签的执行问题,set标签应该在if标签前执行
				//如果是list
//				if(collection instanceof List){
//					List clist = (List)collection;
//					for(int i=0;i<clist.size();i++){
//						String subStr2 = "";
//						subStr2+="{:set _key="+i+"}";
//						subStr2+="{:set _value="+collectionName+".get("+i+")}";
//						subStr2+="{:set _isLast="+(i==clist.size()-1)+"}";
//						templetFillOneLine(subStr2, paramMap);//将_key,_value,_isLast的值存入paramMap中,{:if要用
//						newSubLineList.add(subStr2+subStr);
//					}
//				}
//				//如果是map
//				if(collection instanceof Map){
//					Map<String,Object> cMap = (Map<String,Object>)collection;
//					int i = 0;
//					for(String key : cMap.keySet()){
//						String subStr2 = "";
//						subStr2+="{:set _key=\""+key+"\"}";
//						subStr2+="{:set _value="+collectionName+"."+key+"}";
//						subStr2+="{:set _isLast="+(i==cMap.size()-1)+"}";
//						templetFillOneLine(subStr2, paramMap);//将_key,_value,_isLast的值存入paramMap中,{:if要用
//						newSubLineList.add(subStr2+subStr);
//						i++;
//					}
//				}
				//替换
				String newSubStr = StringUtils.join(newSubLineList, "");
				templet = templet.replace(tagContent, newSubStr);
			}
		}
		
		//单行模板替换
		templet = templet.replaceAll("[\r\n]+", "");
		String newStr = templetFillOneLine(templet, paramMap);
		//将用户输入的\r\n替换为换行
		newStr = newStr.replaceAll("\\\\r\\\\n", "\r\n");
		return newStr;
	}
	
	/**
	 * 获得所有指定的成对的标记内容
	 * @param str 源字符串
	 * @param tagStartStr 标记声明部分(<div class="map_main">)
	 * @return 标记内容
	 */
	public static List<String> getAllTwinTagContent(String str,String tagStartStr){
		//确定成对的标签
		String twinStart = "";//成对开始标签
		String twinEnd = "";//成对结束标签
		if(tagStartStr.contains("(")){
			twinStart = "(";
			twinEnd = ")";
		}
		if(tagStartStr.contains("[")){
			twinStart = "[";
			twinEnd = "]";
		}
		if(tagStartStr.contains("{")){
			twinStart = "{";
			twinEnd = "}";
		}
		
		List<String> list = new ArrayList<String>();
		for(;;){
			//查询开始标记
			int index = str.indexOf(tagStartStr);
			if(index==-1){
				break;
			}
			
			//去除第一次开始标签出现之前的字符串
			str = str.substring(index);
			StringBuilder builder = new StringBuilder(str);
			
			//查找结束标记
			int twinStartCount = 0;
			int twinEndCount = 0;
			index = -1;
			while( (index = builder.indexOf(twinEnd))!=-1){
				//判断 开始标签 和 结束标签 是否成对(出现的次数相等)
				String s = builder.substring(0,index+twinEnd.length());//两个结束标签中间的字符串
				twinStartCount += StringUtils.getSubstrExistsCount(s,twinStart);
				twinEndCount++;
				builder.delete(0, s.length());//删除已经查找过的字符串,剩下余下的字符串
				if(twinStartCount == twinEndCount){
					list.add(str.substring(0,str.length()-builder.length()));
					break;
				}
			}
			str = str.substring(str.length()-builder.length());//删除已经查找过的字符串,剩下余下的字符串
			if(index==-1){
				throw new RuntimeException("标记不是成对的:"+twinStart+","+twinEnd);
			}
		}
		return list;
	}
	
	/**
	 * 获得第一个指定的成对的标记的内容
	 * @param str 源字符串
	 * @param tagStartStr 标记声明部分(<div class="map_main">)
	 * @return 标记内容
	 */
	private static String getFirstTwinTagContent(String str,String tagStartStr){
		//确定成对的标签
		String twinStart = "";//成对开始标签
		String twinEnd = "";//成对结束标签
		if(tagStartStr.contains("(")){
			twinStart = "(";
			twinEnd = ")";
		}
		if(tagStartStr.contains("[")){
			twinStart = "[";
			twinEnd = "]";
		}
		if(tagStartStr.contains("{")){
			twinStart = "{";
			twinEnd = "}";
		}
		
		//查询开始标记
		int index = str.indexOf(tagStartStr);
		if(index==-1){
			return null;
		}
		
		//去除第一次开始标签出现之前的字符串
		str = str.substring(index);
		StringBuilder builder = new StringBuilder(str);
		
		//查找结束标记
		int twinStartCount = 0;
		int twinEndCount = 0;
		index = -1;
		while( (index = builder.indexOf(twinEnd))!=-1){
			//判断 开始标签 和 结束标签 是否成对(出现的次数相等)
			String s = builder.substring(0,index+twinEnd.length());//两个结束标签中间的字符串
			twinStartCount += StringUtils.getSubstrExistsCount(s,twinStart);
			twinEndCount++;
			builder.delete(0, s.length());//删除已经查找过的字符串,剩下余下的字符串
			if(twinStartCount == twinEndCount){
				return str.substring(0,str.length()-builder.length());
			}
		}
		throw new RuntimeException("标记不是成对的:"+twinStart+","+twinEnd);
	}
	
	
	//返回第一个{:if或{:for的内容
	private static String getFirstTagContent(String templet){
		int index1 = templet.indexOf("{:if");
		int index2 = templet.indexOf("{:for");
		if(index1==-1){
			if(index2 ==-1){
				return null;
			}else{
				return getFirstTwinTagContent(templet, "{:for");
			}
		}else{
			if(index2 ==-1){
				return getFirstTwinTagContent(templet, "{:if");
			}else{
				String str = index1 < index2 ? "{:if":"{:for";
				return getFirstTwinTagContent(templet, str);
			}
		}
	}
	
	
	
	/**
	 * @描述：计算表达式的值
	 * @开发人员：likaihao
	 * @开发时间：2015年9月22日 下午3:31:31
	 * @param expression
	 * @param paramMap
	 * @return
	 */
	public static Object computeEngine(String expression,Map<String,Object> paramMap){
		String tagContent = RegexUtils.getSubstrByRegex(expression, "(\\{:.*?\\})");
		if(tagContent!=null && tagContent.length()==expression.length()){
			//如果expression是一个占位符,则返回Object
			return ExpressionEngineUtils.jsEngineDecoration(getStrTagContent(expression), paramMap);
		}else{
			//如果expression不只是一个占位符,则返回String
			return templetFillOneLine(expression, paramMap);
		}
	}
	
	/**
	 * @描述：将页面传入的参数字符串变为参数map
	 * @开发人员：likaihao
	 * @开发时间：2015年9月11日 下午5:34:24
	 * @param paramStr
	 * @return
	 */
	public static Map<String,Object> getParamMap(String paramStr,Map<String,Object> nameValueMap){
		/**
		 *  str:abc //字符串
		 *  expression:{:5+2} //表达式
		 *  boolean:{:new Boolean(true)} //boolean
		 *  map:{:new Map(key1:value1,key2:value2)} //map
		 *  list:{:new List(1,2,3)} //list
		 *  map2:{:map} //引用上面声明过的变量
		 *  expression:{:pro
		 *  	var a=1;
		 *  	var b=2;
		 *  	return a+b;
		 *  } //利用js程序片段计算变量的值
		 *  str2:{:text
		 *  	你好,
		 *  	我是小明
		 *  } //多行文本
		 *  
		 *  发送http请求相关的规则
		 *  _sendParam:str,map //保留的参数,其他参数不会被发送
		 *  _noSendParam:map2 //移除的参数,此参数不会被发送
		 *  _body:{
		 *  	xxxx{:str}xxx
		 *  } //_body会认为没有key,作为请求体发送,其他参数全部不发送
		 *  _after:{:_value.substring(1)} //_after是对返回结果进行的操作,_value为原来的返回结果,_after将返回一个string为新的返回结果(此标签中只能写一句语句)
		 *  _codeBlock:{:xxx} //不会发送http请求,将执行指定的方法,将方法的结果返回(此标签中只能写一句语句)
		 *  开头是//的认为是注释, 末尾是 空格+//后的内容被认为是注释
		 *  
		 *  字符串处理相关的规则
		 *  _newValue:{:stringUtils.urlEncoding(_value)} //_value的值为原来的值,_newValue的值被作为返回的值
		 *  
		 *  代码块调用相关的规则
		 *  _codeBlock:{:xxx} //调用代码块,将返回结果输出到页面
		 *  
		 */
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(nameValueMap==null){
			nameValueMap = new HashMap<String,Object>();
		}
		//将自定义参数分割为map
    	if(paramStr!=null && paramStr.length()>0){
    		String[] tagArr = TempletUtils.getTag();
    		//获取所有的 代码片段,多行文本,_body(将每行开头添加\t)
    		List<String> textList = new ArrayList<String>();
    		textList.addAll(TempletUtils.getAllTwinTagContent(paramStr, tagArr[0]+"pro"));
    		textList.addAll(TempletUtils.getAllTwinTagContent(paramStr, tagArr[0]+"text"));
    		textList.addAll(TempletUtils.getAllTwinTagContent(paramStr, "_body:{"));
    		if(textList!=null && textList.size()>0){
    			for(String textStr : textList){
    				paramStr = paramStr.replace(textStr, textStr.replace("\n", "\n\t"));
    			}
    		}
    		//分割参数
			String[] arr = paramStr.split("\n(?!\t)");
			String _afterStr = null;
			for(String str : arr){
				if(str.startsWith("//")){//说明是注释
					continue;
				}
				int index = str.indexOf(":");
				if(index!=-1){
					String name = str.substring(0,index).trim();
					String value = str.substring(index+1);
					index = value.indexOf(" //");//去掉后面的注释 
					if(index!=-1){
						value = value.substring(0,index);
					}
					index = value.indexOf(" //");//去掉后面的注释(空格不一样..)
					if(index!=-1){
						value = value.substring(0,index);
					}
					index = value.indexOf("　//");//去掉后面的注释(空格不一样..)
					if(index!=-1){
						value = value.substring(0,index);
					}
					value = value.trim().replace(" ", "");
					//替换模板
					Object value2 = value;
					if(name.equals("_body")){
						//请求体
						value = value.substring(1,value.length()-1).trim();
						value2 = TempletUtils.computeEngine(value,nameValueMap);
						paramMap.clear();
						paramMap.put("", value2);
						break;
					}else if(name.equals("_after")){
						_afterStr = value;
					}else{
						//键值对
						if(value.contains(tagArr[0])){
							if(value.startsWith("{:new Map(")){//创建map
								Map<String,Object> map = new HashMap<String,Object>();
								String val = RegexUtils.getSubstrByRegex(value, "new Map\\((.+?)\\)");
								String[] arr2 = val.split(",");
								for(String str2 : arr2){
									String[] arr3 = str2.split(":");
									if(arr3[0].startsWith("\"")){
										arr3[0] = arr3[0].substring(1,arr3[0].length()-1);
									}else{
										throw new RuntimeException("map的key暂时只支持字符串");
									}
									if(arr3[1].startsWith("\"")){
										map.put(arr3[0], arr3[1]);
									}else{
										Object o = ExpressionEngineUtils.jsEngineDecoration(arr3[1], nameValueMap);
										map.put(arr3[0], o);
									}
								}
								value2 = map;
							}else if(value.startsWith("{:new List(")){//创建list
								List<Object> objList = new ArrayList<Object>();
								String val = RegexUtils.getSubstrByRegex(value, "new List\\((.+?)\\)");
								String[] arr2 = val.split(",");
								for(String str2 : arr2){
									Object val2 = ExpressionEngineUtils.jsEngineDecoration(str2, nameValueMap);
									objList.add(val2);
								}
								value2 = objList;
							}else if(value.startsWith("{:new Boolean(")){//创建boolean
								String val = RegexUtils.getSubstrByRegex(value, "new Boolean\\((.+?)\\)");
								value2 = ExpressionEngineUtils.jsEngineDecoration(val, nameValueMap);
								value2 = new Boolean(value2.toString());
							}else if(value.startsWith("{:pro\r\n")){//程序片段
								String val = value.substring("{:pro".length(),value.length()-1);
								val = val.replaceAll("//.*?([\r\n]+)", "$1");//去掉注释
				    			value2 = ExpressionEngineUtils.jsEngineDecoration(val, nameValueMap);
							}else if(value.startsWith("{:text\r\n")){//多行文本
								String val = value.substring("{:text".length(),value.length()-1);
								val = val.replaceAll("//.*?([\r\n]+)", "$1");//去掉注释
				    			value2 = val.replaceAll("\t", "").trim();
							}else{//表达式
								value2 = TempletUtils.computeEngine(value, nameValueMap);
							}
			    		}
					}
					
					paramMap.put(name, value2);
					nameValueMap.put(name, value2);
				}
			}
			
			//判断sendParam参数
			if(paramMap.containsKey("_sendParam")){
				String sendParamStr = paramMap.get("_sendParam").toString();
				String[] paramArr = sendParamStr.split(",");
				Map<String,Object> paramMap2 = new HashMap<String,Object>();
				for(String param : paramArr){
					paramMap2.put(param, paramMap.get(param));
				}
				if(paramMap.containsKey("_noSendParam")){
					paramMap2.put("_noSendParam", paramMap.get("_noSendParam"));
				}
				paramMap = paramMap2;
			}
			//判断noSendParam参数
			if(paramMap.containsKey("_noSendParam")){
				String noSendParamStr = paramMap.get("_noSendParam").toString();
				String[] paramArr = noSendParamStr.split(",");
				for(String param : paramArr){
					paramMap.remove(param);
				}
				paramMap.remove("_noSendParam");
			}
			//添加_after
			if(_afterStr!=null){
				paramMap.put("_after", _afterStr);
			}
    	}
    	return paramMap;
	}
	
	/**
	 * @描述：将页面传入的参数字符串变为参数map,返回值为Map<String,String>
	 * @开发人员：likaihao
	 * @开发时间：2015年9月11日 下午5:34:24
	 * @param paramStr
	 * @return
	 */
	public static Map<String,String> getParamStrMap(String paramStr,Map<String,Object> nameValueMap){
		Map<String,Object> map = getParamMap(paramStr,nameValueMap);
		Map<String,String> map2 = new HashMap<String,String>();
		for(String key : map.keySet()){
			if(map.get(key)!=null){
				map2.put(key, map.get(key).toString());
			}
		} 
		return map2;
	}
}

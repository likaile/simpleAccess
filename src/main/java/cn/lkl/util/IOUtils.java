package cn.lkl.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class IOUtils {
	
	/**
	 * 获得桌面路径
	 * @return
	 */
	public static String getHomeDirectoryPath(){
		return javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
	}
	
	/**
	 * @描述：获得文件或url的后缀
	 * @开发人员：likaihao
	 * @开发时间：2015年12月17日 上午10:09:07
	 * @param str
	 * @return
	 */
	public static String getSuffix(String str){
		str = getFileName(str);
		int index = str.lastIndexOf(".");
		if(index==-1){
			return "";
		}
		str = str.substring(index+1);
		return str;
	}
	
	/**
	 * @描述：获得文件或url的名称
	 * @开发人员：likaihao
	 * @开发时间：2015年12月17日 上午10:09:07
	 * @param str
	 * @return
	 */
	public static String getFileName(String str){
		int index1 = str.lastIndexOf("/");
		int index2 = str.lastIndexOf("\\");
		
		int index = Math.max(index1, index2);
		if(index!=-1){
			str = str.substring(index+1);
		}
		//去掉问号后的内容
		index = str.lastIndexOf("?");
		if(index!=-1){
			str = str.substring(0,index);
		}
		return StringUtils.urlDecoding(str);
	}
	
	/**
	 * @描述：将inputStream转换为字节数组
	 * @开发人员：likaihao
	 * @开发时间：2015年12月17日 上午10:05:30
	 * @param in
	 * @return
	 */
	public static byte[] inputStreamToByteArray(InputStream in){
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			byte[] b = new byte[102400];
			int len = -1;
			while( (len = in.read(b))!=-1){
				byteOut.write(b,0,len);
				System.out.print(new String(b,0,len));
			}
			byte[] data = byteOut.toByteArray();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * @描述：将inputStream转换为String
	 * @开发人员：likaihao
	 * @开发时间：2015年12月17日 上午10:05:30
	 * @param in
	 * @return
	 */
	public static String inputStreamToString(InputStream in){
		try {
			byte[] data = inputStreamToByteArray(in);
			return new String(data,"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	/**
	 * 读取文件,返回List
	 * @param path
	 * @param encoding
	 * @return
	 */
/*	public static List<String> readFileReturnList(String path,String encoding){
		File file = new File(path);
		return readFileReturnList(file, encoding);
	}*/
	
	/**
	 * 读取文件,返回List
	 * @param path
	 * @return
	 *//*
	public static List<String> readFileReturnList(String path){
		return readFileReturnList(path,"utf-8");
	}*/
	
	/**
	 * 读取文件
	 * @param path
	 * @return
	 *//*
	public static String readFile(String path,String encoding){
		List<String> list = readFileReturnList(path,encoding);
		return StringUtils.join(list, "\r\n");
	}*/
	
	/**
	 * 读取文件
	 * @param path
	 * @return
	 */
	/*public static String readFile(String path){
		return readFile(path,"utf-8");
	}*/
	
	
	
	/**
	 * 读取控制台一行输入
	 * @return
	 */
	public static String readSystemIn(){
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			return in.readLine();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 读取控制台多行输入,返回List
	 * @param endStr 结束字符串
	 * @return
	 */
	public static List<String> readSystemInReturnList(String endStr){
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			List<String> list = new ArrayList<String>();
			String str = null;
			while(!(str=in.readLine()).equals(endStr)){
				list.add(str);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 写入文件
	 * @param path 文件路径
	 * @param content 要写入的内容
	 * @param encoding 文件编码
	 * @param isAppend 是否追加
	 * @param isReplace 是否替换
	 */
	public static void writeFile(String path,String content,String encoding,boolean isAppend,boolean isReplace){
		try {
			File file = new File(path);
			if(file.isDirectory()){
				System.out.println("指定路径是文件夹,无法写入,已跳过:"+file.getAbsolutePath());
				return;
			}
			
			file.getParentFile().mkdirs();
			
			if(!isAppend){
				if(file.exists()){
					if(!isReplace){
						System.out.println("文件已存在,跳过:"+file.getAbsolutePath());
						return;
					}
				}else{
					file.createNewFile();
				}
			}
			synchronized(file.getAbsoluteFile()){
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file,isAppend),encoding);
				out.write(content);
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 写入文件
	 * @param path 文件路径
	 * @param content 要写入的内容
	 * @param isAppend 是否追加
	 */
	public static void writeFile(String path,String content,boolean isAppend){
		writeFile(path, content, "utf-8", isAppend, false);
	}
	
	/**
	 * 替换文件
	 * @param path 文件路径
	 * @param content 要写入的内容
	 */
	public static void writeFileReplace(String path,String content){
		writeFile(path, content, "utf-8", false, true);
	}
	
	/**
	 * 替换文件
	 * @param path 文件路径
	 * @param content 要写入的内容
	 */
	public static void writeFileReplace(String path,String content,String encoding){
		writeFile(path, content, encoding, false, true);
	}
	
	/**
	 * 保存一个文件
	 * @param path 保存路径
	 * @param in 流
	 */
	public static void saveFile(String path,InputStream in){
		try {
			File file = new File(path);
			if(file.exists()){
				System.err.println("文件已经存在:"+file.getAbsolutePath());
				return;
			}
			file.getParentFile().mkdirs();
			if(!file.getParentFile().exists()){
				System.err.println("父文件夹不存在,跳过:"+file.getAbsolutePath());
				return;
			}
			OutputStream out = new FileOutputStream(file);
			byte[] b = new byte[102400];
			int len = -1;
			while( (len = in.read(b))>0){
				out.write(b,0,len);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(path);
		}
	}
	
	/**
	 * 保存一个文件
	 * @param path 保存路径
	 * @param in 流
	 */
	public static void saveFile(String path,byte[] bytes){
		saveFile(path,new ByteArrayInputStream(bytes));
	}
	
	/**
	 * @描述：获得文件夹下的所有文件(遍历)
	 * @开发人员：likaihao
	 * @开发时间：2016年4月26日 下午5:54:42
	 * @param path
	 */
	public static List<File> getAllFileList(String path){
		File file = new File(path);
		List<File> fileList = new ArrayList<File>();
		getAllFileList(file,fileList,false);
		return fileList;
	}
	
	/**
	 * @描述：获得文件夹下的所有文件(遍历),包含文件夹
	 * @开发人员：likaihao
	 * @开发时间：2016年4月26日 下午5:54:42
	 * @param path
	 */
	public static List<File> getAllFileListContainsDir(String path){
		File file = new File(path);
		List<File> fileList = new ArrayList<File>();
		getAllFileList(file,fileList,true);
		return fileList;
	}
	
	//遍历获得文件列表
	private static void getAllFileList(File file,List<File> fileList,boolean addDirFile){
		if(file.isDirectory()){
			for(File f : file.listFiles()){
				getAllFileList(f,fileList,addDirFile);
			}
			if(addDirFile){
				fileList.add(file);
			}
		}else{
			fileList.add(file);
		}
	}
		
	/**
	 * 获得一个文件夹下符合要求的文件列表
	 * @param path 搜索的根路径
	 * @param isDepth 是否深度搜索(递归搜索子文件夹)
	 * @param firstDirNameList 第一层目录名称
	 * @param houZhuiList 文件后缀
	 * @param noHouZhuiArr 不允许的文件后缀
	 */
	public static List<File> getFileList(String path,boolean isDepth,String[] firstDirNameArr,String[] houZhuiArr,String[] noHouZhuiArr){
		File file = new File(path);
		if(!file.exists()){
			throw new RuntimeException("文件或文件夹不存在:"+file.getAbsolutePath());
		}
		//如果是一个文件就直接返回
		if(!file.isDirectory()){
			List<File> list =new ArrayList<File>();
			list.add(file);
			return list;
		}
		List<String> houZhuiList = null;
		if(houZhuiArr!=null){
			houZhuiList = new ArrayList<String>();
			for(String houZhui : houZhuiArr){
				if(houZhui.startsWith(".")){
					houZhui = houZhui.substring(1);
				}
				houZhuiList.add(houZhui.toLowerCase());
			}
		}
		List<String> noHouZhuiList = null;
		if(noHouZhuiArr!=null){
			noHouZhuiList = new ArrayList<String>();
			for(String noHouZhui : noHouZhuiArr){
				if(noHouZhui.startsWith(".")){
					noHouZhui = noHouZhui.substring(1);
				}
				noHouZhuiList.add(noHouZhui.toLowerCase());
			}
		}
		List<File> fileList = new ArrayList<File>();
		//只搜索当前文件夹
		if(!isDepth){
			for(File f : file.listFiles()){
				if(f.isFile()){
					String houZhui = getSuffix(f.getName()).toLowerCase();
					if(houZhuiList==null || houZhuiList.contains(houZhui)){
						if(noHouZhuiList==null || !noHouZhuiList.contains(houZhui)){
							fileList.add(f);
						}
					}
				}
			}
		}else{
			//递归查找
			List<String> firstDirNameList = null;
			if(firstDirNameArr!=null){
				firstDirNameList = Arrays.asList(firstDirNameArr);
			}
			getFileList2(file,fileList,firstDirNameList,houZhuiList,noHouZhuiList);
		}
		return fileList;
	}
	
	//私有方法,遍历获得文件列表
	private static List<File> getFileList2(File file,List<File> fileList,List<String> firstDirNameList,List<String> houZhuiList,List<String> noHouZhuiList){
		if(file.isDirectory()){
			for(File f : file.listFiles()){
				if(firstDirNameList==null || firstDirNameList.contains(f.getName())){
					getFileList2(f,fileList,null,houZhuiList,noHouZhuiList);
				}
			}
		}else{
			String houZhui = getSuffix(file.getName());
			if(houZhuiList==null || houZhuiList.contains(houZhui)){
				if(noHouZhuiList==null || !noHouZhuiList.contains(houZhui)){
					fileList.add(file);
				}
			}
		}
		return fileList;
	}
	
	/**
	 * @描述：获得一个文件夹下符合要求的文件列表
	 * @开发人员：likaihao
	 * @开发时间：2016年5月5日 下午4:38:15
	 * @param path 搜索路径
	 * @param pathPatternArr 路径正则数组
	 * @param noPathPatternArr 不允许路径的正则数组
	 * @param fileNamePatternArr 文件名称正则数组
	 * @param noFileNamePatternArr 不允许文件名称的正则数组
	 * @return
	 */
	public static List<File> getFileListByPattern(String path,String[] pathPatternArr,String[] noPathPatternArr,String[] fileNamePatternArr,String[] noFileNamePatternArr){
		File file = new File(path);
		if(!file.exists()){
			throw new RuntimeException("文件或文件夹不存在:"+file.getAbsolutePath());
		}
		//如果是一个文件就直接返回
		if(!file.isDirectory()){
			List<File> list =new ArrayList<File>();
			list.add(file);
			return list;
		}
		
		
		//按路径正则获得文件
		List<File> fileList = new ArrayList<File>();
		getFileListByPattern2(file,pathPatternArr,noPathPatternArr,fileList,path,-1);
		
		//筛选
		Pattern pathPtn = null;
		Pattern noPathPtn = null;
		Pattern fileNamePtn = null;
		Pattern noFileNamePtn = null;
		if(pathPatternArr!=null && pathPatternArr.length>0){
			String pathPattern = "^(" + StringUtils.join(pathPatternArr, ")|(") + ")$";
			pathPtn = Pattern.compile(pathPattern.toLowerCase());
		}
		if(noPathPatternArr!=null && noPathPatternArr.length>0){
			String noPathPattern = "^(" + StringUtils.join(noPathPatternArr, ")|(") + ")$";
			noPathPtn = Pattern.compile(noPathPattern.toLowerCase());
		}
		if(fileNamePatternArr!=null && fileNamePatternArr.length>0){
			String fileNamePattern = "^(" + StringUtils.join(fileNamePatternArr, ")|(") + ")$";
			fileNamePtn = Pattern.compile(fileNamePattern.toLowerCase());
		}
		if(noFileNamePatternArr!=null && noFileNamePatternArr.length>0){
			String noFileNamePattern = "^(" + StringUtils.join(noFileNamePatternArr, ")|(") + ")$";
			noFileNamePtn = Pattern.compile(noFileNamePattern.toLowerCase());
		}
		
		List<File> fileList2 = new ArrayList<File>();
		for(File f : fileList){
			String fPath = f.getAbsolutePath().substring(file.getAbsolutePath().length()).replace("\\", "/").toLowerCase();
			if(pathPtn!=null && !pathPtn.matcher(fPath).matches()){
				continue;
			}
			if(noPathPtn!=null && noPathPtn.matcher(fPath).matches()){
				continue;
			}
			String fName = f.getName().toLowerCase();
			if(fileNamePtn!=null && !fileNamePtn.matcher(fName).matches()){
				continue;
			}
			if(noFileNamePtn!=null && noFileNamePtn.matcher(fName).matches()){
				continue;
			}
			fileList2.add(f);
		}
		return fileList2;
	}
	
	//遍历获得文件列表
  	private static void getFileListByPattern2(File file,String[] pathPatternArr,String[] noPathPatternArr,List<File> fileList,String basePath,int index){
  		if(file.isDirectory()){
  			//提前将不符合条件的文件夹筛选出去,不遍历文件夹
  			if(index!=-1){
  				//验证不符合的路径模式(如果模糊符合就不检测)
	  			if(noPathPatternArr!=null && noPathPatternArr.length>0){
	  				String subPath = file.getAbsolutePath().substring(basePath.length());
	  				for(int i=0;i<noPathPatternArr.length;i++){
	  					if(subPath.matches(noPathPatternArr[i])){
	  						return;
	  					}
	  				}
	  			}
  				//验证符合的路径模式(如果完全不符合才不检测)
	  			if(pathPatternArr!=null && pathPatternArr.length>0){
	  				String subPath = file.getAbsolutePath().substring(basePath.length()).replace("\\", "/");
	  	  			for(int i=0;i<pathPatternArr.length;i++){
	  	  				String pathPattern = pathPatternArr[i];
	  	  				if(pathPattern.startsWith("/")){
	  	  					pathPattern = pathPattern.substring(1);
	  	  				}
	  	  				String[] subPathPatternArr = pathPattern.replace("[^/]", "@@@").split("/",0);
	  	  				
		  	  			String re = "";
	  	  				for(int j=0;j<=index;j++){
	  	  					re += "/"+subPathPatternArr[j].replace("@@@", "[^/]");
	  	  				}
	  	  				
	  	  				//符合条件
	  	  				if(subPath.matches(re)){
	  	  					//如果存在.*,则不验证后续的文件夹
		  	  				if(subPathPatternArr[index].contains(".*")){
			  	  				pathPatternArr = null;
			  	  			}
	  	  					break;
	  	  				}
		  	  			if(i==pathPatternArr.length-1){
			  	  			//不符合条件,不遍历此文件夹
			  	  			return;
		  	  			}
	  	  			}
	  	  		}
  			}
  			for(File f : file.listFiles()){
  				getFileListByPattern2(f,pathPatternArr,noPathPatternArr,fileList,basePath,index+1);
  			}
  		}else{
  			fileList.add(file);
  		}
  	}
	
	
	 /**
     * @描述：获得符合条件的文件列表
     * @开发人员：likaihao
     * @开发时间：2015年12月18日 上午11:50:11
     * @param path
     * @param firstDirName
     * @param fileNamePattern
     * @param noFileNamePattern
     * @return
     */
    public static List<File> getFileListByMyPattern(String path,String pathPattern,String noPathPattern,String fileNamePattern,String noFileNamePattern){
    	/**
    	 * 规则:
    	 * 	?	代表一个
    	 *  *	代表多个
    	 *  $.	代表不包含.的多个
    	 *  $/	带表不包含/的多个
    	 */
    	
    	
    	String[] pathPatternArr = null;
    	String[] noPathPatternArr = null;
    	String[] fileNamePatternArr = null;
    	String[] noFileNamePatternArr = null;
    	
    	if(pathPattern!=null && pathPattern.length()>0){
    		pathPattern = patternConvert(pathPattern);
    		pathPatternArr = pathPattern.split(",");
    	}
    	if(noPathPattern!=null && noPathPattern.length()>0){
    		noPathPattern = patternConvert(noPathPattern);
    		noPathPatternArr = noPathPattern.split(",");
    	}
    	if(fileNamePattern!=null && fileNamePattern.length()>0){
    		fileNamePattern = patternConvert(fileNamePattern);
    		fileNamePatternArr = fileNamePattern.split(",");
    	}
    	if(noFileNamePattern!=null && noFileNamePattern.length()>0){
    		noFileNamePattern = patternConvert(noFileNamePattern);
    		noFileNamePatternArr = noFileNamePattern.split(",");
    	}
    	
    	//获得文件
    	List<File> fileList = IOUtils.getFileListByPattern(path, pathPatternArr, noPathPatternArr, fileNamePatternArr, noFileNamePatternArr);
//    	for(File f : fileList){
//			System.out.println(f.getAbsolutePath());
//		}
    	return fileList;
    }
    
    
    
	//模式换行
    private static String patternConvert(String pattern){
    	//将.转换为\. 将?转换为. 将*转换为.* 将$.替换为[^.]* 将$/替换为[^/]*
    	if(pattern!=null && pattern.length()>0){
    		pattern = pattern.replace(".", "\\.").replace("?", ".").replace("*", ".*").replace("$\\.", "[^.]*").replace("$/", "[^/]*");
    		//pattern = "^("+pattern.replace(",", ")|(")+")$";
    	}
    	return pattern;
    }
    
	/**
	 * 复制文件
	 * @param oldPath
	 * @param newPath
	 */
	public static void copyFile(String oldPath,String newPath){
		File oldFile = new File(oldPath);
		if(!oldFile.exists()){
			throw new RuntimeException("文件不存在:"+oldPath);
		}
		File newFile = new File(newPath);
		if(newFile.exists()){
			if(!newFile.delete()){
				throw new RuntimeException("文件已存在,且删除失败:"+newPath);
			}
		}else if(!newFile.getParentFile().exists()){
			newFile.getParentFile().mkdirs();
		}
		
		copyFile2(oldFile,newPath);
	}
	
	//复制文件 私有递归方法
	private static void copyFile2(File oldFile,String newPath){
		try {
			int basePathLen = oldFile.getAbsolutePath().length();
			if(oldFile.isDirectory()){
				File newFile = new File(newPath);
				if(!newFile.exists()){
					newFile.mkdirs();
				}
				File[] fileArr = oldFile.listFiles();
				for(File file : fileArr){
					if(file.isDirectory()){
						copyFile2(file,newPath+"/"+file.getAbsolutePath().substring(basePathLen+1));
					}else{
						InputStream in = new FileInputStream(file);
						OutputStream out = new FileOutputStream(new File(newPath,file.getName()));
						byte[] bytes = new byte[1024*200];
						int len = -1;
						while( (len=in.read(bytes))>0){
							out.write(bytes, 0, len);
						}
						in.close();
						out.close();
					}
				}
			}else{
				InputStream in = new FileInputStream(oldFile);
				File newFile = new File(newPath);
				if(newFile.isDirectory()){
					newFile = new File(newPath,oldFile.getName());
				}
				OutputStream out = new FileOutputStream(newFile);
				byte[] bytes = new byte[1024*200];
				int len = -1;
				while( (len=in.read(bytes))>0){
					out.write(bytes, 0, len);
				}
				in.close();
				out.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @描述：删除某个文件或文件夹
	 * @开发人员：likaihao
	 * @开发时间：2016年3月23日 下午5:17:05
	 * @param path
	 */
	public static void deleteFileOrDir(String path){
		File file = new File(path);
		if(file.isDirectory()){
			for(File f : file.listFiles()){
				//递归
				deleteFileOrDir(f.getAbsolutePath());
			}
			file.delete();
		}else if(file.isFile()){
			file.delete();
		}
	}
	
	/**
	 * 获取文件编码格式(利用第三方开源包cpdetector)
	 * 
	 * @param filePath
	 * @return
	 */
	public synchronized static String getFileEncode(File file) {
		/**
		 * <pre>
		 * 1、cpDetector内置了一些常用的探测实现类,这些探测实现类的实例可以通过add方法加进来,如:ParsingDetector、 JChardetFacade、ASCIIDetector、UnicodeDetector. 
		 * 2、detector按照“谁最先返回非空的探测结果,就以该结果为准”的原则. 
		 * 3、cpDetector是基于统计学原理的,不保证完全正确.
		 * </pre>
		 */
		
		Charset charset = null;
		PrintStream out = System.out;
		try {
			System.setOut(new PrintStream(new ByteArrayOutputStream()));
		} catch (Exception e) {
			System.out.println(e);
			return "UTF-8";
		}finally{
			System.setOut(out);
		}
		
		String charsetName = "GBK";
		if (charset != null) {
			if (charset.name().equals("US-ASCII")) {
				charsetName = "ISO_8859_1";
			} else if (charset.name().startsWith("UTF")) {
				charsetName = charset.name();// 例如:UTF-8,UTF-16BE.
			}
		}
		return charsetName;
	}
	
	public static void main(String[] args) {
		String path = "F:\\svn\\bj_1306_webtkt";
		String pathPattern = "/02PM_工程过程/.*,/05RD_参考资料/.*";
		String noPathPattern = ".*\\.git.*,.*\\.svn.*";
		
		String[] pathPatternArr = pathPattern.split(",");
    	String[] noPathPatternArr = noPathPattern.split(",");
		getFileListByPattern(path,pathPatternArr,noPathPatternArr,null,null);
    	
	}
	
}

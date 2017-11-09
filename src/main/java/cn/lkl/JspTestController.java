package cn.lkl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;



@Controller
public class JspTestController {
	
	@RequestMapping("/index")
    public String index(){
		Map<String,Object> map = new HashMap<String, Object>();
        map.put("name", "HelloController");
        return "index";
    }
	@RequestMapping("/access")
    public String access(Map<String,Object> map){
        map.put("name", "HelloController");
        return "liuchong/index";
    }
	@RequestMapping("/liuchong")
    public String liuchong(Map<String,Object> map){
        map.put("name", "HelloController");
        return "liuchong/login";
    }
	//跳转到上传文件的页面
    @RequestMapping(value="/uploadimg", method = RequestMethod.GET)
    public String goUploadImg() {
        //跳转到 templates 目录下的 uploadimg.html
        return "index";
    }
    @RequestMapping(value="/test",method=RequestMethod.GET)  
    public String downLoadFile(HttpServletResponse res,
    		@RequestParam(value="fileKey", required=true) String fileKey,
    		 @RequestParam(value="noticeId", required=true) Long noticeId,
    		@RequestParam(value="companyId", required=true) Long companyId) throws Exception{  
		return fileKey;
    }  
    //处理文件上传
    @RequestMapping(value="/testuploadimg", method = RequestMethod.POST)
    public @ResponseBody String uploadImg(@RequestParam("file") MultipartFile file,
            HttpServletRequest request) throws Exception {
    	
    	/*System.out.println("1111111111111");
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String filePath = request.getSession().getServletContext().getRealPath("imgupload/");
        try {
            uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
        }*/
        //返回json
        return bytesToHexString(file.getBytes());
    }
  //处理文件上传
    @RequestMapping(value="/testuploadimg1", method = RequestMethod.POST)
    @ResponseBody 
    public String uploadImg1(
            HttpServletRequest request) throws Exception {
    	List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("file"); 
    	StringBuffer str = new StringBuffer();
        MultipartFile file = null;
        for (int i =0; i< files.size(); ++i) { 
            file = files.get(i); 
            if (!file.isEmpty()) {
            	String name = file.getOriginalFilename();
            	System.out.println(name.substring(name.lastIndexOf(".") + 1)+" 文件流请求头："+bytesToHexString(file.getBytes())+"   "+name);
            	str.append(name.substring(name.lastIndexOf(".") + 1)+" 文件流请求头："+bytesToHexString(file.getBytes())+"   "+name+"          ");
            } else { 
                return "You failed to upload " + i + " becausethe file was empty."; 
            } 
        } 
        return str.toString(); 
    	/*System.out.println("1111111111111");
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String filePath = request.getSession().getServletContext().getRealPath("imgupload/");
        try {
            uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
        }*/
        //返回json
    }
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception { 
        File targetFile = new File(filePath);  
        if(!targetFile.exists()){    
            targetFile.mkdirs();    
        }       
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
    
    
    public  String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < 4; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
	
}

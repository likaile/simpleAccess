package cn.lkl.Controller;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.lkl.service.ImageService;
import cn.lkl.vo.JianDanImage;
import cn.lkl.vo.ImageResponseJson;

@SuppressWarnings("deprecation")
@RestController
@RequestMapping("/image")
public class ImageController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static DefaultHttpClient httpclient = new DefaultHttpClient(new PoolingClientConnectionManager());
	@Autowired
	private ImageService ImageService;
	
	@RequestMapping(value = "/upload")
	@ResponseBody
	public ImageResponseJson uploadImage(HttpServletRequest request,@RequestParam(value = "upfile",required=false) MultipartFile file) throws Exception {
		ImageResponseJson imageResponseJson = new ImageResponseJson();
    	if (!ServletFileUpload.isMultipartContent(request)) {
		      return imageResponseJson;
	    }
    	String fileName = file.getOriginalFilename();
		System.out.println(fileName+"  "+file.getSize());
		imageResponseJson.setName(fileName);
    	imageResponseJson.setOriginal(fileName);
    	imageResponseJson.setUrl("url");
    	imageResponseJson.setType(".jpg");
    	imageResponseJson.setSize(file.getSize());
    	imageResponseJson.setUrl("http://img13.360buyimg.com/n1/jfs/t2845/183/3389528240/401020/38a5b289/578b4450N8bebb141.jpg");
    	System.out.println(imageResponseJson);
        return imageResponseJson;
	}
	 /**
     * 改变图片的大小
     * @param is 上传的图片的输入流
     * @param os 改变了图片的大小后，把图片的流输出到目标OutputStream
     * @param height 新图片的高
     * @param width 新图片的宽
     * @param format 新图片的格式
     * @throws IOException
     */

	public void resizeImage(InputStream is, OutputStream os, int height,int width, String format) throws IOException {
		BufferedImage prevImage = ImageIO.read(is);
		if(height ==0  && width == 0  ) {
			height = prevImage.getHeight();
			width = prevImage.getWidth();
		}
	    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.createGraphics();
        graphics.drawImage(prevImage, 0, 0, width, height, null);
        ImageIO.write(image, format, os);
        os.flush();
        is.close();
        os.close();
    }
	
	
	@RequestMapping(value = "/girl/{height}_{width}")
	public void randomPicture(HttpServletRequest request, HttpServletResponse response,
			@PathVariable Integer height,@PathVariable Integer width
			) throws IOException {
		if(height<0||height>300) {
			height = 300;
		}
		if(width<0 ||width>140) {
			width = 140;
		}
		httpclient = new DefaultHttpClient(new PoolingClientConnectionManager());
		Random random = new Random();
		Integer id = random.nextInt(55278);
		
		JianDanImage jiandan = ImageService.getImageById(id);
		try {
			String filename = "http://" + jiandan.getImage().substring(2, jiandan.getImage().length());
			logger.info("----girl image url :" + filename);
			response.setContentType("image/jpeg");
			HttpGet get = new HttpGet(filename);
			HttpResponse res = httpclient.execute(get);
			HttpEntity entity = res.getEntity();
			// 打印出返回的流的信息
			if (entity != null && res.getStatusLine().getStatusCode() == 200) {
				resizeImage(entity.getContent(),response.getOutputStream(),height,width,"jpg");
		        
				/*byte[] buffer = new byte[4 * 1024];
				int byteRead = -1;
				while ((byteRead = (iputstream.read(buffer))) != -1) {
					stream.write(buffer, 0, byteRead);
				}
				stream.flush();
				iputstream.close();
				stream.close();*/
			} else {
				filename = "http://upload.chinaz.com/upimg/allimg/090330/11153832.jpg";
				get = new HttpGet(filename);
				res = httpclient.execute(get);
				entity = res.getEntity();
				resizeImage(entity.getContent(),response.getOutputStream(),height,width,"jpg");
			}
			EntityUtils.consume(entity); // 会自动释放连接 4.2版本以后的新关闭连接方法 close 有风险，放弃
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	@RequestMapping(value = "/gif")
	public void randomGif(HttpServletRequest request, HttpServletResponse response
			) throws IOException {
		httpclient = new DefaultHttpClient(new PoolingClientConnectionManager());
		Integer maxId = ImageService.getMaxId();
		Random random = new Random();
		Integer id = random.nextInt(maxId-55278)+55278+1;
		
		JianDanImage jiandan = ImageService.getImageById(id);
		try {
			String url = "http://" + jiandan.getImage().substring(2, jiandan.getImage().length());
			logger.info("----gif url :" + url);
			response.setContentType("image/gif");
			HttpGet get = new HttpGet(url);
			HttpResponse res = httpclient.execute(get);
			HttpEntity entity = res.getEntity();
			// 打印出返回的流的信息
			if (entity != null && res.getStatusLine().getStatusCode() == 200) {
				//resizeImage(entity.getContent(),response.getOutputStream(),height,width,"jpg");
				ServletOutputStream stream = response.getOutputStream();
				InputStream inputStream = entity.getContent();
				byte[] buffer = new byte[4 * 1024];
				int byteRead = -1;
				while ((byteRead = (inputStream.read(buffer))) != -1) {
					stream.write(buffer, 0, byteRead);
				}
				stream.flush();
				inputStream.close();
				stream.close();
			} else {
				url = "http://upload.chinaz.com/upimg/allimg/090330/11153832.jpg";
				get = new HttpGet(url);
				res = httpclient.execute(get);
				entity = res.getEntity();
				resizeImage(entity.getContent(),response.getOutputStream(),320,220,"jpg");
			}
			EntityUtils.consume(entity); // 会自动释放连接 4.2版本以后的新关闭连接方法 close 有风险，放弃
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}

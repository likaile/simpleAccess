package cn.lkl.vo;
/**
 * 
 * Date: 2017年11月7日 下午3:52:57 
 * @author likaile 
 * @desc 公告编辑器UEditor上传图片要求返回的固定的json
 */
public class ImageResponseJson {
	/** 第一次get请求需要返回的字段**/
	String imageActionName = "uploadimage";
	String imageFieldName = "upfile";
	Long imageMaxSize = 3145728L;
	String[] imageAllowFiles = new String[] {".png", ".jpg", ".jpeg", ".gif"};
	boolean imageCompressEnable =true;
	int imageCompressBorder =1600;
	String imageInsertAlign = "none";
	String imageUrlPrefix="";
	String imagePathFormat = "";
	
	/** 第二次post请求需要返回的字段**/
	String name ;   //文件名
	String original;//文件名
	Long size;      //文件大小
	String state ="SUCCESS"; //状态 默认为成功
	String type;    //文件类型
	String url="img13.360buyimg.com/n1/jfs/t2845/183/3389528240/401020/38a5b289/578b4450N8bebb141.jpg";     //文件的Url
	String msg;     //返回消息
	public String getImageActionName() {
		return imageActionName;
	}
	public void setImageActionName(String imageActionName) {
		this.imageActionName = imageActionName;
	}
	public String getImageFieldName() {
		return imageFieldName;
	}
	public void setImageFieldName(String imageFieldName) {
		this.imageFieldName = imageFieldName;
	}
	public Long getImageMaxSize() {
		return imageMaxSize;
	}
	public void setImageMaxSize(Long imageMaxSize) {
		this.imageMaxSize = imageMaxSize;
	}
	public String[] getImageAllowFiles() {
		return imageAllowFiles;
	}
	public void setImageAllowFiles(String[] imageAllowFiles) {
		this.imageAllowFiles = imageAllowFiles;
	}
	public boolean isImageCompressEnable() {
		return imageCompressEnable;
	}
	public void setImageCompressEnable(boolean imageCompressEnable) {
		this.imageCompressEnable = imageCompressEnable;
	}
	public int getImageCompressBorder() {
		return imageCompressBorder;
	}
	public void setImageCompressBorder(int imageCompressBorder) {
		this.imageCompressBorder = imageCompressBorder;
	}
	public String getImageInsertAlign() {
		return imageInsertAlign;
	}
	public void setImageInsertAlign(String imageInsertAlign) {
		this.imageInsertAlign = imageInsertAlign;
	}
	public String getImageUrlPrefix() {
		return imageUrlPrefix;
	}
	public void setImageUrlPrefix(String imageUrlPrefix) {
		this.imageUrlPrefix = imageUrlPrefix;
	}
	public String getImagePathFormat() {
		return imagePathFormat;
	}
	public void setImagePathFormat(String imagePathFormat) {
		this.imagePathFormat = imagePathFormat;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "ImageResponseJson [name=" + name + ", size=" + size + ", state=" + state + ", type=" + type + ", url="
				+ url + "]";
	}
	
	
}

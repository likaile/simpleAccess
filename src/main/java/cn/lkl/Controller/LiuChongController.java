package cn.lkl.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lkl.dao.UserDao;
import cn.lkl.service.ILiuChongService;
import cn.lkl.util.StringUtils;
import cn.lkl.vo.ResponseJson;
import cn.lkl.vo.TranactionList;
import cn.lkl.vo.TranactionRecord;
import cn.lkl.vo.TypeCount;
import cn.lkl.vo.User;





@Controller
@RequestMapping({"/access"})
public class LiuChongController
{
  @Autowired
  private ILiuChongService liuChongService;
  
  public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
  public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";
  public static Logger logger = LoggerFactory.getLogger(LiuChongController.class);
  public static boolean isMobile(String mobile) {
      return Pattern.matches(REGEX_MOBILE, mobile);
  }
  public static boolean isPassword(String password) {
      return Pattern.matches(REGEX_PASSWORD, password);
  }
  @RequestMapping({"/liuchong"})
  public String liuchong(HttpServletRequest request)
  {
    if (request.getSession().getAttribute("companyId") != null) {
    	Map<String,Object> map = new HashMap<String, Object>();
        map.put("role", (String)request.getSession().getAttribute("role"));
        map.put("username", (String)request.getSession().getAttribute("username"));	
      return "liuchong/list";
    }
    return "liuchong/login";
  }
  
  @RequestMapping(value={"/liuchong/toLogin"}, method=RequestMethod.POST)
  @ResponseBody
  public ResponseJson login(HttpServletRequest request,String username,String password)
  {
    ResponseJson r = new ResponseJson();
    r.setCode(0);
    if ((StringUtils.isEmpty(username)) || (StringUtils.isEmpty(password))) {
      r.setMessage("用户名或密码不能为空！");
      return r;
    }
    User user = liuChongService.selectUserByUserName(username);
    if (null!=user &&password.equals(user.getPassword()))
    {
      request.getSession().setAttribute("companyId", user.getCompanyid());
      request.getSession().setAttribute("role", user.getRole());
      request.getSession().setAttribute("pwd", user.getPassword());
      request.getSession().setAttribute("action", user.getAction());
      request.getSession().setAttribute("username", user.getUsername());
      request.getSession().setAttribute("mobile", user.getMobile());
      r.setCode(1);
      return r;
    }
    r.setMessage("用户名或密码错误！");
    return r;
  }
  @RequestMapping(value={"/liuchong/getCountByType"}, method=RequestMethod.POST)
  @ResponseBody
  public ResponseJson getCountByType(HttpServletRequest request)
  {
	if(request.getParameter("type") == null) {
		return null;
	}
    String type = request.getParameter("type").trim();
    ResponseJson r = new ResponseJson();
    r.setMessage("0");
    Integer companyId = (Integer) request.getSession().getAttribute("companyId");
    Integer count = liuChongService.getCountByType(type,companyId);
    r.setMessage(count+"");
    return r;
  }
  @RequestMapping(value={"/liuchong/callBackData"}, method=RequestMethod.POST)
  @ResponseBody
  public ResponseJson callBackData(HttpServletRequest request)
  {	
	ResponseJson r = new ResponseJson();
	String action = "";
	try {
		action = (String) request.getSession().getAttribute("action");
	} catch (Exception e) {
	}
	if(!action.contains("回滚")) {
		r.setMessage("无权限进行该操作,请联系管理员开通误操作回滚权限！");
		return r;
	}
    String id = request.getParameter("id");
    TranactionRecord tr = liuChongService.getTranactionRecordByID(id);
    if(null == tr){
    	r.setMessage("误操作回滚失败，原因该记录不存在");
    	return r;
    }
    r.setMessage(liuChongService.callBackDate(tr));
    return r;
  }
  @RequestMapping({"/page/{page}"})
  public String liuchong(@PathVariable String page, HttpServletRequest request)
  {	
	
    if (page.equals("loginOut")) {
    	request.getSession().invalidate();
    }
    Map<String,Object> map = new HashMap<String, Object>();
    map.put("mobile", (String)request.getSession().getAttribute("mobile"));	
    return "liuchong/" + page;
  }
  
  @RequestMapping(value={"/liuchong/saveType"}, method=RequestMethod.POST,produces ="application/json;charset=UTF-8")
  @ResponseBody
  public ResponseJson saveType(HttpServletRequest request)
  {
    String type = request.getParameter("type").trim();
    String desc = request.getParameter("desc");
    ResponseJson r = new ResponseJson();
    r.setCode(0);
    String action = "";
	try {
		action = (String) request.getSession().getAttribute("action");
	} catch (Exception e) {
	}
	if(!action.contains("新增商品")) {
		r.setMessage("无权限进行该操作,请联系管理员开通新增商品种类权限！");
		return r;
	}
    if (StringUtils.isEmpty(type))
    {
      r.setMessage("商品种类不能为空");
      return r;
    }
    Integer companyId;
	try {
		companyId = (Integer)request.getSession().getAttribute("companyId");
	} catch (Exception e1) {
		r.setMessage("保存失败,请刷新页面后重试！");
	      return r;
	}
    if(liuChongService.queryType(companyId,type)) {
    	r.setMessage("商品种类已存在！");
    	return r;
    }
    try {
		liuChongService.saveType(type, desc,companyId);
	} catch (Exception e) {
		r.setMessage("商品种类保存失败！");
		return r;
	}
	r.setCode(1);
    return r;
  }
  
  @RequestMapping({"/liuchong/getTypes"})
  @ResponseBody
  public ResponseJson getTypes(HttpServletRequest request)
  {
    ResponseJson r = new ResponseJson();
    r.setCode(1);
	Integer companyId = (Integer)request.getSession().getAttribute("companyId");
    r.setMessage(liuChongService.getTypes(companyId));
    return r;
  }
  
  @RequestMapping(value={"/liuchong/saveItem"}, method=RequestMethod.POST,produces ="application/json;charset=UTF-8")
  @ResponseBody
  public ResponseJson saveItem(HttpServletRequest request)
  {
    String type = request.getParameter("type").trim();
    String desc = request.getParameter("desc");
    String num = request.getParameter("num");
    String time = request.getParameter("time");
    ResponseJson r = new ResponseJson();
    r.setCode(0);
    String action = "";
	try {
		action = (String) request.getSession().getAttribute("action");
	} catch (Exception e) {
	}
	if(!action.contains("入库")) {
		r.setMessage("无权限进行该操作,请联系管理员开通入库操作权限！");
		return r;
	}
    if ((StringUtils.isEmpty(type)) || (StringUtils.isEmpty(num)) || (type.equals("请选择")))
    {
      r.setMessage("商品种类或者数量不能为空");
      return r;
    }
    int num1 = 0 ;
    try
    {
      num1 = Integer.valueOf(num).intValue();
    }
    catch (Exception e)
    {
      r.setMessage("参数错误！");
      return r;
    }
    String addFromUser;
    Integer companyId;
    try {
    	addFromUser = (String) request.getSession().getAttribute("username");
    	companyId = (Integer) request.getSession().getAttribute("companyId");
    } catch (Exception e1) {
    	r.setMessage("入库出错,请刷新页面重新操作");
    	return r;
    }
    if (!liuChongService.queryType(companyId,type))
    {
      r.setMessage("商品种类不存在！");
      return r;
    }

    Integer count = 0;
	try {
		count = liuChongService.saveItem(type, num1, desc, time,addFromUser,companyId);
	} catch (Exception e) {
	    r.setMessage("入库出错,原因为："+e.getMessage());
	    return r;
	}
    r.setCode(1);
    logger.info(type+"入库数量："+num1+" 成功,入库后"+type+"的总数量位："+count+" 入库时间"+time);
    r.setMessage(type+"入库数量："+num1+" 成功,入库后"+type+"的总数量位："+count);
    return r;
  }
  
  @RequestMapping(value={"/liuchong/editItem"}, method=RequestMethod.POST,produces =  "application/json;charset=UTF-8")
  @ResponseBody
  public ResponseJson editItem(HttpServletRequest request)
  {
    String type = request.getParameter("type").trim();
    String desc = request.getParameter("desc");
    String num = request.getParameter("num");
    String addTime = request.getParameter("addTime");
    String editToUser = request.getParameter("editToUser");
    ResponseJson r = new ResponseJson();
    r.setCode(0);
    String action = "";
   	try {
   		action = (String) request.getSession().getAttribute("action");
   	} catch (Exception e) {
   	}
   	if(!action.contains("出库")) {
   		r.setMessage("无权限进行该操作,请联系管理员开通出库操作权限！");
   		return r;
   	}
    if ((StringUtils.isEmpty(type)) || (StringUtils.isEmpty(num)) || (type.equals("请选择")))
    {
      r.setMessage("商品种类或者数量不能为空");
      return r;
    }
    
    int num1;
    try
    {
      num1 = Integer.valueOf(num).intValue();
    }
    catch (Exception e)
    {
      r.setMessage("参数错误！");
      return r;
    }
    String editFromUser;Integer companyId;
    try {
    	editFromUser = (String) request.getSession().getAttribute("username");
    	companyId = (Integer) request.getSession().getAttribute("companyId");
    } catch (Exception e1) {
    	r.setMessage("出库出错,请刷新页面,重新进行操作");
    	return r;
    }
    if (!liuChongService.queryType(companyId,type))
    {
      r.setMessage("商品种类不存在！");
      return r;
    }
    if(StringUtils.isEmpty(editToUser)) {
    	r.setMessage("出库领用人不能为空！");
        return r;
    }

    Integer count = 0;
    try
    {
    	count = liuChongService.editItem(type, num1, desc, addTime,editFromUser,editToUser,companyId);
    }
    catch (Exception e)
    {
      r.setMessage("出库出错,原因："+e.getMessage());
      return r;
    }
    r.setCode(1);
    logger.info(type+"出库数量："+num1+" 成功,出库后"+type+"的总数量位："+count+" 出库时间"+addTime);
    r.setMessage(type+"出库数量："+num1+" 成功,出库后"+type+"的剩余数量为："+count);
    return r;
  }
  
  @RequestMapping(value={"/liuchong/tranactionList"}, produces={"application/json;charset=UTF-8;"})
  @ResponseBody
  public TranactionList getItemList(HttpServletRequest request,Integer page, Integer rows, String orderType, String time, String type)
    throws Exception
  {
	  Integer companyId;
		try {
			companyId = (Integer) request.getSession().getAttribute("companyId");
		} catch (Exception e1) {
			throw new RuntimeException("系统内部出错,请刷新页面重试！");
	}
    List<TranactionRecord> all = liuChongService.getTranactionRecord(page, rows,orderType, time, type,companyId);
    TranactionList result = new TranactionList();
    int total =liuChongService.getAllTypeTrans(companyId);
    result.setTotal(StringUtils.isEmpty(type)?total:all.size());
    result.setRows(all);
    return result;
  }
  
  @RequestMapping(value="/liuchong/typesCount",produces =  "application/json;charset=UTF-8")
  @ResponseBody
  public TranactionList typesCount(HttpServletRequest request,Integer page, Integer rows,String type)
  {	
	/*if (!StringUtils.isEmpty(type)) {
		  try {
			type = new String(type.getBytes("iso8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			type= null;
		}
	}else{
		type=null;
	}*/
	  Integer companyId;
		try {
			companyId = (Integer) request.getSession().getAttribute("companyId");
		} catch (Exception e1) {
			throw new RuntimeException("系统内部出错,请刷新页面重试！");
	}
	if(StringUtils.isEmpty(type)) {
		type=null;
	}
    List<TypeCount> all = liuChongService.getTypeCount(page, rows,type,companyId);
    TranactionList result = new TranactionList();
    int total = liuChongService.getAllTypeInfos(companyId);
    result.setTotal(StringUtils.isEmpty(type)?total:all.size());
    result.setRows(all);
    return result;
  }
  
  @RequestMapping(value="/liuchong/saveUser",produces =  "application/json;charset=UTF-8")
  @ResponseBody
  public ResponseJson saveUser(HttpServletRequest request)
  {	
	  ResponseJson r = new ResponseJson();
	  r.setCode(0);
	  String role ="";
	  try {
		 role = (String) request.getSession().getAttribute("role");
	  } catch (Exception e) {
		r.setMessage("系统出错，请刷新页面重试！");
		return r;
	  }
	  if(!role.equals("admin")) {
		r.setMessage("您无权进行相关操作,请联系管理员！");
		return r;
	  }
	  String mobile = request.getParameter("mobile");
	  String username = request.getParameter("username");
	  String password = request.getParameter("password");
	  String isAdd = request.getParameter("isAdd");
	  String isEdit = request.getParameter("isEdit");
	  String isSave = request.getParameter("isSave");
	  String isCallBack = request.getParameter("isCallBack");
	  if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(username)||StringUtils.isEmpty(password)) {
		  r.setMessage("参数不全,请重新提交");
			return r;
	  }
	  if(!isMobile(mobile)||!isPassword(password)) {
		  r.setMessage("参数校验失败,请检查您的手机号或者密码");
			return r;
	  }
	  if(null != liuChongService.selectUserByUserName(mobile)) {
		  r.setMessage("该手机号已存在！");
		  return r;
	  }
	  Integer companyId = (Integer) request.getSession().getAttribute("companyId");
	  liuChongService.saveUser(mobile,username,password,isAdd,isEdit,isSave,isCallBack,companyId);
	  r.setCode(1);
	  r.setMessage("新增用户成功！");
	  return r;
  }
  
  @RequestMapping(value="/liuchong/changePwd",produces =  "application/json;charset=UTF-8")
  @ResponseBody
  public ResponseJson changePwd(HttpServletRequest request)
  {	
	  ResponseJson r = new ResponseJson();
	  r.setCode(0);
	  String oldPassword = request.getParameter("oldPassword");
	  String newPassword = request.getParameter("newPassword");
	  String role = (String) request.getSession().getAttribute("role");
	  String mobile = (String) request.getSession().getAttribute("mobile");
	  if(!oldPassword.equals(liuChongService.selectUserByUserName(mobile).getPassword())) {
		  r.setMessage("旧密码错误！");
		  return r;
	  }
	  if(!isPassword(newPassword)) {
		  r.setMessage("新密码格式错误！");
		  return r;
	  }
	  if(role.equals("guest")) {
		  r.setMessage("无权修改访客密码,请联系管理员！");
		  return r;
	  }
	  liuChongService.updateUserPwd(mobile,newPassword);
	  request.getSession().invalidate();
	  r.setCode(1);
	  return r;
  }
  
  
  
  
  
  
  
  
}


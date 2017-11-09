package cn.lkl.Interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.lkl.util.JsonUtils;

public class MyInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(MyInterceptor.class);
    private static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info(sd.format(new Date())+" ip:"+  request.getRemoteAddr()+" url:"+request.getRequestURI()+" params:"+JsonUtils.parseObject(request.getParameterMap()));
        //获取session
        if(request.getRequestURI().startsWith("/access")&&request.getSession().getAttribute("companyId") == null) {
            logger.info("------未登陆,跳转到login页面！当前访问地址："+request.getRequestURI());
            response.sendRedirect(request.getContextPath()+"/access/liuchong.html");
            return false;
        }
        return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

    

}

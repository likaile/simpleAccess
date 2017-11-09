package cn.lkl.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.lkl.Interceptor.MyInterceptor;


@Configuration
public class WebConfig  extends WebMvcConfigurerAdapter {

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/access/**").excludePathPatterns("/access/liuchong/toLogin").excludePathPatterns("/access/liuchong.html");
        super.addInterceptors(registry);
    }

}

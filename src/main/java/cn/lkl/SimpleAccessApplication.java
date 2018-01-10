package cn.lkl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@MapperScan("cn.lkl.dao")
public class SimpleAccessApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SimpleAccessApplication.class, args);
	}
	 @Bean
	 public EmbeddedServletContainerCustomizer containerCustomizer(){
	        return new EmbeddedServletContainerCustomizer() {
				@Override
				public void customize(ConfigurableEmbeddedServletContainer container) {
					container.setSessionTimeout(6000);//session 过期时间 单位为S
				}
	     };
	 }
	 
}

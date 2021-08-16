package cn.toseektech.login.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.toseektech.login.core.DefaultLoginProcessEngine;
import cn.toseektech.login.core.LoginCheckingProcessor;
import cn.toseektech.login.core.LoginProcessEngine;
import cn.toseektech.login.core.LoginProperties;
import cn.toseektech.login.core.PathCheckingProcessor;
import cn.toseektech.login.core.UserLoginService;
import cn.toseektech.login.utils.JwtUtils;

@Configuration
@ConditionalOnBean(UserLoginService.class)
@EnableConfigurationProperties(LoginProperties.class)
public class ToSeekTechLoginConfiguration {
	  
	  /**
	   * jwt工具类
	   * @return
	   */
	
	  @Bean
	  public JwtUtils jwtUtils() {
		  return new JwtUtils();
	  }
	  
	  /**
	   * 登录信息验证处理类
	   * @return
	   */
	  @Bean
	  public LoginCheckingProcessor loginCheckingProcessor() {
		  return new LoginCheckingProcessor();
	  }
	  
	  /**
	   * 请求路径验证处理类
	   */
	  @Bean
	  public PathCheckingProcessor pathCheckingProcessor() {
		  return new PathCheckingProcessor();
	  }
	  
	  /**
	   * 登录处理引擎
	   */
	  @Bean
	  public LoginProcessEngine defaultLoginProcessEngine() {
		  return new DefaultLoginProcessEngine();
	  }
    
}

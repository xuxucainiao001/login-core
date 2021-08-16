package cn.toseektech.login.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public interface CheckingProcessor {
	
	/**
	 * 检查处理器
	 * @param request
	 * @param response
	 * @return  true:检查处理成功，false:检查处理失败
	 */
	public boolean check(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 检查处理器 (适用于非servlet 容器)
	 * @param request
	 * @param response
	 * @return  true:检查处理成功，false:检查处理失败
	 */
	public boolean check(ServerHttpRequest request,ServerHttpResponse response);

}

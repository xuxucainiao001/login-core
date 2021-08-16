package cn.toseektech.login.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public interface LoginProcessEngine {

	/**
	 * 登录验证处理 (适用于非servlet 容器)
	 * 
	 * @param request
	 * @param response
	 * @return true：处理成功 false 处理失败
	 */
	boolean process(ServerHttpRequest request, ServerHttpResponse response);

	/**
	 * 登录验证处理
	 * 
	 * @param request
	 * @param response
	 * @return true：处理成功 false 处理失败
	 */
	boolean process(HttpServletRequest request, HttpServletResponse response);

}

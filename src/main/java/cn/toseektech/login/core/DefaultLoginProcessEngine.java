package cn.toseektech.login.core;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public class DefaultLoginProcessEngine implements LoginProcessEngine {

	@Resource
	private LoginCheckingProcessor loginCheckingProcessor;

	@Resource
	private PathCheckingProcessor pathCheckingProcessor;

	@Override
	public boolean process(HttpServletRequest request, HttpServletResponse response) {
		// 匹配上的地址需要登录
		if (pathCheckingProcessor.check(request, response)) {
			return loginCheckingProcessor.check(request, response);
		}
		// 未匹配上的地址无需验证，直接通过
		return true;
	}

	@Override
	public boolean process(ServerHttpRequest request, ServerHttpResponse response) {
		// 匹配上的地址需要登录
		if (pathCheckingProcessor.check(request, response)) {
			return loginCheckingProcessor.check(request, response);
		}
		// 未匹配上的地址无需验证，直接通过
		return true;
	}

}

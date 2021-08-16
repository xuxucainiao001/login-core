package cn.toseektech.login.core;

import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class PathCheckingProcessor implements CheckingProcessor {

	@Resource
	private LoginProperties loginProperties;

	@Override
	public boolean check(HttpServletRequest request, HttpServletResponse response) {
		String requestrUri = request.getRequestURI();
		return check0(requestrUri);

	}

	@Override
	public boolean check(ServerHttpRequest request, ServerHttpResponse response) {
		String requestrUri = request.getPath().toString();
		return check0(requestrUri);
	}

	private boolean check0(String requestUri) {
		Set<String> paths = loginProperties.getPaths();
		if(CollectionUtils.isEmpty(paths)) {
			return false;
		}
		for (String path : paths) {
			if (isMatch(path, requestUri)) {
				return true;
			}
		}
		return false;
	}

	private boolean isMatch(String path, String requestrUri) {
		if (path.endsWith("**")) {
			return requestrUri.startsWith(StringUtils.split(path, "**")[0]);
		}
		return path.equals(requestrUri);
	}
}

package com.yuckar.infra.trace.client;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

import com.yuckar.infra.base.trace.TraceIDUtils;

public class TraceHttpRequestInterceptor implements HttpRequestInterceptor {

	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		String traceid = TraceIDUtils.get();
		if (!StringUtils.isEmpty(traceid)) {
			request.addHeader(TraceIDUtils.ID_REQUEST, traceid);
			context.setAttribute(TraceIDUtils.ID_REQUEST, traceid);
		}
	}
}

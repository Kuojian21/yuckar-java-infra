package com.yuckar.infra.network.http.async;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.slf4j.Logger;

import com.yuckar.infra.common.logger.LoggerUtils;

public class DefaultFutureCallback implements FutureCallback<HttpResponse> {

	public static FutureCallback<HttpResponse> DEFAULT = new DefaultFutureCallback();

	private final Logger logger = LoggerUtils.logger(getClass());

	@Override
	public void completed(HttpResponse response) {

	}

	@Override
	public void failed(Exception ex) {
		logger.error("", ex);
	}

	@Override
	public void cancelled() {
		logger.warn("cancelled!!!");
	}

}

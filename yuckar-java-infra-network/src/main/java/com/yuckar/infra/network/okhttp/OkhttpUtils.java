package com.yuckar.infra.network.okhttp;

import java.util.concurrent.TimeUnit;

import com.yuckar.infra.common.json.ConfigUtils;
import com.yuckar.infra.trace.okhttp.TraceInterceptor;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

public class OkhttpUtils {

	public static OkHttpClient okhttp(OkhttpInfo info) {
		Dispatcher dispatcher = new Dispatcher();
		info.getDispatcher().putIfAbsent("maxRequestsPerHost", 8);
		info.getDispatcher().putIfAbsent("maxRequests", 512);
		ConfigUtils.config(dispatcher, info.getDispatcher());
		return ConfigUtils
				.config(new OkHttpClient.Builder().dispatcher(dispatcher).connectTimeout(1, TimeUnit.SECONDS)
						.readTimeout(1, TimeUnit.SECONDS).writeTimeout(1, TimeUnit.SECONDS), info.getData())
				.addInterceptor(new TraceInterceptor()).build();
	}

}

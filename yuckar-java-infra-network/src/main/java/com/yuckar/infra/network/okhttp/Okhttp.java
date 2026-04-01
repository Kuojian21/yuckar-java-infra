package com.yuckar.infra.network.okhttp;

import com.yuckar.infra.common.executor.LazyExecutor;

import okhttp3.OkHttpClient;

public class Okhttp extends LazyExecutor<OkHttpClient, OkhttpInfo> {

	public Okhttp(OkhttpInfo info) {
		super(info, () -> OkhttpUtils.okhttp(info));
	}
}

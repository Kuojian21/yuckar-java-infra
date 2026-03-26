package com.yuckar.infra.network.browser;

import org.htmlunit.WebClient;

import com.yuckar.infra.executor.pool.PoolExecutor;

public class Browser extends PoolExecutor<WebClient, BrowserInfo> {

	public Browser(BrowserInfo info) {
		super(info);
	}

	@Override
	protected WebClient create() throws Exception {
		return BrowserUtils.client(info());
	}

	@Override
	protected void destroy(WebClient bean) throws Exception {
		bean.close();
	}

}

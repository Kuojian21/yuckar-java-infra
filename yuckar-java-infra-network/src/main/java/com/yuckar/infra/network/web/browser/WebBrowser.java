package com.yuckar.infra.network.web.browser;

import org.htmlunit.WebClient;

import com.yuckar.infra.base.executor.PoolExecutor;

public class WebBrowser extends PoolExecutor<WebClient, WebBrowserInfo> {

	public WebBrowser(WebBrowserInfo info) {
		super(info);
	}

	@Override
	protected WebClient create() throws Exception {
		return WebBrowserUtils.client(info());
	}

	@Override
	protected void destroy(WebClient bean) throws Exception {
		bean.close();
	}

}

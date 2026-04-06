package com.yuckar.infra.network.web.browser;

import org.htmlunit.WebClient;
import org.slf4j.Logger;

import com.yuckar.infra.base.json.ConfigUtils;
import com.yuckar.infra.base.logger.LoggerUtils;

public class WebBrowserUtils {

	public static final Logger logger = LoggerUtils.logger(WebBrowserUtils.class);

	public static WebClient client(WebBrowserInfo info) throws Exception {
		WebClient webClient = new WebClient(info.toBrowserVersion());
		ConfigUtils.config(webClient, info.getListeners());
		ConfigUtils.config(webClient.getOptions(), info.getOptions());
		return webClient;
	}

}

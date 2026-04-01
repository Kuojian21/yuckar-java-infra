package com.yuckar.infra.network.browser;

import org.htmlunit.WebClient;
import org.slf4j.Logger;

import com.yuckar.infra.common.json.ConfigUtils;
import com.yuckar.infra.common.logger.LoggerUtils;

public class BrowserUtils {

	public static final Logger logger = LoggerUtils.logger(BrowserUtils.class);

	public static WebClient client(BrowserInfo info) throws Exception {
		WebClient webClient = new WebClient(info.toBrowserVersion());
		ConfigUtils.config(webClient, info.getListeners());
		ConfigUtils.config(webClient.getOptions(), info.getOptions());
		return webClient;
	}

}

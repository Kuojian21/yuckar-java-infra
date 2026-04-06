package com.yuckar.infra.network.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.network.web.browser.WebBrowser;
import com.yuckar.infra.network.web.browser.WebBrowserInfo;

public interface WebBrowserYconf extends CacheableInfoYconf<WebBrowserInfo, WebBrowser> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.web_browser(key());
	}

}

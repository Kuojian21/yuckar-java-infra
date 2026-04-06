package com.yuckar.infra.network.yconf;

import com.yuckar.infra.conf.info.CacheableInfoYconf;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.network.web.capture.WebChromeCapture;
import com.yuckar.infra.network.web.capture.WebChromeCaptureInfo;

public interface WebCaptureYconf extends CacheableInfoYconf<WebChromeCaptureInfo, WebChromeCapture> {

	String key();

	@Override
	default String path() {
		return YconfsNamespaceUtils.web_capture(key());
	}

}

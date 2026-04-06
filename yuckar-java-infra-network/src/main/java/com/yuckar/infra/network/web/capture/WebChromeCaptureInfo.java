package com.yuckar.infra.network.web.capture;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.chrome.ChromeDriver;

import com.yuckar.infra.base.lazy.LazySupplier;

public class WebChromeCaptureInfo extends WebCaptureInfo<ChromeDriver> {

	public static final LazySupplier<WebChromeCaptureInfo> DEFAULT = LazySupplier.wrap(() -> {
		WebChromeCaptureInfo info = new WebChromeCaptureInfo();
		GenericObjectPoolConfig<ChromeDriver> config = new GenericObjectPoolConfig<ChromeDriver>();
		config.setMinIdle(0);
		config.setMaxTotal(Runtime.getRuntime().availableProcessors());
		info.setPoolConfig(config);
		return info;
	});

}
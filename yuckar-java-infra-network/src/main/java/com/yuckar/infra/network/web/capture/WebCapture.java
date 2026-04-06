package com.yuckar.infra.network.web.capture;

import org.openqa.selenium.WebDriver;

import com.yuckar.infra.base.executor.PoolExecutor;
import com.yuckar.infra.base.lazy.LazyRunnable;

import io.github.bonigarcia.wdm.WebDriverManager;

public abstract class WebCapture<T extends WebDriver, I extends WebCaptureInfo<T>> extends PoolExecutor<T, I> {

	public static void driver_chrome() {
		driver_chrome.run();
	}

	public static void driver_firefox() {
		driver_firefox.run();
	}

	public static void driver_edge() {
		driver_edge.run();
	}

	private static final LazyRunnable driver_chrome = LazyRunnable.wrap(() -> {
		WebDriverManager.chromedriver().setup();
	});
	private static final LazyRunnable driver_firefox = LazyRunnable.wrap(() -> {
		WebDriverManager.firefoxdriver().setup();
	});
	private static final LazyRunnable driver_edge = LazyRunnable.wrap(() -> {
		WebDriverManager.edgedriver().setup();
	});

	protected WebCapture(I info) {
		super(info);
	}

	@Override
	protected void destroy(T driver) {
		driver.quit();
	}

}

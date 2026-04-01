package com.yuckar.infra.common.file.watch;

import org.apache.commons.io.monitor.FileAlterationListener;

public interface IWatch {

	static IWatch monitor() {
		return monitor(10_000);
	}

	static IWatch monitor(long interval) {
		return WatchMonitorImpl.of(interval);
	}

	static IWatch watch() {
		return WatchServiceImpl.of();
	}

	void watch(String path, FileAlterationListener handler);

}

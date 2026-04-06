package com.yuckar.infra.conf.yconfs.utils;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import com.yuckar.infra.base.file.watch.IWatch;
import com.yuckar.infra.base.lazy.LazySupplier;

public class YconfsFileUtils {

	private static final LazySupplier<IWatch> monitor = LazySupplier.wrap(() -> IWatch.monitor());

	public static String toFile(String workspace, String path) {
		path = path.trim();
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		return workspace + File.separator + path.replace("/", File.separator);
	}

	public static String toPath(String workspace, String file) {
		return file.substring(workspace.length() + 1);
	}

	public static void monitor(String file, FileAlterationListenerAdaptor listener) {
		monitor.get().watch(file, listener);
	}

}

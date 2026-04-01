package com.yuckar.infra.common.scanner;

import com.annimon.stream.function.Predicate;
import com.yuckar.infra.common.thread.utils.ThreadHelper;

public class FileScanner extends Scanner<String> {

	public static FileScanner of(Predicate<String> filter) {
		return of(filter, ThreadHelper.getContextClassLoader());
	}

	public static FileScanner of(Predicate<String> filter, ClassLoader loader) {
		return new FileScanner(filter, loader);
	}

	private FileScanner(Predicate<String> filter, ClassLoader loader) {
		super(s -> s, filter, loader);
	}

	public FileScanner scan(String path) {
		super.scan(path, ".*" + path + ".*");
		return this;
	}

}

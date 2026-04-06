package com.yuckar.infra.base.scanner;

import com.annimon.stream.function.Predicate;
import com.yuckar.infra.base.thread.ThreadHelper;

public class ClazzScanner extends Scanner<Class<?>> {

	public static ClazzScanner of(Predicate<Class<?>> filter) {
		return of(filter, ThreadHelper.getContextClassLoader());
	}

	public static ClazzScanner of(Predicate<Class<?>> filter, ClassLoader loader) {
		return new ClazzScanner(filter, loader);
	}

	private ClazzScanner(Predicate<Class<?>> filter, ClassLoader loader) {
		super(cls -> Class.forName(cls.replace("/", ".")), filter, loader);
	}

	public ClazzScanner scan(String pkg) {
		super.scan(pkg.replace(".", "/"), "(" + pkg.replace(".", "/") + ".+)\\.class$");
		return this;
	}

}

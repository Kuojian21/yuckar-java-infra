package com.yuckar.infra.base.logger;

import java.util.Map;

import org.slf4j.Logger;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Supplier;
import com.yuckar.infra.base.lazy.LazySupplier;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.CoreConstants;

public class PkgKLoggerFactory implements IKLoggerFactory {

	private final Supplier<IKLoggerFactory> factoryDef = LazySupplier.wrap(() -> new Slf4jKLoggerFactory());
	private final Map<String, IKLoggerFactory> factoryMap;

	public PkgKLoggerFactory(Map<String, IKLoggerFactory> factoryMap) {
		this.factoryMap = factoryMap;
		Stream.of(factoryMap.values()).forEach(f -> {
			if (f instanceof LoggerContext) {
				((LoggerContext) f).getFrameworkPackages().add("com.yuckar.infra.common.logger");
			}
		});
	}

	@Override
	public Logger getLogger(String name) {
		IKLoggerFactory factory = null;
		int i = 0;
		while (true) {
			int h = getSeparatorIndexOf(name, i);
			String pkg = name;
			if (h > 0) {
				pkg = name.substring(0, h);
			}
			factory = factoryMap.get(pkg);
			i = h + 1;
			if (h == -1) {
				if (factory == null) {
					return factoryDef.get().getLogger(name);
				} else {
					return factory.getLogger(name);
				}
			}
		}
	}

	public static int getSeparatorIndexOf(String name, int fromIndex) {
		int dotIndex = name.indexOf(CoreConstants.DOT, fromIndex);
		int dollarIndex = name.indexOf(CoreConstants.DOLLAR, fromIndex);

		if (dotIndex == -1 && dollarIndex == -1)
			return -1;
		if (dotIndex == -1)
			return dollarIndex;
		if (dollarIndex == -1)
			return dotIndex;

		return dotIndex < dollarIndex ? dotIndex : dollarIndex;
	}

	public static void main(String[] args) {
		String name = PkgKLoggerFactory.class.getName();
		System.out.println(name.substring(0, getSeparatorIndexOf("." + name, 0)));
	}

}

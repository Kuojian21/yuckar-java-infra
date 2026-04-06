package com.yuckar.infra.base.logger;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.slf4j.Logger;

import com.annimon.stream.Stream;
import com.yuckar.infra.base.utils.ProxyUtils;

import ch.qos.logback.classic.LoggerContext;

public class MultiKLoggerFactory implements IKLoggerFactory {

	private final List<IKLoggerFactory> factories;

	public MultiKLoggerFactory(List<IKLoggerFactory> factories) {
		super();
		this.factories = factories;
		Stream.of(factories).forEach(f -> {
			if (f instanceof LoggerContext) {
				((LoggerContext) f).getFrameworkPackages().add("com.yuckar.infra.common.logger");
			}
		});
	}

	@Override
	public Logger getLogger(String name) {
		return (Logger) ProxyUtils.jvm(Logger.class, (obj, method, args, proxy) -> {
			String mName = method.getName();
			if ("trace".equals(mName) || "debug".equals(mName) || "info".equals(mName) || "warn".equals(mName)
					|| "error".equals(mName)) {
				factories.forEach(f -> {
					try {
						method.invoke(f.getLogger(name), args);
					} catch (IllegalAccessException | InvocationTargetException e) {
						try {
							f.getLogger(name).error("{}", f.getClass(), e);
						} catch (Throwable t) {
							e.printStackTrace();
						}
					}
				});
				return null;
			} else {
				return method.invoke(factories.get(0).getLogger(name), args);
			}
		});
	}

}

package com.yuckar.infra.register.legacy;

import java.util.List;

import org.slf4j.Logger;

import com.yuckar.infra.common.logger.LoggerUtils;
import com.yuckar.infra.common.spi.SpiFactory;

public class RegisterRawDataFactory {

	private static final Logger logger = LoggerUtils.logger(IRegisterRawDataHandler.class);
	private static final List<IRegisterRawDataHandler> handlers;
	static {
		try {
			handlers = SpiFactory.list(IRegisterRawDataHandler.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void handle(Class<?> clazz, Object obj) {
		try {
			for (IRegisterRawDataHandler handler : handlers) {
				if (handler.forClazz().isAssignableFrom(clazz)) {
					handler.handle(obj);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}

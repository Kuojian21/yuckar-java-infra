package com.yuckar.infra.register.legacy;

public interface IRegisterRawDataHandler {

	Class<?> forClazz();

	void handle(Object rawData);

}

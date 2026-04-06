package com.yuckar.infra.conf.legacy;

public interface IRegisterRawDataHandler {

	Class<?> forClazz();

	void handle(Object rawData);

}

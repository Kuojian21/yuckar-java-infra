package com.yuckar.infra.register;

@FunctionalInterface
public interface RegisterListener<D> {

	void onChange(RegisterEvent<D> event);

}

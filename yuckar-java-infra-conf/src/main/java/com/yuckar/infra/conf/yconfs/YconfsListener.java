package com.yuckar.infra.conf.yconfs;

@FunctionalInterface
public interface YconfsListener<D> {

	void onChange(YconfsEvent<D> event);

}

package com.yuckar.infra.cluster;

public interface Master<R> {

	R master();

	R slave();

}

package com.yuckar.infra.base.thread;

import java.util.concurrent.ExecutorService;

public interface KrExecutorService extends ExecutorService, AutoCloseable {

	void shutdownBlocking();

	@Override
	void close();

}

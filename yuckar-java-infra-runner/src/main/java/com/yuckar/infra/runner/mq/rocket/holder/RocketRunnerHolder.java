package com.yuckar.infra.runner.mq.rocket.holder;

import org.apache.rocketmq.client.apis.consumer.PushConsumer;

import com.yuckar.infra.conf.yconfs.context.YconfsFactory;
import com.yuckar.infra.runner.common.RunnerHolder;
import com.yuckar.infra.runner.mq.rocket.RocketRunner;

public class RocketRunnerHolder extends RunnerHolder<RocketRunner> {

	public static RocketRunnerHolder of(RocketRunner runner) {
		return new RocketRunnerHolder(runner);
	}

	private volatile PushConsumer consumer;

	protected RocketRunnerHolder(RocketRunner runner) {
		super(runner, YconfsFactory.getContext(runner.topic().getClass()));
	}

	public void consumer(PushConsumer consumer) {
		this.consumer = consumer;
	}

	@Override
	public void close() throws Exception {
		if (this.consumer != null) {
			this.consumer.close();
		}
	}

}

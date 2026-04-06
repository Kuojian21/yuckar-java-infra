package com.yuckar.infra.runner.mq.rocket.server;

import java.util.Collections;
import java.util.Properties;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;

import com.yuckar.infra.base.json.ConfigUtils;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.term.TermHelper;
import com.yuckar.infra.base.utils.RunUtils;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.runner.mq.rocket.RocketRunner;
import com.yuckar.infra.runner.mq.rocket.holder.RocketRunnerHolder;
import com.yuckar.infra.runner.server.AbstractRunnerServer;

public class RocketRunnerServer extends AbstractRunnerServer<RocketRunner> {

	@Override
	protected void doRun(RocketRunner runner) {
		RocketRunnerHolder holder = RocketRunnerHolder.of(runner);
		String path = YconfsNamespaceUtils.rocket(runner.topic().topic() + "/consumer");
		Yconfs<Properties> yconfs = holder.context().getYconfs(Properties.class);

		LazySupplier<RocketRunnerHolder> holder_supplier = LazySupplier.wrap(() -> RunUtils.throwing(() -> {
			PushConsumer consumer = ClientServiceProvider.loadService().newPushConsumerBuilder()
					.setClientConfiguration(
							ConfigUtils.config(ClientConfiguration.newBuilder(), yconfs.get(path)).build())
					.setConsumerGroup(runner.group().group())
					.setSubscriptionExpressions(Collections.singletonMap(runner.topic().topic(),
							new FilterExpression(runner.tag(), FilterExpressionType.TAG)))
					.setMessageListener(message -> {
						runner.handle(message);
						return ConsumeResult.SUCCESS;
					}).build();
			holder.consumer(consumer);
			return holder;
		}));

		yconfs.addListener(path, event -> {
			holder_supplier.refresh(RocketRunnerHolder::close);
			holder_supplier.get();
		});
		TermHelper.addTerm(runner.module(), () -> holder.close());
		holder_supplier.get();
	}

	@Override
	protected boolean nlock() {
		return false;
	}

}

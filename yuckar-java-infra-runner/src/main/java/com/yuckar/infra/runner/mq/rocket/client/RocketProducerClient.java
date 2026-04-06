package com.yuckar.infra.runner.mq.rocket.client;

import java.util.Map;
import java.util.Properties;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.MessageBuilder;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.shaded.com.google.common.collect.Maps;

import com.yuckar.infra.base.json.ConfigUtils;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.utils.RunUtils;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.context.YconfsFactory;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.runner.mq.ITopic;

public class RocketProducerClient {

	private static final ClientServiceProvider provider = ClientServiceProvider.loadService();
	private static final Map<ITopic, LazySupplier<LazySupplier<Producer>>> producers = Maps.newConcurrentMap();

	public static SendReceipt send(ITopic topic, MessageBuilder builder) {
		return RunUtils.throwing(() -> producers.computeIfAbsent(topic, k -> LazySupplier.wrap(() -> {
			Yconfs<Properties> yconfs = YconfsFactory.getContext(topic.getClass()).getYconfs(Properties.class);
			String path = YconfsNamespaceUtils.rocket(topic.topic() + "/producer");
			LazySupplier<Producer> producer = LazySupplier
					.wrap(() -> RunUtils.throwing(() -> provider.newProducerBuilder().setTopics(topic.topic())
							.setClientConfiguration(
									ConfigUtils.config(ClientConfiguration.newBuilder(), yconfs.get(path)).build())
							.build()));
			yconfs.addListener(path, event -> {
				producer.refresh(p -> p.close());
			});
			return producer;
		})).get().get().send(builder.setTopic(topic.topic()).build()));
	}

}

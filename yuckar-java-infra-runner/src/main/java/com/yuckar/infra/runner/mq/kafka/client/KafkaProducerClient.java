package com.yuckar.infra.runner.mq.kafka.client;

import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.rocketmq.shaded.com.google.common.collect.Maps;

import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.context.YconfsFactory;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.runner.mq.ITopic;

@SuppressWarnings("unchecked")
public class KafkaProducerClient {

	private static final Map<ITopic, LazySupplier<LazySupplier<KafkaProducer<?, ?>>>> producers = Maps
			.newConcurrentMap();

	public static <K, V> Future<RecordMetadata> send(ITopic topic, K key, V value) {
		return ((KafkaProducer<K, V>) producers.computeIfAbsent(topic, k -> LazySupplier.wrap(() -> {
			Yconfs<Properties> yconfs = YconfsFactory.getContext(topic.getClass()).getYconfs(Properties.class);
			String path = YconfsNamespaceUtils.kafka(topic.topic() + "/producer");
			LazySupplier<KafkaProducer<?, ?>> producer = LazySupplier
					.wrap(() -> new KafkaProducer<K, V>(yconfs.get(path)));
			yconfs.addListener(path, event -> {
				producer.refresh(p -> p.close(Duration.ofMinutes(1)));
			});
			return producer;
		})).get().get()).send(new ProducerRecord<K, V>(topic.topic(), key, value));
	}

}

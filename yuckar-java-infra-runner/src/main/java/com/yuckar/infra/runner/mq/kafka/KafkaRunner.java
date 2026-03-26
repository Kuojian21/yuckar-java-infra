package com.yuckar.infra.runner.mq.kafka;

import com.yuckar.infra.runner.mq.MQRunner;

public interface KafkaRunner<K, V> extends MQRunner {

	void handle(K key, V value);

}

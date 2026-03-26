package com.yuckar.infra.runner.mq.rocket;

import org.apache.rocketmq.client.apis.message.MessageView;

import com.yuckar.infra.runner.mq.MQRunner;

public interface RocketRunner extends MQRunner {

	void handle(MessageView message);

	String tag();

}

//package com.yuckar.infra.buffer.legacy;
//
//import org.slf4j.Logger;
//
//import com.yuckar.infra.buffer.legacy.GenericBatchConsumerTriggerBuilder;
//import com.yuckar.infra.buffer.legacy.GenericSimpleBufferTriggerBuilder;
//import com.yuckar.infra.common.logger.LoggerUtils;
//
//public class BufferTriggerBuilder<E> {
//
//	public static final Logger logger = LoggerUtils.logger(BufferTriggerBuilder.class);
//
//	public static <E, C> GenericSimpleBufferTriggerBuilder<E, C> simple() {
//		return new GenericSimpleBufferTriggerBuilder<>();
//	}
//
//	public static <E> GenericBatchConsumerTriggerBuilder<E> batchBlocking() {
//		return new GenericBatchConsumerTriggerBuilder<>();
//	}
//
//}

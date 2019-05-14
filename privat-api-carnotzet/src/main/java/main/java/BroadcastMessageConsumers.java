package main.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BroadcastMessageConsumers {

	private static Logger logger = LoggerFactory.getLogger(PrivatbankApplication.class);

	@RabbitListener(queues = {PrivatbankApplication.queueName})
	public void receiveMessageFromFanout1(String message) {
		logger.info(message);
	}
}
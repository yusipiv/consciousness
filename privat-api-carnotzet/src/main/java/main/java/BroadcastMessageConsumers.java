package main.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import main.java.requester.Rate;

@EnableRabbit
@Component
public class BroadcastMessageConsumers {

	private static Logger logger = LoggerFactory.getLogger(BroadcastMessageConsumers.class);

	@RabbitListener(queues = {PrivatbankApplication.queueName})
	public void receiveMessage(Rate message) {
		logger.info("->> {}", message);
	}
}
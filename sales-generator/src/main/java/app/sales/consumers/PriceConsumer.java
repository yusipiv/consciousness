package app.sales.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.consciousness.me.Rate;

import app.GeneratorApplication;

@EnableRabbit
@Component
public class PriceConsumer {

	private static final Logger logger = LoggerFactory.getLogger(PriceConsumer.class);

	@RabbitListener(queues = {GeneratorApplication.queueName})
	public void receiveMessage(Rate message) {
		logger.info("received rates: > {}", message);
	}
}

package app.queue;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import app.requester.RatesRequester;

@Component
public class RabbitQueueUpdater {

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitQueueUpdater.class);

	private final String topicName;
	private final String routingKey;
	private final RabbitTemplate rabbitTemplate;
	private final RatesRequester ratesRequester;

	public RabbitQueueUpdater(@Value("${exchange.rates.topicName}") String topicName,
			@Value("${exchange.rates.routingKey}") String routingKey, RabbitTemplate rabbitTemplate, RatesRequester ratesRequester) {
		this.topicName = topicName;
		this.routingKey = routingKey;
		this.rabbitTemplate = rabbitTemplate;
		this.ratesRequester = ratesRequester;
	}

	void subscribeAndPush(Duration duration) {
		ratesRequester.getRates(duration)
				.subscribe(data -> {
					LOGGER.debug("send prices {}", data);
					rabbitTemplate.convertAndSend(topicName, routingKey, data);
				}
		);
	}
}

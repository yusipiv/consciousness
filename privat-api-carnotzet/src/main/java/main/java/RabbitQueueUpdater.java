package main.java;

import java.time.Duration;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import main.java.requester.RatesRequester;

@Component
public class RabbitQueueUpdater {

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

	public void subscribeAndPush(Duration duration) {
		ratesRequester.getRates(duration)
				.subscribe(data ->
					rabbitTemplate.convertAndSend(topicName, routingKey, data)
		);
	}
}

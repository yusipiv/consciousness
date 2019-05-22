package app;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PrivatbankPricesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrivatbankPricesApplication.class, args);
	}

	@Bean
	public Queue ratesQueue(@Value("${exchange.rates.queueName}") String queueName) {
		return new Queue(queueName, true);
	}

	@Bean
	public Queue pricesQueue(@Value("${exchange.prices.queueName}") String queueName) {
		return new Queue(queueName, true);
	}

	@Bean
	public TopicExchange ratesExchange(@Value("${exchange.rates.topicName}") String topicName) {
		return new TopicExchange(topicName);
	}

	@Bean
	public TopicExchange pricesExchange(@Value("${exchange.prices.topicName}") String topicName) {
		return new TopicExchange(topicName);
	}

	@Bean
	public List<Binding> binding(Queue ratesQueue, Queue pricesQueue,
			TopicExchange ratesExchange, @Value("${exchange.rates.routingKey}") String ratesRoutingKey,
			TopicExchange pricesExchange, @Value("${exchange.prices.routingKey}") String pricesRoutingKey) {

		return Arrays.asList(
				BindingBuilder.bind(ratesQueue).to(ratesExchange).with(ratesRoutingKey),
				BindingBuilder.bind(pricesQueue).to(pricesExchange).with(pricesRoutingKey)
		);
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}

package app;

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

	static final String queueName = "spring-boot";

	public static void main(String[] args) {
		SpringApplication.run(PrivatbankPricesApplication.class, args);
	}

	@Bean
	public Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	public TopicExchange exchange(@Value("${exchange.rates.topicName}") String topicName) {
		return new TopicExchange(topicName);
	}

	@Bean
	public Binding binding(Queue queue, TopicExchange exchange, @Value("${exchange.rates.routingKey}") String routingKey) {
		return BindingBuilder.bind(queue).to(exchange).with("rates.exchange.#");
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}

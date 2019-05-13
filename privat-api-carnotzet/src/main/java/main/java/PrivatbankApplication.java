package main.java;

import main.java.requester.RatesRequester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class PrivatbankApplication {

	static Logger logger = LoggerFactory.getLogger(PrivatbankApplication.class);


	public static final String topicExchangeName = "spring-boot-exchange";

	static final String queueName = "spring-boot";

	@Bean
	public Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(topicExchangeName);
	}

	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
	}

	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
											 MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	public MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Resource
	main.java.requester.RatesRequester ratesRequester;
	public static void main(String[] args) {

		/*final Flux<String> stream = WebClient
				.create("http://emojitrack-gostreamer.herokuapp.com")
				.get().uri("/subscribe/eps")
				.retrieve()
				.bodyToFlux(ServerSentEvent.class)
				.flatMap(e -> Mono.justOrEmpty(e.data()))
				.map(x -> (Map<String, Integer>) x)
				.flatMapIterable(Map::entrySet)
				.flatMap(entry -> Flux.just(entry.getKey()).repeat(entry.getValue()));

		stream.subscribe(sse -> logger.info("Received: {}", sse));

		TimeUnit.SECONDS.sleep(10);*/

		SpringApplication.run(PrivatbankApplication.class, args);
	}
}

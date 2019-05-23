package app.sales.consumers;

import static app.config.SpringExtension.SPRING_EXTENSION_PROVIDER;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.consciousness.me.Rate;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

@EnableRabbit
@Component
public class PricesConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(PricesConsumer.class);
	private final ActorSystem system;
	private ActorRef midValueCalcActor;

	public PricesConsumer(ActorSystem instance) {
		this.system = instance;
	}

	@PostConstruct
	public void init() {
		midValueCalcActor = system.actorOf(SPRING_EXTENSION_PROVIDER.get(system)
				.props("midValueCalcActor"), "midValueCalcActor");
	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "${exchange.prices.queueName}", durable = "true"),
			exchange = @Exchange(name = "${exchange.prices.topicName}", type = "topic"),
			key = "${exchange.prices.routingKey}"))
	public void receiveMessage(Rate rate) {
		LOGGER.info("received prices: ->> {}", rate);
		midValueCalcActor.tell(rate, ActorRef.noSender());
	}
}

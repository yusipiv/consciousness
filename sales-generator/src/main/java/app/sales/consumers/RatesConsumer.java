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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.consciousness.me.Rate;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import app.GeneratorApplication;

@EnableRabbit
@Component
public class RatesConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(RatesConsumer.class);
	private final ActorSystem system;
	private final SimpMessagingTemplate template;
	private ActorRef historyActor;

	public RatesConsumer(ActorSystem instance, SimpMessagingTemplate template) {
		this.system = instance;
		this.template = template;
	}

	@PostConstruct
	public void init() {
		historyActor = system.actorOf(SPRING_EXTENSION_PROVIDER.get(system)
				.props("historyActor"), "historyActor");
	}

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "${exchange.rates.queueName}", durable = "true"),
			exchange = @Exchange(name = "${exchange.rates.topicName}", type = "topic"),
			key = "${exchange.rates.routingKey}"))
	public void receiveMessage(Rate rate) {
		LOGGER.info("received rates: ->> {}", rate);

		template.convertAndSend("/topic/prices", rate);
		historyActor.tell(rate, ActorRef.noSender());
	}
}

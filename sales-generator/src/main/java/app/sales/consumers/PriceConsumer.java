package app.sales.consumers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import app.GeneratorApplication;
import app.prices.PricesGeneratorService;
import com.consciousness.me.Rate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static app.config.SpringExtension.SPRING_EXTENSION_PROVIDER;

@EnableRabbit
@Component
public class PriceConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceConsumer.class);
    private final PricesGeneratorService pricesGeneratorService;
    private final ActorSystem system;
    private final SimpMessagingTemplate template;


    private ActorRef historyActor;

    public PriceConsumer(PricesGeneratorService pricesGeneratorService, ActorSystem instance,
            SimpMessagingTemplate template) {
        this.pricesGeneratorService = pricesGeneratorService;
        this.system = instance;
        this.template = template;
    }

    @PostConstruct
    public void init() {
        historyActor = system.actorOf(SPRING_EXTENSION_PROVIDER.get(system)
                .props("historyActor"), "historyActor");
    }

    @RabbitListener(queues = {GeneratorApplication.queueName})
    public void receiveMessage(Rate rate) {
        LOGGER.info("received rates: ->> {}", rate);
        // pricesGeneratorService.updatePrices(rate);

        template.convertAndSend("/topic/prices", rate);
        historyActor.tell(rate, ActorRef.noSender());
    }
}

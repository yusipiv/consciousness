package app.actors;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.consciousness.me.Rate;

import akka.actor.AbstractActor;
import app.prices.PricesGeneratorService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MidValueCalcActor extends AbstractActor {

	private final AtomicBoolean sentOnce = new AtomicBoolean();
	private final SimpMessagingTemplate template;
	private final PricesGeneratorService pricesGeneratorService;

	public MidValueCalcActor(SimpMessagingTemplate template, PricesGeneratorService pricesGeneratorService) {
		this.template = template;
		this.pricesGeneratorService = pricesGeneratorService;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Rate.class, this::calcAndSendPrices)
				.build();
	}

	private void calcAndSendPrices(Rate bankRate) {
		pricesGeneratorService.updatePrices(bankRate);
		if (!sentOnce.get()
				|| pricesGeneratorService.hasChanges()) {
			sentOnce.set(true);
			template.convertAndSend("/topic/midValue", pricesGeneratorService.getLastPrices());
		}
	}
}

package app.prices;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.consciousness.me.Rate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Component
public class PriceChangeSimulator {

	private final RabbitTemplate pricesQueue;
	private final List<SimulateRestrictions> restrictions = new ArrayList<>();
	private final ThreadLocalRandom r = ThreadLocalRandom.current();

	private final String exchangeName ;
	private final String routingKey ;

	public PriceChangeSimulator(RabbitTemplate pricesQueue,
			@Value("${exchange.prices.topicName}") String exchangeName, @Value("${exchange.prices.routingKey}") String routingKey) {

		this.pricesQueue = pricesQueue;
		this.exchangeName = exchangeName;
		this.routingKey = routingKey;
		restrictions.add(new SimulateRestrictions("BTC/USD", 7450, 8600));
		restrictions.add(new SimulateRestrictions("RUR/UAH", 0.30, 0.50));
		restrictions.add(new SimulateRestrictions("USD/UAH", 25.5, 28.0));
		restrictions.add(new SimulateRestrictions("EUR/UAH", 28.0, 31.0));
	}

	@PostConstruct
	public void start() {
		CompletableFuture.runAsync(()-> {
			while (!Thread.currentThread().isInterrupted()) {

				SimulateRestrictions restr = restrictions.get(r.nextInt(0, 4));

				Rate rate = new Rate();
				String[] currency = restr.getName().split("/");
				rate.setBase(currency[0]);
				rate.setTerm(currency[1]);
				rate.setBid(BigDecimal.valueOf(r.nextDouble(restr.getMinValue(), restr.getMaxValue())));
				rate.setAsk(BigDecimal.valueOf(r.nextDouble(rate.getBid().doubleValue(), restr.getMaxValue())));

				pricesQueue.convertAndSend(exchangeName, routingKey, rate);

				sleepALittle();
			}
		});
	}

	private void sleepALittle() {
		try {
			TimeUnit.MILLISECONDS.sleep(700);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Getter
	@AllArgsConstructor
	private static class SimulateRestrictions {
		private String name;
		private double minValue;
		private double maxValue;
	}
}

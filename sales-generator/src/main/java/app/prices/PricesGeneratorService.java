package app.prices;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.consciousness.me.PriceMovement;
import com.consciousness.me.Rate;

@Component
public class PricesGeneratorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PricesGeneratorService.class);
	private final Map<String, PriceMovement> lastPrices = new ConcurrentHashMap<>();

	private final String separatorChar;
	private final MarketDepthCalculator marketDepthCalculator;
	private final AtomicBoolean hasChanges = new AtomicBoolean();

	public PricesGeneratorService(@Value("${instrument.separatorChar}") String separatorChar,
			MarketDepthCalculator marketDepthCalculator) {
		this.separatorChar = separatorChar;
		this.marketDepthCalculator = marketDepthCalculator;
	}

	public void updatePrices(Rate bankRate) {
		lastPrices.compute(String.join(separatorChar, bankRate.getTerm(), bankRate.getBase()),
				(instrument, oldValue) -> calcMidValue(instrument, bankRate, oldValue));

		LOGGER.info("Last prices: {}", lastPrices);
	}

	private PriceMovement calcMidValue(String instrument, Rate rate, PriceMovement currentPriceMovement) {
		PriceMovement priceMovement = marketDepthCalculator.apply(instrument, rate, currentPriceMovement);

		if (!priceMovement.getAsk().getGrowthAbsolute().equals(BigDecimal.ZERO)
				|| !priceMovement.getBid().getGrowthAbsolute().equals(BigDecimal.ZERO)) {
			this.hasChanges.set(true);
		}

		return priceMovement;
	}

	public Map<String, PriceMovement> getLastPrices() {
		return lastPrices;
	}

	public boolean hasChanges() {
		return hasChanges.get();
	}
}

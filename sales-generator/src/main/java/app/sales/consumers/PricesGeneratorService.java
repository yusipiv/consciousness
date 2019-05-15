package app.sales.consumers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.consciousness.me.Rate;

@Component
public class PricesGeneratorService {

	private final Map<String, Price> lastPrices = new ConcurrentHashMap<>();

	private final String separatorChar;
	private final MarketDepthCalculator marketDepthCalculator;

	public PricesGeneratorService(@Value("${instrument.separatorChar}") String separatorChar,
			MarketDepthCalculator marketDepthCalculator) {
		this.separatorChar = separatorChar;
		this.marketDepthCalculator = marketDepthCalculator;
	}

	public void updatePrices(Rate bankRates) {
		lastPrices.compute(
				String.join(separatorChar, bankRates.getBase(),bankRates.getTerm()), marketDepthCalculator);
	}
}

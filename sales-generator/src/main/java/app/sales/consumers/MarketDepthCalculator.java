package app.sales.consumers;

import java.util.function.BiFunction;

import org.springframework.stereotype.Component;

/**
 * Market depth calculator
 * Based on BiFunction for instrument prices holder
 */
@Component
public class MarketDepthCalculator implements BiFunction<String, Price, Price> {

	@Override
	public Price apply(String s, Price oldPrice) {


		return null;
	}


}

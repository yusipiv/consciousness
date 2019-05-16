package app.sales.consumers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class MidValueCalculatorImpl implements MidValueCalculator {

	private final MathContext roundingContext;
	private final Map<String, MidRateCalculatingStrategy> meanValue = new ConcurrentHashMap<>();

	public MidValueCalculatorImpl(MathContext mathContext) {
		this.roundingContext = mathContext;
	}

	@Override
	public BigDecimal calculateMid(String instrument, BigDecimal value, BigDecimal value2) {
		meanValue.computeIfAbsent(instrument, k -> new MidRateCalculatingStrategy(roundingContext));
		meanValue.computeIfPresent(instrument, (k, p) -> p.mutuallyUpdateMeanValue(value, value2));

		return meanValue.get(instrument).getMeanValue();
	}

	private static class MidRateCalculatingStrategy {

		private static final BigDecimal TWO = BigDecimal.valueOf(2);
		private final MathContext roundingContext;
		private final AtomicInteger counter = new AtomicInteger();
		private BigDecimal meanValue = BigDecimal.ZERO;

		private MidRateCalculatingStrategy(MathContext roundingContext) {
			this.roundingContext = roundingContext;
		}

		MidRateCalculatingStrategy mutuallyUpdateMeanValue(BigDecimal value, BigDecimal value2){
			meanValue = value.add(value2).divide(TWO, roundingContext)
					.subtract(meanValue)
					.divide(BigDecimal.valueOf(counter.incrementAndGet()), roundingContext)
					.add(meanValue);
			return this;
		}

		BigDecimal getMeanValue() {
			return meanValue;
		}

		@Override
		public String toString() {
			return "mean=" + meanValue;
		}
	}
}

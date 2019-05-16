package app.sales.consumers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import javafx.util.Pair;

@Component
public class MidValueCalculatorImpl implements MidValueCalculator {

	private final MathContext roundingContext;
	private final Map<String, Pair<AtomicInteger, BigDecimal>> meanValue = new ConcurrentHashMap<>();

	public MidValueCalculatorImpl(MathContext mathContext) {
		this.roundingContext = mathContext;
	}

	@Override
	public BigDecimal calculateMid(String instrument, BigDecimal value, BigDecimal value2) {
		meanValue.computeIfAbsent(instrument, k -> new Pair<>(new AtomicInteger(), BigDecimal.ZERO));

		meanValue.computeIfPresent(instrument, (k, p) ->
				new Pair(p.getKey(),
						value.add(value2).divide(BigDecimal.valueOf(2), roundingContext)
								.subtract(p.getValue())
								.divide(BigDecimal.valueOf(p.getKey().incrementAndGet()), roundingContext)
								.add(p.getValue())));

		return meanValue.get(instrument).getValue();
	}
}

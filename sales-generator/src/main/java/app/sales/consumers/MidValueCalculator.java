package app.sales.consumers;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface MidValueCalculator {
	default BigDecimal calculateMid(String instrument, BigDecimal oldValue, BigDecimal newValue) {
		return oldValue.add(newValue).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
	}
}

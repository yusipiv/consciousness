package app.sales.consumers;

import java.math.BigDecimal;

public interface MidValueCalculator {

	default BigDecimal calculateMid (BigDecimal oldValue, BigDecimal newValue) {
		return newValue;
	}
}

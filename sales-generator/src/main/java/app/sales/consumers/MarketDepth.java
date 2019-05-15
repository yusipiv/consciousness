package app.sales.consumers;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MarketDepth {
	
	private BigDecimal rate;
	private BigDecimal growthAbsolute;
	private BigDecimal growthPercent;

}

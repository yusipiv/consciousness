package app.sales.consumers;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Price {

	private MarketDepth ask;
	private MarketDepth bid;
	private MarketDepth middle;
}

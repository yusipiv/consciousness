package app.sales.consumers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionDetails {
	private LocalDateTime date;
	private BigDecimal amount;
	private String instrument;
	private BigDecimal midRate;
	private BigDecimal askRate;
	private BigDecimal bidRate;
}

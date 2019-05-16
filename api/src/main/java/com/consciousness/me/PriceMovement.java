package com.consciousness.me;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PriceMovement {
	private MarketDepth ask;
	private MarketDepth bid;
	private MarketDepth middle;
}

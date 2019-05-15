package com.consciousness.me;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Rate {
	@JsonProperty("ccy")
	private String term;
	@JsonProperty("base_ccy")
	private String base;
	@JsonProperty("buy")
	private BigDecimal ask;
	@JsonProperty("sale")
	private BigDecimal bid;
}
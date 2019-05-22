package com.consciousness.me;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Rate implements Serializable {

	private static final long serialVersionUID = -8707389106076291951L;

	@JsonProperty("ccy")
	private String term;
	@JsonProperty("base_ccy")
	private String base;
	@JsonProperty("buy")
	private BigDecimal ask;
	@JsonProperty("sale")
	private BigDecimal bid;
}
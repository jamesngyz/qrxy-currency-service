package com.jamesngyz.qrxy.currencyservice.currency;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CurrencyRequest {
	
	@JsonProperty("code")
	private String code;
	
	@JsonProperty("name")
	private String name;
	
}

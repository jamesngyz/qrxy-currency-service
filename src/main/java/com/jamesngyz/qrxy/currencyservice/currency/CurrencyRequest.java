package com.jamesngyz.qrxy.currencyservice.currency;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CurrencyRequest {
	
	@JsonProperty("code")
	@Size(min = 3, max = 3)
	private String code;
	
	@JsonProperty("name")
	private String name;
	
}

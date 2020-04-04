package com.jamesngyz.qrxy.currencyservice.currency;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
class CurrencyRequest {
	
	@JsonProperty("code")
	@Size(min = 3, max = 3)
	@Pattern(regexp = "^[A-Z]*$")
	private String code;
	
	@JsonProperty("name")
	private String name;
	
}

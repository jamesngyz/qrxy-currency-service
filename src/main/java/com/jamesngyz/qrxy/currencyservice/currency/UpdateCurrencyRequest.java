package com.jamesngyz.qrxy.currencyservice.currency;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
class UpdateCurrencyRequest {
	
	@JsonProperty("code")
	@Size(min = 3, max = 3)
	@Pattern(regexp = "^[A-Z]*$")
	private String code;
	
	@JsonProperty("name")
	@Size(min = 1, max = 80)
	@Pattern(regexp = "^(?=\\s*\\S).*$")
	private String name;
	
}

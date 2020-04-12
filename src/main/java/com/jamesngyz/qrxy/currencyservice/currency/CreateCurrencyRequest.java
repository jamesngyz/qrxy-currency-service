package com.jamesngyz.qrxy.currencyservice.currency;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
class CreateCurrencyRequest {
	
	@JsonProperty("code")
	@NotNull
	@Size(min = 3, max = 3)
	@Pattern(regexp = "^[A-Z]*$")
	private String code;
	
	@JsonProperty("name")
	@NotBlank
	@Size(min = 1, max = 80)
	private String name;
	
}

package com.jamesngyz.qrxy.currencyservice.currency;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CurrencyResponse {
	
	@JsonProperty("id")
	private UUID id;
	
	@JsonProperty("code")
	private String code;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("status")
	private Currency.Status status;
	
	@JsonProperty("created_at")
	private Date createdAt;
	
	@JsonProperty("created_by")
	private String createdBy;
	
	@JsonProperty("updated_at")
	private Date updatedAt;
	
	@JsonProperty("updated_by")
	private String updatedBy;
	
	@JsonProperty("version")
	private Integer version;
	
}

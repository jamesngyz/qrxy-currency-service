package com.jamesngyz.qrxy.currencyservice.currency;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Currency {
	
	@Id
	private UUID id;
	
	private String code;
	
	private String name;
	
	private Status status;
	
	private Date createdAt;
	
	private String createdBy;
	
	private Date updatedAt;
	
	private String updatedBy;
	
	private Integer version;
	
	public enum Status {
		ACTIVE, INACTIVE
	}
	
}

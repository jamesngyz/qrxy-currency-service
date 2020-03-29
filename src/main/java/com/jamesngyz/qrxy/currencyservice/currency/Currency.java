package com.jamesngyz.qrxy.currencyservice.currency;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Currency {
	
	@Id
	private UUID id;
	
	private String code;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@CreatedDate
	private Date createdAt;
	
	@CreatedBy
	private String createdBy;
	
	@LastModifiedDate
	private Date updatedAt;
	
	@LastModifiedBy
	private String updatedBy;
	
	@Version
	private Integer version;
	
	public enum Status {
		ACTIVE, INACTIVE
	}
	
}

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

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "currency")
@Data
public class Currency {
	
	@Column(name = "id")
	@Id
	private UUID id;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(name = "created_at")
	@CreatedDate
	private Date createdAt;
	
	@Column(name = "created_by")
	@CreatedBy
	private String createdBy;
	
	@Column(name = "updated_at")
	@LastModifiedDate
	private Date updatedAt;
	
	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;
	
	@Column(name = "version")
	@Version
	private Integer version;
	
	public enum Status {
		ACTIVE, INACTIVE
	}
	
}

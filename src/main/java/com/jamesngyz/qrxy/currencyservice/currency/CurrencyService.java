package com.jamesngyz.qrxy.currencyservice.currency;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
	
	private final CurrencyRepository repository;
	
	@Autowired
	public CurrencyService(CurrencyRepository repository) {
		this.repository = repository;
	}
	
	public Currency createCurrency(Currency currency) {
		currency.setId(UUID.randomUUID());
		return repository.save(currency);
	}
	
}

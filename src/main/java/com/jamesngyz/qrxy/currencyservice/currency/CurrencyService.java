package com.jamesngyz.qrxy.currencyservice.currency;

import java.util.List;
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
	
	Currency createCurrency(Currency currency) {
		currency.setId(UUID.randomUUID());
		currency.setStatus(Currency.Status.ACTIVE);
		return repository.save(currency);
	}
	
	List<Currency> getCurrencies() {
		return repository.findAll();
	}
	
	Currency updateCurrency(UUID id, UpdateCurrencyRequest request) {
		Currency currency = repository.findById(id).get();
		currency.setCode(request.getCode());
		currency.setName(request.getName());
		return repository.save(currency);
	}
	
}

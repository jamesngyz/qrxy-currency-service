package com.jamesngyz.qrxy.currencyservice.currency;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
		Currency currency = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		Optional.ofNullable(request.getCode()).ifPresent(currency::setCode);
		Optional.ofNullable(request.getName()).ifPresent(currency::setName);
		return repository.save(currency);
	}
	
	Currency updateCurrency(UUID id, Currency.Status status) {
		Currency currency = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		currency.setStatus(status);
		return repository.save(currency);
	}
	
}

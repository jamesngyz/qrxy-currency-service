package com.jamesngyz.qrxy.currencyservice.currency;

import java.net.URI;

import org.mapstruct.factory.Mappers;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyController {
	
	private final CurrencyService service;
	
	private final CurrencyDtoMapper currencyDtoMapper = Mappers.getMapper(CurrencyDtoMapper.class);
	
	public CurrencyController(CurrencyService service) {
		this.service = service;
	}
	
	@PostMapping(path = "/v1/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CurrencyResponse> createCurrency(@RequestBody CurrencyRequest request) {
		
		Currency currency = currencyDtoMapper.requestToCurrency(request);
		Currency createdCurrency = service.createCurrency(currency);
		
		CurrencyResponse response = currencyDtoMapper.currencyToResponse(createdCurrency);
		URI location = URI.create(createdCurrency.getId().toString());
		
		return ResponseEntity.created(location).body(response);
	}
	
}

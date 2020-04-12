package com.jamesngyz.qrxy.currencyservice.currency;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.mapstruct.factory.Mappers;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CurrencyController {
	
	private final CurrencyService service;
	
	private final CurrencyDtoMapper currencyDtoMapper = Mappers.getMapper(CurrencyDtoMapper.class);
	
	public CurrencyController(CurrencyService service) {
		this.service = service;
	}
	
	@PostMapping(path = "/v1/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CurrencyResponse> createCurrency(@RequestBody @Valid CreateCurrencyRequest request) {
		
		Currency currency = currencyDtoMapper.requestToCurrency(request);
		Currency createdCurrency = service.createCurrency(currency);
		
		CurrencyResponse response = currencyDtoMapper.currencyToResponse(createdCurrency);
		URI location = URI.create(createdCurrency.getId().toString());
		return ResponseEntity.created(location).body(response);
	}
	
	@GetMapping(path = "/v1/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CurrencyResponse>> getCurrencies() {
		List<Currency> currencies = service.getCurrencies();
		List<CurrencyResponse> currencyResponses = currencyDtoMapper.currenciesToResponses(currencies);
		return ResponseEntity.ok(currencyResponses);
	}
	
	@PatchMapping(path = "/v1/currencies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CurrencyResponse> updateCurrency(
			@PathVariable(name = "id") UUID id,
			@RequestBody UpdateCurrencyRequest request) {
		Currency currency = service.updateCurrency(id, request);
		CurrencyResponse response = currencyDtoMapper.currencyToResponse(currency);
		return ResponseEntity.ok(response);
	}
	
}

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
	
	private final CurrencyDtoMapper currencyDtoMapper = Mappers.getMapper(CurrencyDtoMapper.class);
	
	@PostMapping(path = "/v1/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CurrencyResponse> createCurrency(@RequestBody CurrencyRequest request) {
		
		Currency currency = currencyDtoMapper.requestToCurrency(request);
		CurrencyResponse response = currencyDtoMapper.currencyToResponse(currency);
		
		return ResponseEntity.created(URI.create("")).body(response);
	}
	
}

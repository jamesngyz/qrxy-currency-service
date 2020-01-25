package com.jamesngyz.qrxy.currencyservice.currency;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyController {
	
	@PostMapping(path = "/v1/currencies")
	public ResponseEntity createCurrency() {
		return ResponseEntity.created(URI.create("")).build();
	}
}

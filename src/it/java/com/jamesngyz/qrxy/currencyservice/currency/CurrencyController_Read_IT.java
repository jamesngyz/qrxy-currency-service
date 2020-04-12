package com.jamesngyz.qrxy.currencyservice.currency;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyController_Read_IT {
	
	private final TestRestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	private final CurrencyRepository repository;
	
	private final Faker faker = new Faker();
	
	@Autowired
	public CurrencyController_Read_IT(
			TestRestTemplate restTemplate,
			ObjectMapper objectMapper,
			CurrencyRepository repository) {
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
		this.repository = repository;
	}
	
	@BeforeEach
	void deleteAllCurrencies() {
		repository.deleteAll();
	}
	
	@Test
	void readCurrencies_Status200WithBody() throws JsonProcessingException {
		CreateCurrencyRequest request = FakeCurrency.Request.build();
		restTemplate.postForEntity("/v1/currencies", request, String.class);
		
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/currencies", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotBlank();
		List<CurrencyResponse> currencyResponses = objectMapper.readValue(
				response.getBody(),
				new TypeReference<List<CurrencyResponse>>() {
				});
		assertThat(currencyResponses).anyMatch(codeAndNameEqualTo(request));
	}
	
	private Predicate<CurrencyResponse> codeAndNameEqualTo(CreateCurrencyRequest request) {
		return response -> response.getCode().equals(request.getCode())
				&& response.getName().equals(request.getName());
	}
	
}

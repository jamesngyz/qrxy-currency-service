package com.jamesngyz.qrxy.currencyservice.currency;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyController_Create_IT {
	
	private final TestRestTemplate restTemplate;
	private final CurrencyRepository repository;
	private final ObjectMapper objectMapper;
	
	private final Faker faker = new Faker();
	
	@Value("${spring.jackson.date-format}")
	private String springJacksonDateFormat;
	
	@Value("${spring.jackson.time-zone}")
	private String springJacksonTimeZone;
	
	@Autowired
	public CurrencyController_Create_IT(
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
	void createCurrency_ValidRequestBody_Status201CreatedWithValidBody() throws JsonProcessingException {
		
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.build();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		assertThat(response.getBody()).isNotNull();
		
		CurrencyResponse currencyResponse = objectMapper.readValue(response.getBody(), CurrencyResponse.class);
		
		assertThat(response.getHeaders().getLocation()).hasPath(currencyResponse.getId().toString());
		assertThat(currencyResponse.getCode()).isEqualTo(request.getCode());
		assertThat(currencyResponse.getName()).isEqualTo(request.getName());
		assertThat(currencyResponse.getId()).isNotNull();
		assertThat(currencyResponse.getCreatedAt()).isNotNull();
		assertThat(currencyResponse.getUpdatedAt()).isNotNull();
		assertThat(currencyResponse.getStatus()).isEqualTo(Currency.Status.ACTIVE);
	}
	
	@Test
	void createCurrency_CodeLengthSmallerThan3_Status400WithNullBody() {
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withCodeShorterThan3();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	@Test
	void createCurrency_CodeLengthGreaterThan3_Status400WithNullBody() {
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withCodeLongerThan3();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	@Test
	void createCurrency_CodeNonAlphabetic_Status400() {
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withCodeNonAlphabetic();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	@Test
	void createCurrency_CodeNotUpperCase_Status400() {
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withCodeNotUpperCase();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	@Test
	void createCurrency_CodeNull_Status400() {
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withCodeNull();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	@Test
	void createCurrency_NameLengthSmallerThan1_Status400WithNullBody() {
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withNameShorterThan1();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	@Test
	void createCurrency_NameLengthGreaterThan80_Status400WithNullBody() {
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withNameLongerThan80();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	@Test
	void createCurrency_NameWhitespaceOnly_Status400() {
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withNameWhitespaceOnly();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	@Test
	void createCurrency_NameNull_Status400() {
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withNameNull();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
}

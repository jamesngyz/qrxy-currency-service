package com.jamesngyz.qrxy.currencyservice.currency;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.javafaker.Faker;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CurrencyController_Create_IT {
	
	private final TestRestTemplate restTemplate;
	private final Faker faker = new Faker();
	
	@Value("${spring.jackson.date-format}")
	private String springJacksonDateFormat;
	
	@Value("${spring.jackson.time-zone}")
	private String springJacksonTimeZone;
	
	@Autowired
	public CurrencyController_Create_IT(TestRestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Test
	void createCurrency_ValidRequestBody_Status201CreatedWithValidBody() throws JsonProcessingException {
		
		CurrencyRequest request = generateCreateCurrencyRequest();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		
		ObjectMapper mapper = buildObjectMapper();
		CurrencyResponse currencyResponse = mapper.readValue(response.getBody(), CurrencyResponse.class);
		
		assertThat(response.getHeaders().getLocation()).hasPath(currencyResponse.getId().toString());
		assertThat(currencyResponse.getCode()).isEqualTo(request.getCode());
		assertThat(currencyResponse.getName()).isEqualTo(request.getName());
		assertThat(currencyResponse.getId()).isNotNull();
		assertThat(currencyResponse.getCreatedAt()).isNotNull();
		assertThat(currencyResponse.getUpdatedAt()).isNotNull();
		assertThat(currencyResponse.getStatus()).isEqualTo(Currency.Status.ACTIVE);
	}
	
	private ObjectMapper buildObjectMapper() {
		DateFormat dateFormat = new SimpleDateFormat(springJacksonDateFormat);
		TimeZone timeZone = TimeZone.getTimeZone(springJacksonTimeZone);
		
		ObjectMapper objectMapper = new JsonMapper().setDateFormat(dateFormat);
		objectMapper.setTimeZone(timeZone);
		return objectMapper;
	}
	
	private CurrencyRequest generateCreateCurrencyRequest() {
		String code = generateCurrencyCode();
		String name = generateCurrencyName();
		
		CurrencyRequest request = new CurrencyRequest();
		request.setCode(code);
		request.setName(name);
		return request;
	}
	
	private String generateCurrencyCode() {
		return RandomStringUtils.randomAlphabetic(3).toUpperCase();
	}
	
	private String generateCurrencyName() {
		int wordCount = faker.number().numberBetween(1, 20);
		int maxLength = faker.number().numberBetween(1, 80);
		String sentence = faker.lorem().sentence(wordCount);
		return sentence.substring(0, Math.min(maxLength - 1, sentence.length() - 1));
	}
	
	@Test
	void createCurrency_CodeLengthSmallerThan3_Status400WithNullBody() {
		CurrencyRequest request = generateCurrencyRequestWithCodeShorterThan3();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	private CurrencyRequest generateCurrencyRequestWithCodeShorterThan3() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String code = RandomStringUtils.randomAlphabetic(0, 3).toUpperCase();
		request.setCode(code);
		return request;
	}
	
	@Test
	void createCurrency_CodeLengthGreaterThan3_Status400WithNullBody() {
		CurrencyRequest request = generateCurrencyRequestWithCodeLongerThan3();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	private CurrencyRequest generateCurrencyRequestWithCodeLongerThan3() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String code = RandomStringUtils.randomAlphabetic(4, 21).toUpperCase();
		request.setCode(code);
		return request;
	}
	
	@Test
	void createCurrency_CodeNonAlphabetic_Status400() {
		CurrencyRequest request = generateCurrencyRequestWithCodeNonAlphabetic();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	private CurrencyRequest generateCurrencyRequestWithCodeNonAlphabetic() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String code = RandomStringUtils.randomNumeric(3);
		request.setCode(code);
		return request;
	}
	
	@Test
	void createCurrency_CodeNotUpperCase_Status400() {
		CurrencyRequest request = generateCurrencyRequestWithCodeNotUpperCase();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	private CurrencyRequest generateCurrencyRequestWithCodeNotUpperCase() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String code = RandomStringUtils.randomAlphabetic(3).toLowerCase();
		request.setCode(code);
		return request;
	}
	
	@Test
	void createCurrency_CodeNull_Status400() {
		CurrencyRequest request = generateCurrencyRequestWithCodeNull();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	private CurrencyRequest generateCurrencyRequestWithCodeNull() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		request.setCode(null);
		return request;
	}
	
	@Test
	void createCurrency_NameLengthSmallerThan1_Status400WithNullBody() {
		CurrencyRequest request = generateCurrencyRequestWithNameShorterThan1();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	private CurrencyRequest generateCurrencyRequestWithNameShorterThan1() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String name = RandomStringUtils.randomAlphabetic(0, 1).toUpperCase();
		request.setName(name);
		return request;
	}
	
	@Test
	void createCurrency_NameLengthGreaterThan80_Status400WithNullBody() {
		CurrencyRequest request = generateCurrencyRequestWithNameLongerThan80();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	private CurrencyRequest generateCurrencyRequestWithNameLongerThan80() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String name = RandomStringUtils.randomAlphabetic(81, 201).toUpperCase();
		request.setName(name);
		return request;
	}
	
	@Test
	void createCurrency_NameWhitespaceOnly_Status400() {
		CurrencyRequest request = generateCurrencyRequestWithNameWhitespaceOnly();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	private CurrencyRequest generateCurrencyRequestWithNameWhitespaceOnly() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		int length = faker.number().numberBetween(1, 80);
		String name = RandomStringUtils.random(length, " ");
		request.setName(name);
		return request;
	}
	
	@Test
	void createCurrency_NameNull_Status400() {
		CurrencyRequest request = generateCurrencyRequestWithNameNull();
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/currencies", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody()).isNull();
	}
	
	private CurrencyRequest generateCurrencyRequestWithNameNull() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		request.setName(null);
		return request;
	}
	
}

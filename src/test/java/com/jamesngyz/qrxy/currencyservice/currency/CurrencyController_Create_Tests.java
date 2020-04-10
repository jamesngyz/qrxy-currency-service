package com.jamesngyz.qrxy.currencyservice.currency;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@WebMvcTest
public class CurrencyController_Create_Tests {
	
	private final MockMvc mockMvc;
	private final ObjectMapper objectMapper;
	
	private final Faker faker = new Faker();
	private final CurrencyDtoMapper currencyDtoMapper = Mappers.getMapper(CurrencyDtoMapper.class);
	
	@MockBean
	private CurrencyService currencyService;
	
	@Autowired
	public CurrencyController_Create_Tests(
			MockMvc mockMvc,
			ObjectMapper objectMapper) {
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
	}
	
	@Test
	void createCurrency_AllOk_Status201Created() throws Exception {
		
		CurrencyRequest request = generateCreateCurrencyRequest();
		String requestJson = objectMapper.writeValueAsString(request);
		Currency currency = generateCreatedCurrency(request);
		String responseJson = buildCreateCurrencyOkResponseJson(objectMapper, currency);
		
		when(currencyService.createCurrency(notNull())).thenReturn(currency);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", currency.getId().toString()))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(responseJson));
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
	
	private Currency generateCreatedCurrency(CurrencyRequest request) {
		Currency currency = currencyDtoMapper.requestToCurrency(request);
		currency.setCreatedAt(faker.date().birthday());
		currency.setCreatedBy(faker.name().firstName());
		currency.setId(UUID.randomUUID());
		currency.setStatus(Currency.Status.ACTIVE);
		currency.setUpdatedAt(currency.getCreatedAt());
		currency.setUpdatedBy(currency.getCreatedBy());
		currency.setVersion(0);
		return currency;
	}
	
	private String buildCreateCurrencyOkResponseJson(ObjectMapper objectMapper, Currency currency)
			throws JsonProcessingException {
		CurrencyResponse response = currencyDtoMapper.currencyToResponse(currency);
		return objectMapper.writeValueAsString(response);
	}
	
	@Test
	void createCurrency_CodeLengthSmallerThan3_Status400() throws Exception {
		
		CurrencyRequest request = generateCurrencyRequestWithCodeShorterThan3();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	private CurrencyRequest generateCurrencyRequestWithCodeShorterThan3() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String code = RandomStringUtils.randomAlphabetic(0, 3).toUpperCase();
		request.setCode(code);
		return request;
	}
	
	@Test
	void createCurrency_CodeLengthGreaterThan3_Status400() throws Exception {
		
		CurrencyRequest request = generateCurrencyRequestWithCodeLongerThan3();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	private CurrencyRequest generateCurrencyRequestWithCodeLongerThan3() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String code = RandomStringUtils.randomAlphabetic(4, 21).toUpperCase();
		request.setCode(code);
		return request;
	}
	
	@Test
	void createCurrency_CodeNonAlphabetic_Status400() throws Exception {
		
		CurrencyRequest request = generateCurrencyRequestWithCodeNonAlphabetic();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	private CurrencyRequest generateCurrencyRequestWithCodeNonAlphabetic() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String code = RandomStringUtils.randomNumeric(3);
		request.setCode(code);
		return request;
	}
	
	@Test
	void createCurrency_CodeNotUpperCase_Status400() throws Exception {
		
		CurrencyRequest request = generateCurrencyRequestWithCodeNotUpperCase();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	private CurrencyRequest generateCurrencyRequestWithCodeNotUpperCase() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String code = RandomStringUtils.randomAlphabetic(3).toLowerCase();
		request.setCode(code);
		return request;
	}
	
	@Test
	void createCurrency_CodeNull_Status400() throws Exception {
		
		CurrencyRequest request = generateCurrencyRequestWithCodeNull();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	private CurrencyRequest generateCurrencyRequestWithCodeNull() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		request.setCode(null);
		return request;
	}
	
	@Test
	void createCurrency_NameLengthSmallerThan1_Status400() throws Exception {
		
		CurrencyRequest request = generateCurrencyRequestWithNameShorterThan1();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	private CurrencyRequest generateCurrencyRequestWithNameShorterThan1() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String name = RandomStringUtils.randomAlphabetic(0, 1).toUpperCase();
		request.setName(name);
		return request;
	}
	
	@Test
	void createCurrency_NameLengthGreaterThan80_Status400() throws Exception {
		
		CurrencyRequest request = generateCurrencyRequestWithNameLongerThan80();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	private CurrencyRequest generateCurrencyRequestWithNameLongerThan80() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String name = RandomStringUtils.randomAlphabetic(81, 201).toUpperCase();
		request.setName(name);
		return request;
	}
	
	@Test
	void createCurrency_NameWhitespaceOnly_Status400() throws Exception {
		
		CurrencyRequest request = generateCurrencyRequestWithNameWhitespaceOnly();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	private CurrencyRequest generateCurrencyRequestWithNameWhitespaceOnly() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		int length = faker.number().numberBetween(1, 80);
		String name = RandomStringUtils.random(length, " ");
		request.setName(name);
		return request;
	}
	
	@Test
	void createCurrency_NameNull_Status400() throws Exception {
		
		CurrencyRequest request = generateCurrencyRequestWithNameNull();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	private CurrencyRequest generateCurrencyRequestWithNameNull() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		request.setName(null);
		return request;
	}
	
}

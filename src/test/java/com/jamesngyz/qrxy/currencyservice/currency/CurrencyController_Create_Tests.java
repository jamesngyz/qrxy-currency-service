package com.jamesngyz.qrxy.currencyservice.currency;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
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
		
		CreateCurrencyRequest request = FakeCurrency.Request.build();
		String requestJson = objectMapper.writeValueAsString(request);
		Currency currency = generateCreatedCurrency(request);
		String responseJson = buildCreateCurrencyOkResponseJson(currency);
		
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
	
	private Currency generateCreatedCurrency(CreateCurrencyRequest request) {
		Currency currency = FakeCurrency.fromRequest(request);
		currency.setCreatedAt(faker.date().birthday());
		currency.setCreatedBy(faker.name().firstName());
		currency.setId(UUID.randomUUID());
		currency.setStatus(Currency.Status.ACTIVE);
		currency.setUpdatedAt(currency.getCreatedAt());
		currency.setUpdatedBy(currency.getCreatedBy());
		currency.setVersion(0);
		return currency;
	}
	
	private String buildCreateCurrencyOkResponseJson(Currency currency) throws JsonProcessingException {
		CurrencyResponse response = FakeCurrency.toResponse(currency);
		return objectMapper.writeValueAsString(response);
	}
	
	@Test
	void createCurrency_CodeLengthSmallerThan3_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.Request.withCodeShorterThan3();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_CodeLengthGreaterThan3_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.Request.withCodeLongerThan3();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_CodeNonAlphabetic_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.Request.withCodeNonAlphabetic();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_CodeNotUpperCase_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.Request.withCodeNotUpperCase();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_CodeNull_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.Request.withCodeNull();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_NameLengthSmallerThan1_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.Request.withNameShorterThan1();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_NameLengthGreaterThan80_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.Request.withNameLongerThan80();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_NameWhitespaceOnly_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.Request.withNameWhitespaceOnly();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_NameNull_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.Request.withNameNull();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
}

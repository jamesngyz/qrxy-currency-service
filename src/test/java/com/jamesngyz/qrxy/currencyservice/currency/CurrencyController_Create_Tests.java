package com.jamesngyz.qrxy.currencyservice.currency;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
		
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.build();
		String requestJson = objectMapper.writeValueAsString(request);
		Currency created = FakeCurrency.fromRequestAndGeneratePersistenceFields(request);
		String expected = buildCreateCurrencyOkResponseJson(created);
		
		when(currencyService.createCurrency(notNull())).thenReturn(created);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", created.getId().toString()))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expected));
	}
	
	private String buildCreateCurrencyOkResponseJson(Currency currency) throws JsonProcessingException {
		CurrencyResponse response = FakeCurrency.Response.fromCurrency(currency);
		return objectMapper.writeValueAsString(response);
	}
	
	@Test
	void createCurrency_CodeLengthSmallerThan3_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withCodeShorterThan3();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_CodeLengthGreaterThan3_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withCodeLongerThan3();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_CodeNonAlphabetic_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withCodeNonAlphabetic();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_CodeNotUpperCase_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withCodeNotUpperCase();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_CodeNull_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withCodeNull();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_NameLengthSmallerThan1_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withNameShorterThan1();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_NameLengthGreaterThan80_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withNameLongerThan80();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_NameWhitespaceOnly_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withNameWhitespaceOnly();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void createCurrency_NameNull_Status400() throws Exception {
		
		CreateCurrencyRequest request = FakeCurrency.CreateRequest.withNameNull();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
}

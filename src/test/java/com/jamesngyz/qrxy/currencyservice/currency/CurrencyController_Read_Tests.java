package com.jamesngyz.qrxy.currencyservice.currency;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
public class CurrencyController_Read_Tests {
	
	private final MockMvc mockMvc;
	private final ObjectMapper objectMapper;
	private final Faker faker = new Faker();
	
	@MockBean
	private CurrencyService currencyService;
	
	@Autowired
	public CurrencyController_Read_Tests(
			MockMvc mockMvc,
			ObjectMapper objectMapper) {
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
	}
	
	@Test
	void readCurrencies_AllOk_Status200() throws Exception {
		
		List<Currency> currencies = generateCurrencies();
		String responsesJson = generateCurrencyResponsesJson(currencies);
		
		when(currencyService.getCurrencies()).thenReturn(currencies);
		
		mockMvc.perform(get("/v1/currencies"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(responsesJson));
	}
	
	private String generateCurrencyResponsesJson(List<Currency> currencies) throws JsonProcessingException {
		List<CurrencyResponse> responses = new ArrayList<>();
		currencies.forEach(currency -> {
			CurrencyResponse response = FakeCurrency.toResponse(currency);
			responses.add(response);
		});
		return objectMapper.writeValueAsString(responses);
	}
	
	private List<Currency> generateCurrencies() {
		List<Currency> currencies = new ArrayList<>();
		int currenciesCount = faker.number().numberBetween(1, 20);
		
		while (currenciesCount > 0) {
			currencies.add(FakeCurrency.build());
			currenciesCount--;
		}
		return currencies;
	}
	
}

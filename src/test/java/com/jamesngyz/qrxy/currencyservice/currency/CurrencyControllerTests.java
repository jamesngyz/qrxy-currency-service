package com.jamesngyz.qrxy.currencyservice.currency;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.javafaker.Faker;

@WebMvcTest
public class CurrencyControllerTests {
	
	private final MockMvc mockMvc;
	private final Faker faker = new Faker();
	private final CurrencyDtoMapper currencyDtoMapper = Mappers.getMapper(CurrencyDtoMapper.class);
	
	@Autowired
	public CurrencyControllerTests(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}
	
	@Test
	public void createCurrency_AllOk_Status201Created() throws Exception {
		
		CurrencyRequest request = new CurrencyRequest();
		request.setCode(faker.lorem().characters(1, 5).toUpperCase());
		request.setName(String.join(" ", faker.lorem().words(faker.number().numberBetween(1, 10))).toUpperCase());
		
		String requestJson = new JsonMapper().writeValueAsString(request);
		
		Currency currency = currencyDtoMapper.requestToCurrency(request);
		CurrencyResponse response = currencyDtoMapper.currencyToResponse(currency);
		String responseJson = new JsonMapper().writeValueAsString(response);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(responseJson));
	}
	
}

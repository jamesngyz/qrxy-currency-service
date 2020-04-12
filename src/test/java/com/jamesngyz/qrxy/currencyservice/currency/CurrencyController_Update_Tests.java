package com.jamesngyz.qrxy.currencyservice.currency;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
class CurrencyController_Update_Tests {
	
	private final MockMvc mockMvc;
	private final ObjectMapper objectMapper;
	
	@MockBean
	private CurrencyService currencyService;
	
	@Autowired
	CurrencyController_Update_Tests(
			MockMvc mockMvc,
			ObjectMapper objectMapper) {
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
	}
	
	@Test
	void updateCurrency_AllOk_Status200WithUpdatedBody() throws Exception {
		
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.build();
		String requestJson = objectMapper.writeValueAsString(request);
		Currency updated = FakeCurrency.fromInitialThenUpdate(FakeCurrency.build(), request);
		CurrencyResponse response = FakeCurrency.Response.fromCurrency(updated);
		String responseJson = objectMapper.writeValueAsString(response);
		
		when(currencyService.updateCurrency(updated.getId(), request)).thenReturn(updated);
		
		mockMvc.perform(
				patch("/v1/currencies/" + updated.getId().toString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(responseJson));
	}
	
	@Test
	void updateCurrency_CodeOnly_Status200WithUpdatedBody() throws Exception {
		
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.withCodeOnly();
		String requestJson = objectMapper.writeValueAsString(request);
		Currency updated = FakeCurrency.fromInitialThenUpdate(FakeCurrency.build(), request);
		CurrencyResponse expected = FakeCurrency.Response.fromCurrency(updated);
		String expectedJson = objectMapper.writeValueAsString(expected);
		UUID id = updated.getId();
		
		when(currencyService.updateCurrency(id, request)).thenReturn(updated);
		
		mockMvc.perform(
				patch("/v1/currencies/" + id.toString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expectedJson));
	}
	
	@Test
	void updateCurrency_NameOnly_Status200WithUpdatedBody() throws Exception {
		
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.withNameOnly();
		String requestJson = objectMapper.writeValueAsString(request);
		Currency updated = FakeCurrency.fromInitialThenUpdate(FakeCurrency.build(), request);
		CurrencyResponse expected = FakeCurrency.Response.fromCurrency(updated);
		String expectedJson = objectMapper.writeValueAsString(expected);
		UUID id = updated.getId();
		
		when(currencyService.updateCurrency(id, request)).thenReturn(updated);
		
		mockMvc.perform(
				patch("/v1/currencies/" + id.toString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(expectedJson));
	}
	
	@Test
	void updateCurrency_CurrencyNotFound_Status404() throws Exception {
		
		UUID id = UUID.randomUUID();
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.build();
		String requestJson = objectMapper.writeValueAsString(request);
		
		when(currencyService.updateCurrency(id, request)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		mockMvc.perform(
				patch("/v1/currencies/" + id.toString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isNotFound());
	}
	
}

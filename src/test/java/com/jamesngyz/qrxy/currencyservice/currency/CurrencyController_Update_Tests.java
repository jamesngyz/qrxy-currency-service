package com.jamesngyz.qrxy.currencyservice.currency;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
	
	@Test
	void updateCurrency_CodeShorterThan3_Status400() throws Exception {
		
		UUID id = UUID.randomUUID();
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.withCodeShorterThan3();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				patch("/v1/currencies/" + id.toString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateCurrency_CodeLonger3_Status400() throws Exception {
		
		UUID id = UUID.randomUUID();
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.withCodeLongerThan3();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				patch("/v1/currencies/" + id.toString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateCurrency_CodeNonAlphabetic_Status400() throws Exception {
		
		UUID id = UUID.randomUUID();
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.withCodeNonAlphabetic();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				patch("/v1/currencies/" + id.toString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateCurrency_CodeNotUpperCase_Status400() throws Exception {
		
		UUID id = UUID.randomUUID();
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.withCodeNotUpperCase();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				patch("/v1/currencies/" + id.toString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateCurrency_NameShorterThan1_Status400() throws Exception {
		
		UUID id = UUID.randomUUID();
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.withNameShorterThan1();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				patch("/v1/currencies/" + id.toString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateCurrency_NameLongerThan80_Status400() throws Exception {
		
		UUID id = UUID.randomUUID();
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.withNameLongerThan80();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				patch("/v1/currencies/" + id.toString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateCurrency_NameWhitespaceOnly_Status400() throws Exception {
		
		UUID id = UUID.randomUUID();
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.withNameWhitespaceOnly();
		String requestJson = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(
				patch("/v1/currencies/" + id.toString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateCurrencyStatus_AllOk_Status200() throws Exception {
		
		Currency currency = FakeCurrency.build();
		UUID id = currency.getId();
		String currencyStatus = FakeCurrency.Status.build().name();
		
		mockMvc.perform(
				put("/v1/currencies/" + id.toString() + "/status")
						.contentType(MediaType.TEXT_PLAIN)
						.content(currencyStatus))
				.andExpect(status().isNoContent());
	}
	
	@Test
	void updateCurrencyStatus_StatusNonExistent_Status400() throws Exception {
		
		UUID id = UUID.randomUUID();
		String nonExistentStatus = "abc";
		
		mockMvc.perform(
				put("/v1/currencies/" + id.toString() + "/status")
						.contentType(MediaType.TEXT_PLAIN)
						.content(nonExistentStatus))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void updateCurrencyStatus_CurrencyNotFound_Status404() throws Exception {
		
		UUID id = UUID.randomUUID();
		String currencyStatus = FakeCurrency.Status.build().name();
		
		when(currencyService.updateCurrency(notNull(), (Currency.Status) notNull()))
				.thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		mockMvc.perform(
				put("/v1/currencies/" + id.toString() + "/status")
						.contentType(MediaType.TEXT_PLAIN)
						.content(currencyStatus))
				.andExpect(status().isNotFound());
	}
}

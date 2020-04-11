package com.jamesngyz.qrxy.currencyservice.currency;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
		Currency currency = FakeCurrency.fromRequestAndGeneratePersistenceFields(request);
		CurrencyResponse response = FakeCurrency.toResponse(currency);
		String responseJson = objectMapper.writeValueAsString(response);
		
		when(currencyService.updateCurrency(currency.getId(), request)).thenReturn(currency);
		
		mockMvc.perform(
				patch("/v1/currencies/" + currency.getId().toString())
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(responseJson));
	}
	
}

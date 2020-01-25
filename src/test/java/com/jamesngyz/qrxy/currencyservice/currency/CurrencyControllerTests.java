package com.jamesngyz.qrxy.currencyservice.currency;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class CurrencyControllerTests {
	
	private final MockMvc mockMvc;
	
	@Autowired
	public CurrencyControllerTests(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}
	
	@Test
	public void createCurrency_AllOk_Status201Created() throws Exception {
		mockMvc.perform(post("/v1/currencies"))
				.andExpect(status().isCreated());
	}
	
}

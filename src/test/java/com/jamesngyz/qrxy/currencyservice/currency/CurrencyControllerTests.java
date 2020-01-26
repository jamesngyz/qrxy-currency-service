package com.jamesngyz.qrxy.currencyservice.currency;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.javafaker.Faker;

@WebMvcTest
public class CurrencyControllerTests {
	
	private final MockMvc mockMvc;
	private final Faker faker = new Faker();
	private final CurrencyDtoMapper currencyDtoMapper = Mappers.getMapper(CurrencyDtoMapper.class);
	
	@Value("${spring.jackson.date-format}")
	private String springJacksonDateFormat;
	
	@Value("${spring.jackson.time-zone}")
	private String springJacksonTimeZone;
	
	@MockBean
	private CurrencyService currencyService;
	
	@Autowired
	public CurrencyControllerTests(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}
	
	@Test
	public void createCurrency_AllOk_Status201Created() throws Exception {
		
		CurrencyRequest request = new CurrencyRequest();
		request.setCode(faker.lorem().characters(1, 5).toUpperCase());
		request.setName(String.join(" ", faker.lorem().words(faker.number().numberBetween(1, 10))).toUpperCase());
		
		DateFormat dateFormat = new SimpleDateFormat(springJacksonDateFormat);
		TimeZone timeZone = TimeZone.getTimeZone(springJacksonTimeZone);
		
		ObjectMapper jsonMapper = new JsonMapper().setDateFormat(dateFormat);
		jsonMapper.setTimeZone(timeZone);
		
		String requestJson = jsonMapper.writeValueAsString(request);
		
		Currency currency = currencyDtoMapper.requestToCurrency(request);
		currency.setCreatedAt(faker.date().birthday());
		currency.setCreatedBy(faker.name().firstName());
		currency.setId(UUID.randomUUID());
		currency.setStatus(Currency.Status.ACTIVE);
		currency.setUpdatedAt(currency.getCreatedAt());
		currency.setUpdatedBy(currency.getCreatedBy());
		currency.setVersion(0);
		
		when(currencyService.createCurrency(notNull())).thenReturn(currency);
		
		CurrencyResponse response = currencyDtoMapper.currencyToResponse(currency);
		String responseJson = jsonMapper.writeValueAsString(response);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(responseJson));
	}
	
}

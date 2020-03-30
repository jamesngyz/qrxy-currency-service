package com.jamesngyz.qrxy.currencyservice.currency;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
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

import com.fasterxml.jackson.core.JsonProcessingException;
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
		
		ObjectMapper jsonMapper = buildObjectMapper();
		
		CurrencyRequest request = generateCreateCurrencyRequest();
		String requestJson = jsonMapper.writeValueAsString(request);
		Currency currency = generateCreatedCurrency(request);
		String responseJson = buildCreateCurrencyOkResponseJson(jsonMapper, currency);
		
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
	
	private ObjectMapper buildObjectMapper() {
		DateFormat dateFormat = new SimpleDateFormat(springJacksonDateFormat);
		TimeZone timeZone = TimeZone.getTimeZone(springJacksonTimeZone);
		
		ObjectMapper objectMapper = new JsonMapper().setDateFormat(dateFormat);
		objectMapper.setTimeZone(timeZone);
		return objectMapper;
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
		return faker.lorem().characters(3).toUpperCase();
	}
	
	private String generateCurrencyName() {
		int nameWordCount = faker.number().numberBetween(1, 10);
		List<String> nameWords = faker.lorem().words(nameWordCount);
		return String.join(" ", nameWords).toUpperCase();
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
	
	private String buildCreateCurrencyOkResponseJson(ObjectMapper jsonMapper, Currency currency)
			throws JsonProcessingException {
		CurrencyResponse response = currencyDtoMapper.currencyToResponse(currency);
		return jsonMapper.writeValueAsString(response);
	}
	
	@Test
	public void createCurrency_CodeLengthSmallerThan3_Status400() throws Exception {
		ObjectMapper jsonMapper = buildObjectMapper();
		
		CurrencyRequest request = generateCurrencyRequestWithCodeShorterThan3();
		String requestJson = jsonMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	private CurrencyRequest generateCurrencyRequestWithCodeShorterThan3() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String code = generateStringShorterThan3();
		request.setCode(code);
		return request;
	}
	
	private String generateStringShorterThan3() {
		return faker.lorem().characters(0, 2).toUpperCase();
	}
	
	@Test
	public void createCurrency_CodeLengthGreaterThan3_Status400() throws Exception {
		ObjectMapper jsonMapper = buildObjectMapper();
		
		CurrencyRequest request = generateCurrencyRequestWithCodeLongerThan3();
		String requestJson = jsonMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isBadRequest());
	}
	
	private CurrencyRequest generateCurrencyRequestWithCodeLongerThan3() {
		CurrencyRequest request = generateCreateCurrencyRequest();
		String code = generateStringLongerThan3();
		request.setCode(code);
		return request;
	}
	
	private String generateStringLongerThan3() {
		return faker.lorem().characters(4, 20).toUpperCase();
	}
	
}

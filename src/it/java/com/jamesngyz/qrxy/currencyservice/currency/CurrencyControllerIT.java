package com.jamesngyz.qrxy.currencyservice.currency;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.javafaker.Faker;

@ActiveProfiles("integration-test")
@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerIT {
	
	private final MockMvc mockMvc;
	private final Faker faker = new Faker();
	
	@Value("${spring.jackson.date-format}")
	private String springJacksonDateFormat;
	
	@Value("${spring.jackson.time-zone}")
	private String springJacksonTimeZone;
	
	@Autowired
	public CurrencyControllerIT(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}
	
	@Test
	void postCurrency_ValidRequestBody_Status201Created() throws Exception {
		
		ObjectMapper jsonMapper = buildObjectMapper();
		
		CurrencyRequest request = generateCreateCurrencyRequest();
		String requestJson = jsonMapper.writeValueAsString(request);
		
		mockMvc.perform(
				post("/v1/currencies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
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
	
}

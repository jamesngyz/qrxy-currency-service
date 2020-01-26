package com.jamesngyz.qrxy.currencyservice.currency;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTests {
	
	private final Faker faker = new Faker();
	
	@InjectMocks
	CurrencyService subject;
	
	@Test
	public void createCurrency_AllOK_PersistToDatabaseAndReturn() {
		Currency currency = new Currency();
		currency.setCode(generateCurrencyCode());
		currency.setName(generateCurrencyName());
		
		Currency createdCurrency = subject.createCurrency(currency);
		assertThat(createdCurrency).isEqualTo(currency);
	}
	
	private String generateCurrencyCode() {
		return faker.lorem().characters(1, 5).toUpperCase();
	}
	
	private String generateCurrencyName() {
		int nameWordCount = faker.number().numberBetween(1, 10);
		List<String> nameWords = faker.lorem().words(nameWordCount);
		return String.join(" ", nameWords).toUpperCase();
	}
	
}

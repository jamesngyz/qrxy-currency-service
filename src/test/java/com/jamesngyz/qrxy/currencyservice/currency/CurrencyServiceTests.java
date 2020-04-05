package com.jamesngyz.qrxy.currencyservice.currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTests {
	
	private final Faker faker = new Faker();
	
	@InjectMocks
	CurrencyService subject;
	
	@Mock
	CurrencyRepository repository;
	
	@Test
	void createCurrency_AllOK_PersistToDatabaseAndReturn() {
		Currency currency = new Currency();
		currency.setCode(generateCurrencyCode());
		currency.setName(generateCurrencyName());
		
		Currency expectedCurrency = generateExpectedCurrency(currency);
		
		when(repository.save(notNull())).thenReturn(expectedCurrency);
		
		Currency result = subject.createCurrency(currency);
		assertThat(result).isEqualTo(expectedCurrency);
	}
	
	private String generateCurrencyCode() {
		return faker.lorem().characters(3).toUpperCase();
	}
	
	private String generateCurrencyName() {
		int nameWordCount = faker.number().numberBetween(1, 10);
		List<String> nameWords = faker.lorem().words(nameWordCount);
		return String.join(" ", nameWords).toUpperCase();
	}
	
	private Currency generateExpectedCurrency(Currency inputCurrency) {
		Currency expectedCurrency = new Currency();
		expectedCurrency.setCode(inputCurrency.getCode());
		expectedCurrency.setName(inputCurrency.getName());
		expectedCurrency.setCreatedAt(faker.date().birthday());
		expectedCurrency.setCreatedBy(faker.name().firstName());
		expectedCurrency.setId(UUID.randomUUID());
		expectedCurrency.setStatus(Currency.Status.ACTIVE);
		expectedCurrency.setUpdatedAt(expectedCurrency.getCreatedAt());
		expectedCurrency.setUpdatedBy(expectedCurrency.getCreatedBy());
		expectedCurrency.setVersion(0);
		return expectedCurrency;
	}
	
}

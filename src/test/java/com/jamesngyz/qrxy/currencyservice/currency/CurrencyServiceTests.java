package com.jamesngyz.qrxy.currencyservice.currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.github.javafaker.Faker;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTests {
	
	private final Faker faker = new Faker();
	
	@InjectMocks
	CurrencyService subject;
	
	@Mock
	CurrencyRepository repository;
	
	@Test
	void createCurrency_AllOk_PersistToDatabaseAndReturn() {
		
		Currency expectedCurrency = FakeCurrency.build();
		
		Currency inputCurrency = new Currency();
		inputCurrency.setCode(expectedCurrency.getCode());
		inputCurrency.setName(expectedCurrency.getName());
		
		when(repository.save(notNull())).thenReturn(expectedCurrency);
		
		Currency result = subject.createCurrency(inputCurrency);
		assertThat(result).isEqualTo(expectedCurrency);
	}
	
	@Test
	void getCurrencies_AllOk_RetrieveFromRepositoryAndReturn() {
		List<Currency> expectedCurrencies = generateExpectedCurrencies();
		when(repository.findAll()).thenReturn(expectedCurrencies);
		List<Currency> result = subject.getCurrencies();
		assertThat(result).isEqualTo(expectedCurrencies);
	}
	
	private List<Currency> generateExpectedCurrencies() {
		List<Currency> currencies = new ArrayList<>();
		int currenciesCount = faker.number().numberBetween(1, 20);
		
		while (currenciesCount > 0) {
			currencies.add(FakeCurrency.build());
			currenciesCount--;
		}
		return currencies;
	}
	
	@Test
	void updateCurrency_AllOk_FetchUpdatePersistAndReturn() {
		Currency initial = FakeCurrency.build();
		UUID id = initial.getId();
		
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.build();
		Currency expected = FakeCurrency.fromInitialThenUpdate(initial, request);
		
		when(repository.findById(id)).thenReturn(Optional.of(initial));
		when(repository.save(expected)).thenReturn(expected);
		
		Currency result = subject.updateCurrency(id, request);
		assertThat(result).isEqualTo(expected);
	}
	
	@Test
	void updateCurrency_CodeOnly_FetchUpdatePersistAndReturn() {
		Currency initial = FakeCurrency.build();
		UUID id = initial.getId();
		
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.withCodeOnly();
		Currency expected = FakeCurrency.fromInitialThenUpdate(initial, request);
		
		when(repository.findById(id)).thenReturn(Optional.of(initial));
		when(repository.save(expected)).thenReturn(expected);
		
		Currency result = subject.updateCurrency(id, request);
		assertThat(result).isEqualTo(expected);
	}
	
	@Test
	void updateCurrency_NameOnly_FetchUpdatePersistAndReturn() {
		Currency initial = FakeCurrency.build();
		UUID id = initial.getId();
		
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.withNameOnly();
		Currency expected = FakeCurrency.fromInitialThenUpdate(initial, request);
		
		when(repository.findById(id)).thenReturn(Optional.of(initial));
		when(repository.save(expected)).thenReturn(expected);
		
		Currency result = subject.updateCurrency(id, request);
		assertThat(result).isEqualTo(expected);
	}
	
	@Test
	void updateCurrency_CurrencyNotFound_ThrowResponseStatusException() {
		UUID id = UUID.randomUUID();
		UpdateCurrencyRequest request = FakeCurrency.UpdateRequest.build();
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		assertThrows(ResponseStatusException.class, () -> subject.updateCurrency(id, request));
	}
	
}

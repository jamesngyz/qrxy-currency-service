package com.jamesngyz.qrxy.currencyservice.currency;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper
public interface CurrencyDtoMapper {
	
	Currency requestToCurrency(CreateCurrencyRequest request);
	
	CurrencyResponse currencyToResponse(Currency currency);
	
	List<CurrencyResponse> currenciesToResponses(List<Currency> currencies);
	
}

package com.rdrcelic.troskovi.expenses.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdrcelic.troskovi.expenses.dto.ExpenseDto;
import com.rdrcelic.troskovi.expenses.model.TroskoviResult;

import java.util.List;

/**
 * This utility can convert TroskoviResult value to some concrete type.
 */
public class ResultConverter {

    private final ObjectMapper objectMapper;

    public ResultConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T convertValue (TroskoviResult container, String valueKey, T valueType) {
        return objectMapper.convertValue(
                container.get(0).get(valueKey),
                new TypeReference<List<ExpenseDto>>() {});
    }
}

package com.rdrcelic.troskovi.expenses.utility;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;

public class ModelMapperFactory {
    public static ModelMapper createModelMapperSkippingNullProperties() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        return modelMapper;
    }
}

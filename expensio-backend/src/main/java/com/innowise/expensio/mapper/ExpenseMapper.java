package com.innowise.expensio.mapper;

import com.innowise.expensio.dto.ExpenseDto;
import com.innowise.expensio.dto.ExpenseRequestDto;
import com.innowise.expensio.model.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExpenseMapper {

    Expense toModel(ExpenseRequestDto expenseRequestDto);

    ExpenseDto toDto(Expense expense);

}

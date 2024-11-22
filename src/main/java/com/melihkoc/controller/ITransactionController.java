package com.melihkoc.controller;

import com.melihkoc.dto.DtoTransaction;
import com.melihkoc.dto.DtoTransactionIU;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ITransactionController {

    public RootEntity<List<DtoTransaction>> getUserTransactions();

    public RootEntity<DtoTransaction> addTransaction(DtoTransactionIU dtoTransactionIU);

    public RootEntity<DtoTransaction> getTransactionById(Long transactionId);

    public RootEntity<List<DtoTransaction>> getTransactionByCategory(Long categoryId);

    public RootEntity<DtoTransaction> deleteTransaction(Long transactionId);

    public RootEntity<DtoTransaction> updateTransaction(Long transactionId,DtoTransactionIU dtoTransactionIU);

    public RootEntity<List<DtoTransaction>> getMonthlyTransactions(int year, int month);

    public RootEntity<List<DtoTransaction>> getWeeklyTransactions(Date anyDateInWeek);

    public RootEntity<List<DtoTransaction>> getTransactionsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);

}

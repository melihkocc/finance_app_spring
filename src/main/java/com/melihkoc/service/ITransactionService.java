package com.melihkoc.service;

import com.melihkoc.dto.DtoTransaction;
import com.melihkoc.dto.DtoTransactionIU;
import com.melihkoc.model.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ITransactionService {

    public List<DtoTransaction> getUserTransactions();

    public DtoTransaction addTransaction(DtoTransactionIU dtoTransactionIU);

    public DtoTransaction getTransactionById(Long transactionId);

    public List<DtoTransaction> getTransactionByCategory(Long categoryId);

    public DtoTransaction deleteTransaction(Long transactionId);

    public DtoTransaction updateTransaction(Long transactionId, DtoTransactionIU dtoTransactionIU);

    public List<DtoTransaction> getMonthlyTransactions(int year, int month);

    public List<DtoTransaction> getWeeklyTransactions(Date anyDateInWeek);

    public List<DtoTransaction> getTransactionsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);
}

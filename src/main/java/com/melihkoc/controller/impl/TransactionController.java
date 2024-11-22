package com.melihkoc.controller.impl;

import com.melihkoc.controller.ITransactionController;
import com.melihkoc.controller.RestBaseController;
import com.melihkoc.controller.RootEntity;
import com.melihkoc.dto.DtoTransaction;
import com.melihkoc.dto.DtoTransactionIU;
import com.melihkoc.exception.BaseException;
import com.melihkoc.exception.ErrorMessage;
import com.melihkoc.exception.MessageType;
import com.melihkoc.service.ITransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rest/api/transaction")
public class TransactionController extends RestBaseController implements ITransactionController {

    @Autowired
    private ITransactionService transactionService;

    @GetMapping("/get")
    @Override
    public RootEntity<List<DtoTransaction>> getUserTransactions() {
        return ok(transactionService.getUserTransactions());
    }

    @PostMapping("/save")
    @Override
    public RootEntity<DtoTransaction> addTransaction(@Valid @RequestBody DtoTransactionIU dtoTransactionIU) {
        return ok(transactionService.addTransaction(dtoTransactionIU));
    }

    @GetMapping("/get/{id}")
    @Override
    public RootEntity<DtoTransaction> getTransactionById(@PathVariable(name = "id") Long transactionId) {
        return ok(transactionService.getTransactionById(transactionId));
    }

    @GetMapping("/get-by-category/{category_id}")
    @Override
    public RootEntity<List<DtoTransaction>> getTransactionByCategory(@PathVariable(name = "category_id") Long categoryId) {
        if (!(categoryId instanceof Long)) {
            throw new BaseException(new ErrorMessage(MessageType.ID_MUST_BE_INTEGER, categoryId.toString()));
        }
        return ok(transactionService.getTransactionByCategory(categoryId));
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public RootEntity<DtoTransaction> deleteTransaction(@PathVariable(name = "id") Long transactionId) {
        return ok(transactionService.deleteTransaction(transactionId));
    }

    @PutMapping("/update/{id}")
    @Override
    public RootEntity<DtoTransaction> updateTransaction(@PathVariable(name = "id") Long transactionId, @RequestBody DtoTransactionIU dtoTransactionIU) {
        return ok(transactionService.updateTransaction(transactionId,dtoTransactionIU));
    }

    @GetMapping("/get-monthly")
    @Override
    public RootEntity<List<DtoTransaction>> getMonthlyTransactions(
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        return ok(transactionService.getMonthlyTransactions(year, month));
    }

    // Haftalık işlemler için endpoint
    @GetMapping("/get-weekly")
    @Override
    public RootEntity<List<DtoTransaction>> getWeeklyTransactions(
            @RequestParam("anyDateInWeek")
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date anyDateInWeek) {
        return ok(transactionService.getWeeklyTransactions(anyDateInWeek));
    }

    @GetMapping("/get-by-amount-range")
    @Override
    public RootEntity<List<DtoTransaction>> getTransactionsByAmountRange(
            @RequestParam(value = "minAmount",required = false) BigDecimal minAmount,
            @RequestParam(value = "maxAmount",required = false) BigDecimal maxAmount){
        return ok(transactionService.getTransactionsByAmountRange(minAmount, maxAmount));
    }
}

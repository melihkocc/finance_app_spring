package com.melihkoc.service.impl;

import com.melihkoc.dto.DtoCategory;
import com.melihkoc.dto.DtoTransaction;
import com.melihkoc.dto.DtoTransactionIU;
import com.melihkoc.dto.DtoUser;
import com.melihkoc.exception.BaseException;
import com.melihkoc.exception.ErrorMessage;
import com.melihkoc.exception.MessageType;
import com.melihkoc.model.Category;
import com.melihkoc.model.Transaction;
import com.melihkoc.model.UserApp;
import com.melihkoc.repository.CategoryRepository;
import com.melihkoc.repository.TransactionRepository;
import com.melihkoc.repository.UserRepository;
import com.melihkoc.service.ITransactionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    private UserApp getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserApp> optUsername = userRepository.findByUsername(username);
        if(optUsername.isEmpty()){
            throw new BaseException(new ErrorMessage(MessageType.USER_NOT_FOUND,username));
        }
        return optUsername.get();
    }

    //// get transactions
    @Override
    public List<DtoTransaction> getUserTransactions() {
        UserApp currentUser = getCurrentUser();
        List<DtoTransaction> dtoTransactionList = new ArrayList<>();

        List<Transaction> transactionList = transactionRepository.findByUserId(currentUser.getId());
        if(transactionList!=null && !transactionList.isEmpty()){
            for (Transaction transaction : transactionList){
                DtoTransaction dtoTransaction = new DtoTransaction();
                DtoCategory dtoCategory = new DtoCategory();
                BeanUtils.copyProperties(transaction,dtoTransaction);
                BeanUtils.copyProperties(transaction.getCategory(),dtoCategory);
                dtoTransaction.setCategory(dtoCategory);
                dtoTransactionList.add(dtoTransaction);
            }
        }

        return dtoTransactionList;
    }

    ////add transactions

    private Transaction createTransaction(DtoTransactionIU dtoTransactionIU){
        Optional<UserApp> optUser = userRepository.findById(dtoTransactionIU.getUserId());
        Optional<Category> optCategory = categoryRepository.findById(dtoTransactionIU.getCategoryId());

        if(optUser.isEmpty()){
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,dtoTransactionIU.getUserId().toString()));
        }
        if(optCategory.isEmpty()){
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,dtoTransactionIU.getCategoryId().toString()));
        }

        if(dtoTransactionIU.getAmount().compareTo(BigDecimal.ZERO)<0){
            throw new BaseException(new ErrorMessage(MessageType.AMOUNT_IS_NOT_NEGATIVE,dtoTransactionIU.getAmount().toString()));
        }

        if(!optCategory.get().getUser().getId().equals(optUser.get().getId())){
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,dtoTransactionIU.getCategoryId().toString()));
        }

        Transaction transaction = new Transaction();
        transaction.setTitle(dtoTransactionIU.getTitle());
        transaction.setDescription(dtoTransactionIU.getDescription());
        transaction.setTransactionType(dtoTransactionIU.getTransactionType());
        transaction.setCreateTime(dtoTransactionIU.getCreateTime());
        transaction.setEndDate(dtoTransactionIU.getEndDate());
        transaction.setAmount(dtoTransactionIU.getAmount());
        transaction.setCategory(optCategory.get());
        transaction.setUser(optUser.get());

        return transaction;
    }

    @Override
    public DtoTransaction addTransaction(DtoTransactionIU dtoTransactionIU) {

        DtoTransaction dtoTransaction = new DtoTransaction();
        DtoUser dtoUser = new DtoUser();
        DtoCategory dtoCategory = new DtoCategory();

        Transaction savedTransaction = transactionRepository.save(createTransaction(dtoTransactionIU));
        BeanUtils.copyProperties(savedTransaction,dtoTransaction);
        BeanUtils.copyProperties(savedTransaction.getUser(),dtoUser);
        BeanUtils.copyProperties(savedTransaction.getCategory(),dtoCategory);

        dtoTransaction.setUser(dtoUser);
        dtoTransaction.setCategory(dtoCategory);
        return dtoTransaction;
    }

    /// get transaction by ıd
    @Override
    public DtoTransaction getTransactionById(Long transactionId) {
        UserApp currentUser = getCurrentUser();
        DtoTransaction dtoTransaction = new DtoTransaction();
        DtoCategory dtoCategory = new DtoCategory();
        DtoUser dtoUser = new DtoUser();

        Optional<Transaction> optTransaction = transactionRepository.findById(transactionId);
        if(optTransaction.isEmpty()){
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,transactionId.toString()));
        }
        Transaction transaction = optTransaction.get();

        if(!transaction.getUser().getId().equals(currentUser.getId())){
            return dtoTransaction;
        }

        BeanUtils.copyProperties(transaction,dtoTransaction);
        BeanUtils.copyProperties(transaction.getCategory(),dtoCategory);
        BeanUtils.copyProperties(transaction.getUser(),dtoUser);

        dtoTransaction.setCategory(dtoCategory);
        dtoTransaction.setUser(dtoUser);
        return dtoTransaction;
    }

    /// get transaction by category
    @Override
    public List<DtoTransaction> getTransactionByCategory(Long categoryId) {
        UserApp currentUser = getCurrentUser();
        List<DtoTransaction> dtoTransactionList = new ArrayList<>();

        List<Transaction> transactions = transactionRepository.findByCategoryId(categoryId);
        if(transactions!=null && !transactions.isEmpty()){
            for (Transaction transaction : transactions){
                if(transaction.getUser().getId().equals(currentUser.getId())){
                   DtoTransaction dtoTransaction = new DtoTransaction();
                   BeanUtils.copyProperties(transaction,dtoTransaction);
                   DtoCategory dtoCategory = new DtoCategory();
                   BeanUtils.copyProperties(transaction.getCategory(),dtoCategory);
                   dtoTransaction.setCategory(dtoCategory);
                   dtoTransactionList.add(dtoTransaction);
                }
            }
        }

        return dtoTransactionList;
    }

    @Override
    public DtoTransaction deleteTransaction(Long transactionId) {
        UserApp currentUser = getCurrentUser();
        Optional<Transaction> optTransaction = transactionRepository.findById(transactionId);
        if (optTransaction.isEmpty()) {
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,transactionId.toString()));
        }
        if(!optTransaction.get().getUser().getId().equals(currentUser.getId())){
            throw new BaseException(new ErrorMessage(MessageType.NOT_ACCESS_PERMISSION,""));
        }
        DtoTransaction dtoTransaction = new DtoTransaction();
        DtoCategory dtoCategory = new DtoCategory();
        BeanUtils.copyProperties(optTransaction.get(),dtoTransaction);
        BeanUtils.copyProperties(optTransaction.get().getCategory(),dtoCategory);
        dtoTransaction.setCategory(dtoCategory);
        transactionRepository.deleteById(transactionId);
        return dtoTransaction;
    }

    @Override
    public DtoTransaction updateTransaction(Long transactionId, DtoTransactionIU dtoTransactionIU) {
        UserApp currentUser = getCurrentUser(); // Mevcut kullanıcıyı al

        Optional<Transaction> optTransaction = transactionRepository.findById(transactionId);

        if (optTransaction.isEmpty()) {
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, transactionId.toString()));
        }

        Transaction dbTransaction = optTransaction.get();

        if (!dbTransaction.getUser().getId().equals(currentUser.getId())) {
            throw new BaseException(new ErrorMessage(MessageType.NOT_ACCESS_PERMISSION, ""));
        }

        dbTransaction.setTitle(dtoTransactionIU.getTitle());
        dbTransaction.setDescription(dtoTransactionIU.getDescription());
        dbTransaction.setAmount(dtoTransactionIU.getAmount());
        dbTransaction.setTransactionType(dtoTransactionIU.getTransactionType());
        dbTransaction.setCreateTime(dtoTransactionIU.getCreateTime());
        dbTransaction.setEndDate(dtoTransactionIU.getEndDate());

        if (dtoTransactionIU.getCategoryId() != null) {
            Optional<Category> optCategory = categoryRepository.findById(dtoTransactionIU.getCategoryId());
            if (optCategory.isEmpty()) {
                throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, "Category not found"));
            }
            dbTransaction.setCategory(optCategory.get());
        }

        Transaction updatedTransaction = transactionRepository.save(dbTransaction);

        DtoTransaction dtoTransaction = new DtoTransaction();
        BeanUtils.copyProperties(updatedTransaction, dtoTransaction);
        return dtoTransaction;
    }

    private List<DtoTransaction> mapToDto(List<Transaction> transactions) {
        UserApp currentUser = getCurrentUser();

        return transactions.stream()
                // Sadece mevcut kullanıcıya ait transaction'ları filtrele
                .filter(transaction -> transaction.getUser().getId().equals(currentUser.getId()))
                .map(transaction -> {
                    DtoTransaction dtoTransaction = new DtoTransaction();
                    DtoCategory dtoCategory = new DtoCategory();

                    // Transaction -> DtoTransaction kopyalama
                    BeanUtils.copyProperties(transaction, dtoTransaction);

                    // Category kopyalama (önce null kontrolü yap)
                    if (transaction.getCategory() != null) {
                        BeanUtils.copyProperties(transaction.getCategory(), dtoCategory);
                        dtoTransaction.setCategory(dtoCategory);
                    } else {
                        dtoTransaction.setCategory(null);
                    }

                    return dtoTransaction;
                })
                .collect(Collectors.toList());
    }



    ////monthly get
    public List<DtoTransaction> getMonthlyTransactions(int year, int month) {
        UserApp currentUser = getCurrentUser();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();

        List<Transaction> transactions = transactionRepository.findByUserIdAndCreateTimeBetween(currentUser.getId(), startDate, endDate);

        return mapToDto(transactions);
    }

    ///get monthly
    public List<DtoTransaction> getWeeklyTransactions(Date anyDateInWeek) {
        UserApp currentUser = getCurrentUser();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(anyDateInWeek);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date endDate = calendar.getTime();

        List<Transaction> transactions = transactionRepository.findByUserIdAndCreateTimeBetween(currentUser.getId(), startDate, endDate);

        return mapToDto(transactions);
    }

    @Override
    public List<DtoTransaction> getTransactionsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        List<Transaction> transactions;

        if (maxAmount==null) {
            transactions = transactionRepository.findByAmountGreaterThan(minAmount);
        }
        else if (minAmount==null) {
            transactions = transactionRepository.findByAmountLessThan(maxAmount);
        }
        else if (minAmount!=null && maxAmount!=null) {
            transactions = transactionRepository.findByAmountBetween(minAmount,maxAmount);
        }
        else{
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,""));
        }
        return mapToDto(transactions);
    }


}

package com.melihkoc.repository;

import com.melihkoc.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByCategoryId(Long categoryId);

    List<Transaction> findByUserIdAndCreateTimeBetween(Long userId, Date startDate, Date endDate);

    List<Transaction> findByAmountGreaterThan(BigDecimal amount);

    List<Transaction> findByAmountLessThan(BigDecimal amount);

    List<Transaction> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
}

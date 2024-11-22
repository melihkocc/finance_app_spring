package com.melihkoc.model;

import com.melihkoc.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseEntity{

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "description",nullable = true)
    private String description;

    @Column(name = "amount",nullable = false)
    private BigDecimal amount;

    @Column(name = "create_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createTime;

    @Column(name = "end_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date endDate;

    @Column(name = "transaction_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserApp user;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

}

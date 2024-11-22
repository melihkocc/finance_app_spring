package com.melihkoc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.melihkoc.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class DtoTransactionIU {

    @NotNull
    private String title;

    private String description;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    private Long userId;

    @NotNull
    private Long categoryId;
}

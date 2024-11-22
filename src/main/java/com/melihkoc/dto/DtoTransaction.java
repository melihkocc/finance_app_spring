package com.melihkoc.dto;

import com.melihkoc.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class DtoTransaction extends DtoBase{

    private String title;

    private String description;

    private BigDecimal amount;

    private Date createTime;

    private Date endDate;

    private TransactionType transactionType;

    private DtoUser user;

    private DtoCategory category;

}

package com.melihkoc.dto;

import com.melihkoc.model.Transaction;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DtoCategoryUI {

    @NotNull
    private String name;

    @NotNull
    private Long userId;

}

package com.melihkoc.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoCategory extends DtoBase{

    private String name;

    private DtoUser user;
}

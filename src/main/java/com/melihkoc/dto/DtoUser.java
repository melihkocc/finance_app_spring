package com.melihkoc.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DtoUser extends DtoBase {

    private String username;

    private String password;

    private Date createTime;

}

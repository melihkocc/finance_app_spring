package com.melihkoc.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)  //// NULL OLAN VERİLER DÖNMEYECEK GERİ GÖZÜKMEYECEK HİÇ
public class RootEntity<T> {

    private Integer status;

    private T payload;

    private String errorMessage;

    public static <T> RootEntity<T> ok(T payload){
        RootEntity<T> rootEntity = new RootEntity<>();
        rootEntity.setStatus(200);
        rootEntity.setPayload(payload);
        rootEntity.setErrorMessage(null);

        return rootEntity;
    }

    public static<T> RootEntity<T> error(String errorMessage){
        RootEntity<T> rootEntity = new RootEntity<>();
        rootEntity.setStatus(500);
        rootEntity.setPayload(null);
        rootEntity.setErrorMessage(errorMessage);

        return rootEntity;
    }

}

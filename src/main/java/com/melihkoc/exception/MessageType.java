package com.melihkoc.exception;

import lombok.Getter;

@Getter
public enum MessageType {
    NO_RECORD_EXIST("1004","Kayıt Bulunamadı"),
    TOKEN_EXPIRED("1005","Token Süresi Bitmiştir"),
    USERNAME_NOT_FOUND("1006","Username bulunamadı"),
    USERNAME_OR_PASSWORD_INVALID("1007","Kullanıcı adı veya Şifre Hatalı."),
    REFRESH_TOKEN_NOT_FOUND("1008","Refresh Token Bulunamadı"),
    REFRESH_TOKEN_IS_EXPIRED("1009","Refresh Token Süresi Bitmiştir"),
    USER_NOT_FOUND("1010","Kullanıcı Bulunamadı"),
    AMOUNT_IS_NOT_NEGATIVE("1011","Para Miktarı negatif değer olamaz"),
    ID_MUST_BE_INTEGER("1012","Lütfen Bir Sayı Giriniz"),
    NOT_ACCESS_PERMISSION("1013","Erişim İzniniz Yoktur"),
    USERNAME_ALREADY_EXISTS("1014","Böyle Bir Kullanıcı Zaten Mevcut"),
    GENERAL_EXCEPTION("9999","genel bir hata oluştu.");

    private String code;

    private String message;

    MessageType(String code, String message){
        this.code = code;
        this.message = message;
    }

}

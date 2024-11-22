package com.melihkoc.handler;

import com.melihkoc.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    /// bunlar benim fırlatttığım hatalar
    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<ApiError<?>> handleBaseException(BaseException ex, WebRequest request) {
        // Hata mesajını getMessage() ile alıyoruz (String türünde)
        String errorMessage = ex.getMessage();

        HttpStatus status;

        // Hata mesajına göre durumu belirliyoruz
        if (errorMessage.contains("Erişim İzniniz Yoktur")) {
            status = HttpStatus.FORBIDDEN;
        } else if (errorMessage.contains("Token Süresi Bitmiştir")) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (errorMessage.contains("Kayıt Bulunamadı")) {
            status = HttpStatus.NOT_FOUND;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        // Hata mesajını direkt createApiError'a gönderiyoruz
        return ResponseEntity.status(status)
                .body(createApiError(errorMessage, request, status));
    }


    private List<String> addValue(List<String> list, String newValue){
        list.add(newValue);
        return list;
    }

    /// bu da springin validation hataları model içinde veriyoruz ya
    ///
    /// @return
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request){

        Map<String,List<String>> map = new HashMap<>();
        for(ObjectError objError : ex.getBindingResult().getAllErrors()){
            String fieldName = ((FieldError)objError).getField();
            if(map.containsKey(fieldName)){
                map.put(fieldName,addValue(map.get(fieldName),objError.getDefaultMessage()));
            }else{
                map.put(fieldName,addValue(new ArrayList<>(),objError.getDefaultMessage()));
            }
        }
        return ResponseEntity.badRequest().body(createApiError(map,request,HttpStatus.BAD_REQUEST));
    }

    private String getHostName(){
        try{
            return Inet4Address.getLocalHost().getHostName();
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
        return "";
    }

    private <E> ApiError<E> createApiError(E message, WebRequest request,HttpStatus status){
        ApiError<E> apiError =  new ApiError<>();
        apiError.setStatus(status.value());

        Exception<E> exception = new Exception<>();
        exception.setPath(request.getDescription(false).substring(4));
        exception.setCreateTime(new Date());
        exception.setMessage(message);
        exception.setHostName(getHostName());

        apiError.setException(exception);
        return apiError;
    }
}

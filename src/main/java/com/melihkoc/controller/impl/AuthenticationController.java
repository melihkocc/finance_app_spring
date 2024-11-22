package com.melihkoc.controller.impl;

import com.melihkoc.controller.IAuthenticationController;
import com.melihkoc.controller.RestBaseController;
import com.melihkoc.controller.RootEntity;
import com.melihkoc.dto.DtoUser;
import com.melihkoc.jwt.AuthRequest;
import com.melihkoc.jwt.AuthResponse;
import com.melihkoc.jwt.RefreshTokenRequest;
import com.melihkoc.service.IAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController extends RestBaseController implements IAuthenticationController {

    @Autowired
    IAuthenticationService authenticationService;

    @PostMapping("/register")
    @Override
    //// BURADA VALID ANATASYONU VE REQUESTBODY ANATASYONU YAPIYORUZ ENETIY İÇİNDE YAPTIĞIMIZ VALIDLER ÇALIŞSIN DİYE VE  POST İLE VERİ ALACAĞIMIZ İÇİN REQUESTBODY YAPIYORUZ
    public RootEntity<DtoUser> register(@Valid @RequestBody AuthRequest input) {
        DtoUser dtoUser = authenticationService.register(input);
        return ok(dtoUser);
    }

    @PostMapping("/authenticate")
    @Override
    public RootEntity<AuthResponse> authentication(@Valid @RequestBody AuthRequest input) {
        AuthResponse authResponse = authenticationService.authenticate(input);
        return ok(authResponse);
    }

    @PostMapping("/refreshToken")
    @Override
    public RootEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest input) {
        AuthResponse authResponse = authenticationService.refreshToken(input);
        return ok(authResponse);
    }
}

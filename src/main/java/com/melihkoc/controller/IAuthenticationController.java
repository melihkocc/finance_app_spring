package com.melihkoc.controller;

import com.melihkoc.dto.DtoUser;
import com.melihkoc.jwt.AuthRequest;
import com.melihkoc.jwt.AuthResponse;
import com.melihkoc.jwt.RefreshTokenRequest;

public interface IAuthenticationController {

    public RootEntity<DtoUser> register(AuthRequest input);

    public RootEntity<AuthResponse> authentication(AuthRequest input);

    public RootEntity<AuthResponse> refreshToken(RefreshTokenRequest input);

}

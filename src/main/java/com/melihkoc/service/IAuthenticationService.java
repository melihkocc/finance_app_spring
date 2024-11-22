package com.melihkoc.service;

import com.melihkoc.dto.DtoUser;
import com.melihkoc.jwt.AuthRequest;
import com.melihkoc.jwt.AuthResponse;
import com.melihkoc.jwt.RefreshTokenRequest;

public interface IAuthenticationService {

    public DtoUser register(com.melihkoc.jwt.AuthRequest input);

    public AuthResponse authenticate(AuthRequest input);

    public AuthResponse refreshToken(com.melihkoc.jwt.RefreshTokenRequest input);
}

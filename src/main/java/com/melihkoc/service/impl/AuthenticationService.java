package com.melihkoc.service.impl;

import com.melihkoc.dto.DtoUser;
import com.melihkoc.exception.BaseException;
import com.melihkoc.exception.ErrorMessage;
import com.melihkoc.exception.MessageType;
import com.melihkoc.jwt.AuthRequest;
import com.melihkoc.jwt.AuthResponse;
import com.melihkoc.jwt.JwtService;
import com.melihkoc.jwt.RefreshTokenRequest;
import com.melihkoc.model.RefreshToken;
import com.melihkoc.model.UserApp;
import com.melihkoc.repository.RefreshTokenRepository;
import com.melihkoc.repository.UserRepository;
import com.melihkoc.service.IAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthenticationService implements IAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    private UserApp createUser(AuthRequest input){
        //// BURADA AUTHREQUEST CLASSI GİBİ OLAN GİRİLECEK OLAN İNPUTU USER MODELİNE SETLEDİK AŞAĞIDAKİ REGİSTER SERVİCESİNDE REPOSİTOYRYE SAVE İLE KAYDETTİK KULLANICIYI
        UserApp user = new UserApp();
        user.setCreateTime(new Date());
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        return user;
    }

    /// register service
    @Override
    public DtoUser register(AuthRequest input) {
        DtoUser dtoUser = new DtoUser();

        Optional<UserApp> optUser = userRepository.findByUsername(input.getUsername());
        if(optUser.isPresent()){
            throw new BaseException(new ErrorMessage(MessageType.USERNAME_ALREADY_EXISTS,input.getUsername()));
        }

        UserApp user = createUser(input);
        UserApp savedUser = userRepository.save(user);

        BeanUtils.copyProperties(savedUser,dtoUser);
        return dtoUser;
    }

    //// authenticate login service
    @Override
    public AuthResponse authenticate(@Valid AuthRequest input) {
        try {
            /// BU VERİTABANINDA KULLANICI ADI VE ŞİFRE DOĞRU MU KONTROL EDECEK
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(input.getUsername(),input.getPassword());
            authenticationProvider.authenticate(authenticationToken);
            /// kullanıcı ve şifre doğru ise token oluşturuyoz aşağıda

            Optional<UserApp> optUser = userRepository.findByUsername(input.getUsername());

            String accessToken = jwtService.generateToken(optUser.get());
            RefreshToken refreshToken = jwtService.createRefreshToken(optUser.get());
            RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

            return new AuthResponse(accessToken,savedRefreshToken.getRefreshToken(),optUser.get().getId());

        } catch (Exception e) {
            throw new BaseException(new ErrorMessage(MessageType.USERNAME_OR_PASSWORD_INVALID,e.getMessage()));
        }

    }


    /// refresh token expire olmuş mu kontrol function
    public boolean isValidRefreshToken(Date expireDate){
        return new Date().before(expireDate);
    }

    /// refresh token ile access token yenileme
    @Override
    public AuthResponse refreshToken(RefreshTokenRequest input) {
        Optional<RefreshToken> optRefreshToken = refreshTokenRepository.findByRefreshToken(input.getRefreshToken());

        if(optRefreshToken.isEmpty()){
            throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_NOT_FOUND,input.getRefreshToken()));
        }
        if(!isValidRefreshToken(optRefreshToken.get().getExpiredDate())){
            throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_IS_EXPIRED,input.getRefreshToken()));
        }

        UserApp user = optRefreshToken.get().getUser();

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = jwtService.createRefreshToken(user);
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessToken,savedRefreshToken.getRefreshToken(),user.getId());
    }
}

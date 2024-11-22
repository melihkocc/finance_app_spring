package com.melihkoc.jwt;

import com.melihkoc.model.RefreshToken;
import com.melihkoc.model.UserApp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    public static final String SECRET_KEY = "ApMyxJDgqR+eubJEpnsol1rsgqwk/haQinf6Mj9KMiQ=";

    public String generateToken(UserDetails userDetails){

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*2))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public RefreshToken createRefreshToken(UserApp user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCreateTime(new Date());
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4)); //// 4 saat
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        return refreshToken;
    }

    public <T> T exportToken(String token, Function<Claims,T> claimsFunc){
        Claims claims = getClaims(token);
        return claimsFunc.apply(claims);
    }

    public String getUsernameByToken(String token){
        return exportToken(token,Claims::getSubject);
    }

    public boolean isTokenValid(String token){
        Date expireDate = exportToken(token,Claims::getExpiration);
        return new Date().before(expireDate);
    }

    public Claims getClaims(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token).getBody();
        return claims;
    }

    public Key getKey(){
        byte[] bytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

}

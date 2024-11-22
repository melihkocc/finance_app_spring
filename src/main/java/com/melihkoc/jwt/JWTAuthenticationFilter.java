package com.melihkoc.jwt;

import com.melihkoc.exception.BaseException;
import com.melihkoc.exception.ErrorMessage;
import com.melihkoc.exception.MessageType;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    //// BU METHOD OTOMATİK GELİYOR İÇİNDEKİLERİ BİZ YAPTIK
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        /// YAPILAN İSTEKLER BURAYA DÜŞÜYOR İLK ONCEPERREQUESTFİLTER İŞE EXTENDS ETTİĞİMİZ İÇİN CLASSI
        //// HEADERDE ADAM BİR ŞEY GÖNDERMEDİYSE CONTROLLERA GÖNDERMEDEN RETURN EDİYORUZ İŞLEMİ GİDEMİYOR ADAM CONTROLLERA
        if(header==null){
            filterChain.doFilter(request,response);
            return;
        }
        String token;
        String username;

        token = header.substring(7);
        try{
            username = jwtService.getUsernameByToken(token);
            ////USERNAME NULL DEĞİLSE VE SECURİTYCONTEXTHOLDER CLASSI İÇİNDE BİR USER YOKSA İŞLEM DEVAM
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //// USERDETAİLS YANİ DATABASE İÇİNDE BÖYLE BİR ADAM VARSA VE TOKEN EXPİRE DEĞİLSE DEVAM
                if(userDetails!=null && jwtService.isTokenValid(token)){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null,userDetails.getAuthorities());

                    authenticationToken.setDetails(userDetails);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        catch (ExpiredJwtException ex){
            ///kendi yaptığımız exception handling
            throw new BaseException(new ErrorMessage(MessageType.TOKEN_EXPIRED,ex.getMessage()));
        }
        catch (Exception e) {
            ///kendi yaptığımız exception handling
            throw new BaseException(new ErrorMessage(MessageType.GENERAL_EXCEPTION,e.getMessage()));
        }
        filterChain.doFilter(request,response);
    }
}

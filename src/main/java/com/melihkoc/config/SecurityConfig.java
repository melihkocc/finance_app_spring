package com.melihkoc.config;

import com.melihkoc.handler.AuthEntryPoint;
import com.melihkoc.jwt.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String REGISTER = "/register";
    public static final String AUTHENTICATE = "/authenticate";
    public static final String REFRESH_TOKEN = "/refreshToken";


    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //// BURADA YUKARIDAKİ URL ADRESLERİNE İSTEK GELİRSE JWT SERVİSLERİNE GÖNDERME YANİ JWTAUTHENTİCATİONFİLTER SINIFINA GİTMESİN
        //// ÇÜNKÜ ADAM YENİ KAYIT OLACAK VEYA GİRİŞ YAPACAK YA TOKENİ HİÇ YOK O YÜZDEN

        http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                    corsConfiguration.setAllowCredentials(true);
                    // Yıldız yerine, burada belirli originleri listeleyin
                    corsConfiguration.addAllowedOrigin("http://localhost:3000"); // Sadece bu domain'e izin ver
                    corsConfiguration.addAllowedHeader("*");  // Tüm header'lara izin verir
                    corsConfiguration.addAllowedMethod("GET");  // GET methoduna izin verir
                    corsConfiguration.addAllowedMethod("POST"); // POST methoduna izin verir
                    corsConfiguration.addAllowedMethod("PUT");  // PUT methoduna izin verir
                    corsConfiguration.addAllowedMethod("DELETE"); // DELETE methoduna izin verir
                    corsConfiguration.addAllowedMethod("PATCH");  // PATCH methoduna izin verir
                    return corsConfiguration;
                }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(AUTHENTICATE, REGISTER, REFRESH_TOKEN).permitAll()
                        .anyRequest()
                        .authenticated())
                .exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    };

}

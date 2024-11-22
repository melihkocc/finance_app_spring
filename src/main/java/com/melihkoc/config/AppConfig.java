package com.melihkoc.config;

import com.melihkoc.exception.BaseException;
import com.melihkoc.exception.ErrorMessage;
import com.melihkoc.exception.MessageType;
import com.melihkoc.model.UserApp;
import com.melihkoc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Configuration
public class AppConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            ///// BURADA DATABASEDE BÖYLE BİR KULLANICI VAR MI YOK MU ONA BAKIYORUZ
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

                Optional<UserApp> optional = userRepository.findByUsername(username);
                if(optional.isEmpty()){
                    /// YAPTIĞIMIZ EXCEPTİON HANDLİNG İLE FIRLATTIK HATAYI EĞER KULLANICI YOKSA
                    throw new BaseException(new ErrorMessage(MessageType.USERNAME_NOT_FOUND,username));
                }
                /// kullanıcı varsa dön
                return optional.get();
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /// BUNU BEAN YAPIYORUZ ÇÜNKÜ BU CLASS BAŞKALARI TARAFINDAN YAPILMIŞ AMA BEAN DEĞİL BİZ BEAN YAPTIK VE HER YERDEN ULAŞAİLİYORUZ ARTIK
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

}

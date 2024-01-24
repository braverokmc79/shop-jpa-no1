package com.shop.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.config.auth.PrincipalDetailsService;
import com.shop.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfig  {


  private final PrincipalDetailsService principalDetailsService;

  private final EntityManager em;

  @Bean
  public JPAQueryFactory queryFactory(EntityManager em) {
    return new JPAQueryFactory(em);
  }


  @Bean
  public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    var provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(principalDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }





    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.formLogin(login->
            login
            .loginPage("/members/login")
            .defaultSuccessUrl("/", true)
            .usernameParameter("email")
            .failureUrl("/members/login/error")
        )
         .logout(logoutConfig ->logoutConfig
         .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")).logoutSuccessUrl("/")

      ).exceptionHandling(exceptionConfig->
        exceptionConfig.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));


        http.
            authorizeHttpRequests(request -> request
                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            );

       return http.build();
   }




}

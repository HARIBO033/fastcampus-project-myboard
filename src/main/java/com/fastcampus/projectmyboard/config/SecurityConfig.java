package com.fastcampus.projectmyboard.config;

import com.fastcampus.projectmyboard.dto.UserAccountDto;
import com.fastcampus.projectmyboard.dto.security.BoardPrincipal;
import com.fastcampus.projectmyboard.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests((auth) -> auth
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .requestMatchers(HttpMethod.GET, "/", "/articles", "/articles/search-hashtag")
                    .permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin().and()
            .logout()
            .logoutSuccessUrl("/").and()
            .build();

  }

  @Bean // 실제 인증데이터 로직을 가져오는 부분
  public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
    return username -> userAccountRepository
            .findById(username)
            .map(UserAccountDto::from)
            .map(BoardPrincipal::from)
            .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
  }

  @Bean //패스워드 인코더
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}

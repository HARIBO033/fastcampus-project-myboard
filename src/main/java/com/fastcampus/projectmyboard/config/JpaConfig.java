package com.fastcampus.projectmyboard.config;


import com.fastcampus.projectmyboard.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

  @Bean
  public AuditorAware<String> auditorAware() {
    return () -> Optional.ofNullable(SecurityContextHolder.getContext())// 시큐리티 context를 모두 가져옴
            .map(SecurityContext::getAuthentication)// SequrityContext에 Authentication을 가져옴
            .filter(Authentication::isAuthenticated)//Authentication의 로그인상태 확인
            .map(Authentication::getPrincipal)//로그인 정보인 Principal 꺼내옴
            .map(BoardPrincipal.class::cast)//BoardPrincipal을 불러와서 타입캐스팅을 함
            .map(BoardPrincipal::getUsername);//getUsername으로 실제 유저정보를 불러옴
  }
}

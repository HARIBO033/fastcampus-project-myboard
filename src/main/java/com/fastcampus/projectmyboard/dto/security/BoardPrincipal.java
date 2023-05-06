package com.fastcampus.projectmyboard.dto.security;

import com.fastcampus.projectmyboard.dto.UserAccountDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record BoardPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities,
                             String email, String nickname,
                             String memo) implements UserDetails {

  public static BoardPrincipal of(String username, String password, String email, String nickname, String memo) {
    Set<RoleType> roleTypes = Set.of(RoleType.USER);//앞으로 다양한 Role이 추가 될 수 있음

    return new BoardPrincipal(
            username,
            password,
            roleTypes.stream()
                    .map(RoleType::getName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toUnmodifiableSet()),
            email,
            nickname,
            memo
    );
  }

  public static BoardPrincipal from(UserAccountDto dto) {
    return BoardPrincipal.of(
            dto.userId(),
            dto.userPassword(),
            dto.email(),
            dto.nickname(),
            dto.memo()
    );
  }

  // principal을 받아서 dto로 변경해주자
  public UserAccountDto toDto() {
    return UserAccountDto.of(username, password, email, nickname, memo);
  }

  @Override //권한 관련 부분 (인증 x )
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public enum RoleType {
    USER("ROLE_USER");

    @Getter
    private final String name;


    RoleType(String name) {
      this.name = name;
    }
  }
}

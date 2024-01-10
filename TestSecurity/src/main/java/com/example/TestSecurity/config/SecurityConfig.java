package com.example.TestSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean //어느곳에서든 사용 가능하게 빈으로 등록한다.
    //비밀번호 암호화 메서드
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 권한에 따라 오픈되는 페이지가 다르다

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login","loginProc","/join","/joinProc").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        // anyRequest => 위에 설정해둔 페이지 외의 모든 페이지 / authenticated() -> 로그인 한 모두 접근가능
                        .anyRequest().authenticated());

        //상단부터 진행되므로 .anyRequest가 위에 있다면 인가 설정해놓은 페이지는 그냥 다 open 상태가 된다. 그러므로 순서를 잘 지킬 것.


        //Admin page 설정
        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                );

        http
                .csrf((auth) -> auth.disable());
        //토큰 임시 비활성화

        return http.build();
    }
    
    
}

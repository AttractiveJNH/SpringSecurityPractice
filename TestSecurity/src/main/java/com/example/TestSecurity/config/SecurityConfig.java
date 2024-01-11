package com.example.TestSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean //어느곳에서든 사용 가능하게 빈으로 등록한다.
    //비밀번호 암호화 메서드
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 권한에 따라 오픈되는 페이지가 다르다

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/main", "/", "/login", "loginProc", "/join", "/joinProc").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        // anyRequest => 위에 설정해둔 페이지 외의 모든 페이지 / authenticated() -> 로그인 한 모두 접근가능
                        .anyRequest().authenticated());

        //상단부터 진행되므로 .anyRequest가 위에 있다면 인가 설정해놓은 페이지는 그냥 다 open 상태가 된다. 그러므로 순서를 잘 지킬 것.


//        formLogin 설정
        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll()
                );

        //Http Basic Login 설정
//        http
//                .httpBasic(Customizer.withDefaults());


//        http
//                .csrf((auth) -> auth.disable());
        //개발중에는 auth.disable(), 실제 배포에서는 enable로 해야한다.


        // 다중 로그인 설정

        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1) // 하나의 아이디에 대한  다중 로그인 허용 개수
                        .maxSessionsPreventsLogin(true));//다중 로그인 개수를 초과하였을 경우 처리 방법
        // true - 초과시 새로운 로그인 차단
        // false - 초과시 기존 세션 하나 삭제 )


        // 세션 고정 보호

        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());
        // .sessionFixation() .none = 로그인 시 세션 정보 변경 안함
        // .sessionFixation() .newSession() = 로그인 시 세션 새로 생성
        // .sessionFixation() .changeSessionId() = 로그인 시 동일한 세션에 대한 id 변경 <- 주로 사용


        return http.build();
    }

    //로그아웃 설정 메서드
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .logout((auth) -> auth.logoutUrl("/logout")
                        .logoutSuccessUrl("/"));
        return http.build();
    }


    //InMemory 방식 로그인
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1 = User.builder()
                .username("user1")
                .password(bCryptPasswordEncoder().encode("1234"))
                .roles("ADMIN")
                .build();

        UserDetails user2 = User.builder()
                .username("user2")
                .password(bCryptPasswordEncoder().encode("1234"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }





}

package com.gn.mvc.security;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.gn.mvc.controller.AttachController;

import lombok.RequiredArgsConstructor;



@Configuration // 환경설정 파일
@EnableWebSecurity // 스프링 시큐리티 쓸꺼에요~
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	private final DataSource dataSource;

	// 요청중에 정적 리소스가 있는 경우 -> Security 비활성화
    // 무조건 public이어서 @Bean Object를 쓰면 생략 가능
	@Bean
	WebSecurityCustomizer configure() {
		// webSecurity를 web이라고 부름 -> ignoring은 다 무시하겠다 -> 근데 requestMatchers에 들어 있는 url만 무시하겠다
		// 경로 찾을건데 -> 정적인 것만
		return web -> web.ignoring()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
	// 특정 요청이 들어왔을때 어떻게 처리할 것인가
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService customUserDetailsService) throws Exception {
		// requestMatchers("") 안에 들어가 있는 건 누구나 접근 가능, 외에는 허락할 수 없음
		// login 할 때는 /login url 사용, login하면 /board로 이동
		// logout 했을 때 인증된 사용자 정보 지우는 거 허락(true)
		// logout 되면 /login으로 이동
		// logout 하면 세션 무효화(true)
		http.userDetailsService(customUserDetailsService)
			.authorizeHttpRequests(requests -> requests
				.requestMatchers("/login", "/signup", "/logout", "/").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated())
			.formLogin(login -> login.loginPage("/login")
									.successHandler(new MyLoginSuccessHandler())
									.failureHandler(new MyLoginFailureHandler()))
			.logout(logout -> logout.logoutUrl("/logout")
									.clearAuthentication(true)
									.logoutSuccessUrl("/login")
									.invalidateHttpSession(true)
									.deleteCookies("remember-me"))
			.rememberMe(rememberMe -> rememberMe.rememberMeParameter("remember-me")
											.tokenValiditySeconds(60*60*24*30)
											.alwaysRemember(false)
											.tokenRepository(tokenRepository()));
		return http.build();
	}
	
	
	
	// 데이터베이스 접근 Bean 등록
	@Bean
	PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
		jdbcTokenRepository.setDataSource(dataSource);
		return jdbcTokenRepository;
	}
	
	// 비밀번호 암호화 사용될 Bean 등록
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// AuthenticationManager(인증 관리)
	@Bean
	AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationCofiguration) throws Exception {
		return authenticationCofiguration.getAuthenticationManager();
	}
}

package ute.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/","/login", "/books", "/chapters").permitAll() // Không yêu cầu xác thực
                        .anyRequest().permitAll() // Các URL khác yêu cầu đăng nhập
                )
                .formLogin(form -> form
                        .loginPage("/login") // Trang login tùy chỉnh (nếu có)
                        .permitAll() // Cho phép mọi người truy cập trang login
                )
                .logout(logout -> logout.permitAll()); // Cho phép logout
        return http.build();
    }
}

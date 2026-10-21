/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/21/2026
 */

package iuh.fit.demospringjdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Lấy user từ bảng users(id, username, password, role) bằng SQL tự viết.
     */
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
                "SELECT username, password, TRUE AS enabled FROM users WHERE username = ?");
        // Câu này trả về authority nguyên văn nên phải tự thêm tiền tố ROLE_
        manager.setAuthoritiesByUsernameQuery(
                "SELECT username, CONCAT('ROLE_', role) AS authority FROM users WHERE username = ?");
        return manager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Công khai
                        .requestMatchers("/login", "/access-denied", "/error", "/css/**", "/js/**").permitAll()

                        // Thêm / Sửa: USER, ADMIN (đặt TRƯỚC rule xem vì /employees/new cũng khớp /employees/*)
                        .requestMatchers(HttpMethod.GET, "/employees/new").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/employees").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/employees/*/edit").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/employees/*").hasAnyRole("USER", "ADMIN")

                        // Xóa: chỉ ADMIN
                        .requestMatchers(HttpMethod.POST, "/employees/*/delete").hasRole("ADMIN")

                        // Xem danh sách (kèm tìm kiếm), xem chi tiết: cả 3 role
                        .requestMatchers(HttpMethod.GET, "/employees", "/employees/*")
                        .hasAnyRole("GUEST", "USER", "ADMIN")

                        // Thống kê có số liệu lương (nhạy cảm): USER, ADMIN
                        .requestMatchers("/reports/**", "/departments/**", "/positions/**")
                        .hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/employees", true)
                        .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/login?logout"))
                .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));
        // CSRF được bật mặc định, không tắt.

        return http.build();
    }
}

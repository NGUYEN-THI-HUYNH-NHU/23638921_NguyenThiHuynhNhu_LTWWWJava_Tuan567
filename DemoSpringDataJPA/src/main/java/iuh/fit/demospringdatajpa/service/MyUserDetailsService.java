/**
 * File: null.java
 * Created by: Nguyen Thi Huynh Nhu
 * Date: 10/21/2026
 */

package iuh.fit.demospringdatajpa.service;

import iuh.fit.demospringdatajpa.entity.AppUser;
import iuh.fit.demospringdatajpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy: " + username));

        return User.withUsername(user.getUsername())
                .password(user.getPassword())      // đã mã hóa BCrypt
                .roles(user.getRole().name())      // "ADMIN" -> ROLE_ADMIN
                .build();
    }
}
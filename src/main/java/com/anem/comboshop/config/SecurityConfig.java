package com.anem.comboshop.config;

import com.anem.comboshop.repo.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepo){
        return username -> userRepo.findByUsername(username)
                .map(u -> User.withUsername(u.getUsername())
                        .password(u.getPasswordHash())
                        .authorities(u.getRoles().toArray(new String[0]))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    DaoAuthenticationProvider authProvider(UserDetailsService uds, PasswordEncoder encoder){
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(encoder);
        return p;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/", "/register", "/css/**", "/js/**", "/images/**", "/combo/**", "/cart/**", "/products/**", "/uploads/**", "/h2/**").permitAll()
              .requestMatchers("/orders/**", "/returns/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
              .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
              .anyRequest().authenticated()
          )
          .formLogin(form -> form
              .loginPage("/login").permitAll()
              .defaultSuccessUrl("/", true)
          )
          .logout(l -> l.logoutSuccessUrl("/"))
          .csrf(csrf -> csrf.ignoringRequestMatchers("/h2/**"))
          .headers(h -> h.frameOptions(f -> f.sameOrigin()));
        return http.build();
    }
}

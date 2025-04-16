package es.jlrn.configuration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationProvider;

import es.jlrn.configuration.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;


//@EnableGlobalMethodSecurity(prePostEnabled = true) // activar par PreAutorizes
@Configuration 
@EnableWebSecurity 
@RequiredArgsConstructor
public class SecurityConfig {
//
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //
    return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authRequest -> 
                    authRequest
                        .requestMatchers("/auth/**", "/api/person/**","/api/users/**","/api/roles/**","/api/permissions/**").permitAll()
                        .anyRequest().authenticated()
                        //.requestMatchers("/v1/home").authenticated()
                        //.requestMatchers("/v1/admin").hasAuthority("ADMIN").anyRequest().authenticated()
            )
            // .formLogin(Customizer.withDefaults())
            // .build();  
            .sessionManagement(sessionManager->
                sessionManager 
                  .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // no usa las sesiones
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();            
    }
}

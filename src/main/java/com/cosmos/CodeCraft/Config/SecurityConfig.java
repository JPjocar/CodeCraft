/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Config;

import com.cosmos.CodeCraft.Filter.JwtTokenValidator;
import com.cosmos.CodeCraft.Service.UserDetailsServiceImpl;
import com.cosmos.CodeCraft.Utils.JwtUtils;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
    @Autowired
    private JwtUtils jwtUtils;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http.requestMatchers(HttpMethod.POST, "/auth/sign-up").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/auth/log-in").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/answer/test").permitAll();

                    http.requestMatchers("/question/**").authenticated();

                    http.requestMatchers("/tag/**").hasRole("ADMIN");

                    http.anyRequest().authenticated();
                })
             .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
             .build();
    }
//    
    
@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Origen de Angular
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setExposedHeaders(Arrays.asList("Authorization")); // Encabezados expuestos al frontend
        config.setAllowCredentials(true); // Permite cookies o JWT

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Aplica a todas las rutas
        return source;
    }
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true); // Permite cookies
//        config.addAllowedOrigin("http://localhost:4200"); // URL de Angular
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    } 
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
//        return httpSecurity
//                .csrf(csrf -> csrf.disable())
//                .httpBasic(Customizer.withDefaults())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(http -> {
//                                        
//                    http.anyRequest().permitAll();
//                    
//                    
//                })
//                .build();
//    }
    
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
//       return authenticationConfiguration.getAuthenticationManager();
//    }
//    
//    @Bean
//    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsServiceImpl){
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(passwordEncoder());
//        provider.setUserDetailsService(userDetailsServiceImpl);
//        return provider;
//    }

    
//    @Bean
//    public UserDetailsService userDetailsService(){
//        List<UserDetails> detailsServices = new ArrayList<>();
//        detailsServices.add(User
//                .withUsername("jocar")
//                .password("12345")
//                .roles("ADMIN")
//                .authorities("READ", "WRITE")
//                .build());
//        detailsServices.add(User
//                .withUsername("jocar2")
//                .password("jocar")
//                .roles("ADMIN")
//                .authorities("READ", "WRITE")
//                .build());
//        return new InMemoryUserDetailsManager(detailsServices);
//    }
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

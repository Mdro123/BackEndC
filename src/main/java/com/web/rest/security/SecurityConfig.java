package com.web.rest.security;

import com.web.rest.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Asegúrate de que HttpMethod esté importado
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // ... (tus @Beans de PasswordEncoder, AuthenticationManager, etc. están bien)
    @Bean
    public PasswordEncoder passwordEncoder() { /* ... */ }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() { /* ... */ }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { /* ... */ }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() { /* ... */ }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                
                // --- SECCIÓN DE ENDPOINTS PÚBLICOS CORREGIDA ---
                
                // 1. Endpoints de Autenticación (POST)
                .requestMatchers(HttpMethod.POST, "/api/usuarios/registro", "/api/usuarios/login").permitAll()
                
                // 2. Endpoint del Chatbot (POST)
                .requestMatchers(HttpMethod.POST, "/api/chatbot/ask").permitAll()

                // 3. Endpoints de Catálogo (GET)
                .requestMatchers(HttpMethod.GET, 
                    "/api/productos/**",         // Permite ver todos los productos y un producto por ID
                    "/api/categorias/**",        // Permite ver todas las categorías
                    "/api/metodos-pago/**",    // Permite ver los métodos de pago
                    "/api/productos/mas-vendidos" // Permite ver los más vendidos (si la ruta es esta)
                ).permitAll()
                
                // --- FIN DE LA CORRECCIÓN ---

                // 4. Endpoints de Administrador
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // 5. Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

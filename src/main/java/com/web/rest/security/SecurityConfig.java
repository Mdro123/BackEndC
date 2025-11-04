package com.web.rest.security;

import com.web.rest.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Importa HttpMethod
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

    // --- Tus Beans (passwordEncoder, authenticationProvider, authenticationManager, corsConfigurationSource) ---
    // --- están perfectos. No los modificamos. ---
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "https://wonderful-sky-0f2cfe81e.1.azurestaticapps.net"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // --- CORRECCIÓN EN filterChain ---
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                
                // --- 1. Endpoints POST Públicos ---
                .requestMatchers(HttpMethod.POST,
                    "/api/usuarios/registro",
                    "/api/usuarios/login",
                    "/api/chatbot/ask" // Permite el chatbot
                ).permitAll()
                
                // --- 2. Endpoints GET Públicos ---
                .requestMatchers(HttpMethod.GET,
                    "/api/productos",                 // Lista de productos
                    "/api/productos/{id}",            // Detalle de producto
                    "/api/productos/buscar",          // Búsqueda
                    "/api/productos/categoria/{idCategoria}", // Filtro por categoría
                    "/api/productos/mas-vendidos",  // Top ventas
                    "/api/categorias",                // Lista de categorías
                    "/api/categorias/{id}",           // Detalle de categoría
                    "/api/categorias/buscar",         // Búsqueda de categorías
                    "/api/metodos-pago",              // Lista de métodos de pago
                    "/api/metodos-pago/{id}"          // Detalle de método de pago
                ).permitAll()
                
                // --- 3. Endpoints de Admin (Protegidos por @PreAuthorize) ---
                // Tu anotación @PreAuthorize("hasRole('ADMIN')") en los controladores
                // se encargará de esto, pero mantenemos esta regla como defensa
                .requestMatchers("/api/admin/**").hasRole("ADMIN") 
                
                // --- 4. El resto requiere autenticación ---
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

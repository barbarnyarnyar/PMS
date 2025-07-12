package com.tolimoli.pms.config;

import com.tolimoli.pms.security.AuthTokenFilter;
import com.tolimoli.pms.security.AuthEntryPointJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
                // Public endpoints
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/hello/**").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                
                // API endpoints requiring authentication
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/manager/**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/api/reception/**").hasAnyRole("ADMIN", "MANAGER", "RECEPTIONIST")
                
                // Specific resource permissions
                .antMatchers("/api/users/**").hasAuthority("USER_MANAGEMENT")
                .antMatchers("/api/roles/**").hasAuthority("ROLE_MANAGEMENT")
                .antMatchers("/api/permissions/**").hasAuthority("PERMISSION_MANAGEMENT")
                
                // Hotel operations
                .antMatchers("/api/rooms/**").hasAnyAuthority("ROOM_VIEW", "ROOM_MANAGEMENT")
                .antMatchers("/api/reservations/**").hasAnyAuthority("RESERVATION_VIEW", "RESERVATION_MANAGEMENT")
                .antMatchers("/api/payments/**").hasAnyAuthority("PAYMENT_VIEW", "PAYMENT_MANAGEMENT")
                .antMatchers("/api/channels/**").hasAnyAuthority("CHANNEL_VIEW", "CHANNEL_MANAGEMENT")
                .antMatchers("/api/rates/**").hasAnyAuthority("RATE_VIEW", "RATE_MANAGEMENT")
                .antMatchers("/api/folio-charges/**").hasAnyAuthority("FOLIO_VIEW", "FOLIO_MANAGEMENT")
                .antMatchers("/api/guests/**").hasAnyAuthority("GUEST_VIEW", "GUEST_MANAGEMENT")
                
                // All other requests require authentication
                .anyRequest().authenticated();

        // Disable frame options for H2 console
        http.headers().frameOptions().disable();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
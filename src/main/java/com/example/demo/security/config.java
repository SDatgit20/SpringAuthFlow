package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.jwt.jwtAuthFilter;

@Configuration
@EnableWebSecurity
public class config extends WebSecurityConfigurerAdapter {
	@Autowired
    private cuds cuds;
	@Autowired
	private jwtAuthFilter jwtFilter;
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
     authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
    	auth.userDetailsService(cuds);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf()
        .disable()
        .cors()
        .disable()
        .authorizeRequests()
        .antMatchers("/login").permitAll()
        .antMatchers("/register").permitAll()
        //.antMatchers("/edit").permitAll()
        .antMatchers("./UserStatus").permitAll()
        .antMatchers("/getUser").permitAll()
        .anyRequest().authenticated()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
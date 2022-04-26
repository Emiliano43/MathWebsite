package com.example.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity config) throws Exception {
        config
                .authorizeRequests()
                .antMatchers("/home").permitAll()
                .antMatchers("/feedback").permitAll()
                .antMatchers("/feedback/editor").hasRole("EDITOR")
                .antMatchers("/feedback/editor/delete").hasRole("EDITOR")
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/feedback/editor").permitAll()
                .and()
                .logout().logoutUrl("/logout").permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("EDITOR");
    }
}

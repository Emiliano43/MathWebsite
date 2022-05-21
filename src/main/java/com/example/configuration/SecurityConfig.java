package com.example.configuration;

import com.example.services.UserService;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()

                //Доступ только для не зарегистрированных пользователей
                .antMatchers("/registration").not().fullyAuthenticated()


                .antMatchers("/home").permitAll()
                .antMatchers("/feedback").permitAll()
                .antMatchers("/feedback/editor").hasRole("USER")
                .antMatchers("/feedback/editor").hasRole("ADMIN")
                .antMatchers("/feedback/editor/delete").hasRole("ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")


                //Все остальные страницы требуют аутентификации
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/admin/home")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll()
                    .logoutSuccessUrl("/home");
    }

    @Autowired
    public void configureAdmin(AuthenticationManagerBuilder builder) throws Exception {
        builder.inMemoryAuthentication()
                .withUser("admin").password("{noop}admin").roles("ADMIN");
    }
}

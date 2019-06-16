package com.rhapps.show_of_hands.showofhands.config;


import com.rhapps.show_of_hands.showofhands.repository.UsersRepository;
import com.rhapps.show_of_hands.showofhands.service.CustomUserDetailService;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableMongoRepositories(basePackageClasses = UsersRepository.class)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private CustomUserDetailService userDetailsService;

    public SecurityConfiguration(CustomUserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
        .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/**/secured/**").authenticated()
                .anyRequest().permitAll()
                .and().formLogin().permitAll();
    }

    private PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

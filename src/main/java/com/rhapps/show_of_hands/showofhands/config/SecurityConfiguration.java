package com.rhapps.show_of_hands.showofhands.config;


import com.rhapps.show_of_hands.showofhands.model.Usermodels.CustomUserDetails;
import com.rhapps.show_of_hands.showofhands.repository.UsersRepository;
import com.rhapps.show_of_hands.showofhands.security.JwtAuthenticationEntryPoint;
import com.rhapps.show_of_hands.showofhands.security.JwtAuthenticationFilter;
//import com.rhapps.show_of_hands.showofhands.security.JwtAuthorizationFilter;
import com.rhapps.show_of_hands.showofhands.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableMongoRepositories(basePackageClasses = UsersRepository.class)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private CustomUserDetailService userDetailsService;
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }


    public SecurityConfiguration(CustomUserDetailService userDetailsService, JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    public static String getUser() throws UsernameNotFoundException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails obj = (CustomUserDetails) auth.getPrincipal();
            System.out.println(auth.isAuthenticated());
            return obj.getUsername();
        } catch (Exception e) {
            throw new UsernameNotFoundException("user not found");
        }

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //.anyRequest().authenticated()
                .authorizeRequests()
                .antMatchers("/**/polls/**").authenticated()
                .anyRequest().permitAll();
                //.and().formLogin().loginPage("/userLogin").permitAll()\


//        http.addFilter(new JwtAuthenticationFilter(authenticationManager()))
//                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService));
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);



        http.cors();
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

//        http.logout().deleteCookies("JSESSIONID")
//                .and()
//                .rememberMe().key("uniqueAndSecret");
    }


    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

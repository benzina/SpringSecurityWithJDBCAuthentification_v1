package com.dev.springboot.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)  //creates database connection
                .usersByUsernameQuery("select user_name,user_pwd,user_enabled from user where user_name=?")
                .authoritiesByUsernameQuery("select user_name,user_role from user where user_name=?")
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/home").permitAll()
                .antMatchers("/welcome").authenticated()
                .antMatchers("/admin").hasAuthority("ADMIN")
                .antMatchers("/emp").hasAuthority("EMPLOYEE")
                .antMatchers("/mgr").hasAuthority("MANAGER")
                .antMatchers("/common").hasAnyAuthority("EMPLOYEE","MANAGER")
                // Any other URLs which are not configured in above antMatchers
                // generally declared aunthenticated() in real time
                .anyRequest().authenticated()
                //Login Form Details
                .and()
                .formLogin()
                .defaultSuccessUrl("/welcome", true)
                //Logout Form Details
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                //Exception Details
                .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied")
        ;
    }
}

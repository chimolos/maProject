package com.second.maproject;

import com.second.maproject.users.security.jwt.AuthEntryPointJwt;
import com.second.maproject.users.security.jwt.AuthorizationFilter;
import com.second.maproject.users.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;


    @Bean
    public AuthorizationFilter authorizationJwtTokenFilter() {
        return new AuthorizationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors();
        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
        http.sessionManagement().sessionCreationPolicy(STATELESS);

//        http.authorizeRequests().antMatchers("/**").permitAll();
        http.authorizeRequests().antMatchers("/user_profilePics/**").permitAll();
        http.authorizeRequests().antMatchers("/postUploads/**").permitAll();

        http.authorizeRequests().antMatchers("/api/all/**").permitAll();
        http.authorizeRequests().antMatchers("/test/all").permitAll();
//        http.authorizeRequests().antMatchers("/api/post/all/**").permitAll();
//        http.authorizeRequests().antMatchers("/api/comment/all/**").permitAll();

//        http.authorizeRequests().antMatchers("/api/category/**").hasAnyAuthority("ADMIN");
//        http.authorizeRequests().antMatchers("/api/categories/**").hasAnyAuthority("USER", "ADMIN");
//        http.authorizeRequests().antMatchers("/api/post/**").hasAnyAuthority("USER", "ADMIN");
//        http.authorizeRequests().antMatchers("/api/comment/**").hasAnyAuthority("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/api/admin/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers("/api/user/**").hasAnyAuthority("USER", "ADMIN");
        http.authorizeRequests().antMatchers(GET,"/test/user").hasAnyAuthority("USER", "ADMIN");
        http.authorizeRequests().antMatchers(GET, "/test/admin").hasAnyAuthority("ADMIN");

        http.authorizeRequests().anyRequest().authenticated();

        http.addFilterBefore(authorizationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}

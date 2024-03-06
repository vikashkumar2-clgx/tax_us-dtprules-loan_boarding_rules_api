package com.corelogic.tax.tpd.taxservicingrulesapi.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    RequestMatcher matcher = new AntPathRequestMatcher("/api/autoDecision/taxFactLoanBoarding", HttpMethod.PATCH.toString());
    RequestMatcher refreshEndPointMatcher = new AntPathRequestMatcher("/manage/refresh", HttpMethod.POST.toString());

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .ignoringRequestMatchers(matcher)
                .ignoringRequestMatchers(refreshEndPointMatcher)
                .and()
                .authorizeRequests().anyRequest().permitAll();
    }
}
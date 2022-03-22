package com.mediaiq.caps.platform.scheduling.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mediaiq.caps.platform.scheduling.filter.HeaderValidatorFilter;
import com.mediaiq.caps.platform.scheduling.filter.HostnameHeaderFilter;

@Configuration
public class FilterRegistrationConfig {

  @Bean
  public FilterRegistrationBean<HeaderValidatorFilter> headerValidatorFilter() {
    FilterRegistrationBean<HeaderValidatorFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new HeaderValidatorFilter());
    registrationBean.addUrlPatterns("*");
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<HostnameHeaderFilter> hostnameHeaderFilter() {
    FilterRegistrationBean<HostnameHeaderFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new HostnameHeaderFilter());
    registrationBean.addUrlPatterns("*");
    return registrationBean;
  }
}
package com.mUI.microserviceUI.config;

import com.mUI.microserviceUI.filters.RestrictionAdminFilterConfig;
import com.mUI.microserviceUI.filters.RestrictionFilterConfig;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class WebConfig{
    //Register RestrictionFilterConfig
    @Bean
    public FilterRegistrationBean<RestrictionFilterConfig> restrictionFilter() {
        FilterRegistrationBean<RestrictionFilterConfig> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new RestrictionFilterConfig());
        filterRegBean.addUrlPatterns("/*");
        filterRegBean.setOrder(Ordered.LOWEST_PRECEDENCE -1);
        return filterRegBean;
    }

    //Register RestrictionAdminFilterConfig
    @Bean
    public FilterRegistrationBean<RestrictionAdminFilterConfig> adminRestrictionFilter() {
        FilterRegistrationBean<RestrictionAdminFilterConfig> filterRegBean = new FilterRegistrationBean<>();
        filterRegBean.setFilter(new RestrictionAdminFilterConfig());
        filterRegBean.addUrlPatterns("/admin/*");
        filterRegBean.setOrder(Ordered.LOWEST_PRECEDENCE -2);
        return filterRegBean;
    }

}

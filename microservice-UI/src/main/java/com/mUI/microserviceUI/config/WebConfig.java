package com.mUI.microserviceUI.config;

import com.mUI.microserviceUI.filters.RestrictionFilterConfig;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

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

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        return resolver;
    }

    public static class CommonsMultipartResolverMine extends CommonsMultipartResolver {

        @Override
        public boolean isMultipart(HttpServletRequest request) {
            final String header = request.getHeader("Content-Type");
            if(header == null){
                return false;
            }
            return header.contains("multipart/form-data");
        }

    }
}

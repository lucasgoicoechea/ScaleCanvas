package com.scalecanvas.config;

import com.scalecanvas.scenario.domain.CloudPricingCatalog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class CloudPricingConfig {

    @Bean
    CloudPricingCatalog cloudPricingCatalog() {
        return CloudPricingCatalog.defaults();
    }
}

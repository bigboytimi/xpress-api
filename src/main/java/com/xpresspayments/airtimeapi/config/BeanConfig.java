package com.xpresspayments.airtimeapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Timi Olowookere
 * @since 6 Oct 2024
 */
@Configuration
public class BeanConfig {
    /**
     * create a rest template bean for calling external apis
     * @return RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

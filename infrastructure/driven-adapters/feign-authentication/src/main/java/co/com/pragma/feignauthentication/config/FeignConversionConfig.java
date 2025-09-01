package co.com.pragma.feignauthentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.FormattingConversionService;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@Configuration
@EnableReactiveFeignClients(basePackages = "co.com.pragma")

public class FeignConversionConfig {

    @Bean
    public ConversionService feignConversionService() {
        return new FormattingConversionService();
    }
}

package com.altawfik.springbattleshipsapi.config;

import com.altawfik.springbattleshipsapi.service.ShipConfigurationProvider;
import com.altawfik.springbattleshipsapi.service.shipconfig.StandardShipConfigurationProvider;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jackson2ObjectMapperBuilder -> {
            jackson2ObjectMapperBuilder.serializationInclusion(Include.NON_NULL);
            jackson2ObjectMapperBuilder.featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
            jackson2ObjectMapperBuilder.featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
            jackson2ObjectMapperBuilder.featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES);
        };
    }

    @Bean
    public ShipConfigurationProvider shipConfigurationProvider() {
        return new StandardShipConfigurationProvider();
    }
}

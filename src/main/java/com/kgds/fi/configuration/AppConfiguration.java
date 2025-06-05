package com.kgds.fi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Configuration
public class AppConfiguration {
    @Value("${account.service.url}")
    private String accountServiceUrl;

    //create advance webclient bean
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(accountServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public DateTimeFormatter isoLocalDateFormatter() {
        return DateTimeFormatter.ISO_LOCAL_DATE;
    }

    @Bean
    public DateTimeFormatter extendedDateFormatter() {
        return DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.US);
    }
    //create a datasource for my sql database using DataSourceBuilder
    // @Bean
    // @ConfigurationProperties(prefix = "spring.datasource")
    // public DataSource dataSource() {
    //     return DataSourceBuilder.create().build();
    // }

    //create an advance Webclient













}

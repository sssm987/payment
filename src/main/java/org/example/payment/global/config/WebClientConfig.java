package org.example.payment.global.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;


@Configuration
public class WebClientConfig {

    private static final Duration RESPONSE_TIMEOUT = Duration.ofSeconds(5);
    private static final int CONNECT_TIMEOUT = 5000;

    @Value("${pg.url}")
    private String pgUrl;

    @Bean
    public WebClient webClientPg(){
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(RESPONSE_TIMEOUT)  // 응답 전체 타임아웃 30초
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT); // 연결 타임아웃 5초

        return WebClient.builder()
                .baseUrl(pgUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Charset", "UTF-8")
                .build();
    }
}

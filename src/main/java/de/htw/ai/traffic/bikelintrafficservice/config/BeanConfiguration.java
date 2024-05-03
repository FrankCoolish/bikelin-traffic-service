package de.htw.ai.traffic.bikelintrafficservice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfiguration {

    @Bean("webClientViz")
    public WebClient getVizWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.viz.berlin.de/daten/baustellen_sperrungen.json")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    @Bean("webClientTomTom")
    public WebClient getTomTomWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("")
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    @Bean("objectMapper")
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return objectMapper;
    }

}

package com.homecleaningsg.t1.is442_cleaning_scheduler.routing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OdMatrixService {

    private final WebClient webClient;
    private final String apiKey;

    public OdMatrixService(WebClient.Builder webClientBuilder, @Value("${google.maps.api.key}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl("https://maps.googleapis.com").build();
        this.apiKey = apiKey;
    }

    public Mono<OdMatrixResponse> getOdMatrix(List<String> origins, List<String> destinations) {
        return webClient.get()
                .uri(uriBuilder -> {
                    String uri = uriBuilder
                            .path("/maps/api/distancematrix/json")
                            .queryParam("origins", String.join("|", origins))
                            .queryParam("destinations", String.join("|", destinations))
                            .queryParam("key", this.apiKey)
                            .build()
                            .toString();
                    System.out.println(uri);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(OdMatrixResponse.class);
    }
}

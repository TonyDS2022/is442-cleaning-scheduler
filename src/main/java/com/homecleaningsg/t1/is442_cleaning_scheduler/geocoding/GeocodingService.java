package com.homecleaningsg.t1.is442_cleaning_scheduler.geocoding;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GeocodingService {

    private final WebClient webClient;

    public GeocodingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.onemap.gov.sg").build();
    }

    public Mono<GeocodingResponse> getGeocoding(String postalCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/common/elastic/search")
                        .queryParam("searchVal", postalCode)
                        .queryParam("returnGeom", "Y")
                        .queryParam("getAddrDetails", "Y")
                        .build())
                .retrieve()
                .bodyToMono(GeocodingResponse.class);
    }

    public Mono<GeocodingResponse.GeocodingResult> getCoordinates(String postalCode) {
        return getGeocoding(postalCode)
                .flatMap(response -> {
                    if (response.getFound() > 0) {
                        return Mono.just(response.getResults().get(0));
                    }
                    return Mono.empty();
                });
    }
}

package com.homecleaningsg.t1.is442_cleaning_scheduler.routing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OdMatrixResponse {
    @JsonProperty("destination_addresses")
    private List<String> destinationAddresses;

    @JsonProperty("origin_addresses")
    private List<String> originAddresses;

    private List<Row> rows;
    private String status;

    @Data
    public static class Row {
        private List<Element> elements;
    }

    @Data
    public static class Element {
        private Distance distance;
        private Duration duration;
        private String status;
    }

    @Data
    public static class Distance {
        private String text;
        private long value;
    }

    @Data
    public static class Duration {
        private String text;
        private long value;
    }
}
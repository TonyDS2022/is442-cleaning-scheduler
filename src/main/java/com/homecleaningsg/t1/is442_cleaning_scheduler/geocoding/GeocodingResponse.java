package com.homecleaningsg.t1.is442_cleaning_scheduler.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GeocodingResponse {
    private int found;
    private int totalNumPages;
    private int pageNum;
    private List<GeocodingResult> results;

    @Data
    public static class GeocodingResult {
        @JsonProperty("SEARCHVAL")
        private String searchValue;

        @JsonProperty("BLK_NO")
        private String blockNumber;

        @JsonProperty("ROAD_NAME")
        private String roadName;

        @JsonProperty("BUILDING")
        private String building;

        @JsonProperty("ADDRESS")
        private String address;

        @JsonProperty("POSTAL")
        private String postalCode;

        @JsonProperty("X")
        private double xCoordinate;

        @JsonProperty("Y")
        private double yCoordinate;

        @JsonProperty("LATITUDE")
        private double latitude;

        @JsonProperty("LONGITUDE")
        private double longitude;
    }

}

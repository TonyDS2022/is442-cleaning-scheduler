package com.homecleaningsg.t1.is442_cleaning_scheduler.geocoding;

import lombok.Data;

import java.util.List;

@Data
public class GeocodingResponse {
    private int found;
    private int totalNumPages;
    private int pageNum;
    private List<GeocodingResult> results;
}

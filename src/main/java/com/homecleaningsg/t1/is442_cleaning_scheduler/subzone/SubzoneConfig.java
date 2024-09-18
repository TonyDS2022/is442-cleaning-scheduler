package com.homecleaningsg.t1.is442_cleaning_scheduler.subzone;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.File;
import java.util.Map;

@Configuration
public class SubzoneConfig {

    @Bean
    @Order(3)
    CommandLineRunner subzoneCommandLineRunner(SubzoneRepository subzoneRepository) {
        return args -> {

            File subzoneGeojson = new File("MasterPlan2019SubzoneBoundaryNoSeaGEOJSON.geojson");
            ObjectMapper objectMapper = new ObjectMapper();
            FeatureCollection featureCollection = objectMapper.readValue(subzoneGeojson, FeatureCollection.class);

            GeometryFactory geometryFactory = new GeometryFactory();

            for (Feature feature: featureCollection.getFeatures()) {
                Map<String, Object> properties = feature.getProperties();

                // Extract HTML String from Description property
                String descriptionHtml = (String) properties.get("Description");

                // Parse HTML String
                Document doc = Jsoup.parse(descriptionHtml);

                // Find all rows in the table
                Elements rows = doc.select("tr");

                String subzoneName = null;
                String planningAreaName = null;
                String regionName = null;

                // Search for target rows
                for (Element row: rows) {
                    Elements columns = row.select("th, td");

                    if (columns.size() == 2) {
                        String key = columns.get(0).text().trim();
                        String value = columns.get(1).text().trim();

                        // Check for specific keys
                        switch (key) {
                            case "SUBZONE_N":
                                subzoneName = value;
                                break;
                            case "PLN_AREA_N":
                                planningAreaName = value;
                                break;
                            case "REGION_N":
                                regionName = value;
                                break;
                        }
                    }
                }

                System.out.println(subzoneName);
                System.out.println(planningAreaName);
                System.out.println(regionName);
            }
        };
    }
}

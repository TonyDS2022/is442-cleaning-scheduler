package com.homecleaningsg.t1.is442_cleaning_scheduler.subzone;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.File;
import java.util.List;
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

                // cache geometry to geojson polygon
                org.geojson.GeoJsonObject subzoneGeometryDto = feature.getGeometry();
                org.locationtech.jts.geom.Geometry subzoneGeometry = null;

                // Check if the geometry is a Polygon or MultiPolygon
                if (subzoneGeometryDto instanceof org.geojson.Polygon) {
                    // Convert GeoJSON Polygon to JTS Polygon
                    org.geojson.Polygon geoJsonPolygon = (org.geojson.Polygon) subzoneGeometryDto;
                    List<LngLatAlt> coordinates = geoJsonPolygon.getExteriorRing();

                    Coordinate[] jtsCoordinates = coordinates.stream()
                            .map(c -> new Coordinate(c.getLongitude(), c.getLatitude()))
                            .toArray(Coordinate[]::new);

                    LinearRing linearRing = geometryFactory.createLinearRing(jtsCoordinates);
                    subzoneGeometry = geometryFactory.createPolygon(linearRing);

                } else if (subzoneGeometryDto instanceof org.geojson.MultiPolygon) {
                    // Convert GeoJSON MultiPolygon to JTS MultiPolygon
                    org.geojson.MultiPolygon geoJsonMultiPolygon = (org.geojson.MultiPolygon) subzoneGeometryDto;

                    List<org.locationtech.jts.geom.Polygon> jtsPolygons = geoJsonMultiPolygon.getCoordinates().stream()
                            .map(polygonCoordinates -> {
                                List<Coordinate> coordinates = polygonCoordinates.get(0).stream()
                                        .map(c -> new Coordinate(c.getLongitude(), c.getLatitude()))
                                        .toList();

                                LinearRing linearRing = geometryFactory.createLinearRing(coordinates.toArray(new Coordinate[0]));
                                return geometryFactory.createPolygon(linearRing);
                            })
                            .toList();

                    // Convert the list of JTS Polygons to a JTS MultiPolygon
                    subzoneGeometry = geometryFactory.createMultiPolygon(jtsPolygons.toArray(new org.locationtech.jts.geom.Polygon[0]));
                }

                if (subzoneName != null && planningAreaName != null && regionName != null && subzoneGeometry != null) {
                    Subzone subzone = new Subzone(subzoneName, planningAreaName, regionName, subzoneGeometry);
                }
            }
        };
    }
}

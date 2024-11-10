//package com.homecleaningsg.t1.is442_cleaning_scheduler.Admin;
//
//import com.homecleaningsg.t1.is442_cleaning_scheduler.admin.CSVExporter;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class CSVExporterTest {
//
//    // Sample data class with Lombok annotations
//    @Getter
//    @AllArgsConstructor
//    public static class SampleData {
//        private String name;
//        private int age;
//        private double score;
//    }
//
//    private List<SampleData> sampleDataList;
//
//    @BeforeEach
//    public void setup() {
//        // Initialize sample data for testing
//        sampleDataList = Arrays.asList(
//                new SampleData("Alice", 30, 85.5),
//                new SampleData("Bob", 25, 92.0),
//                new SampleData("Charlie", 28, 78.0)
//        );
//    }
//
//    @Test
//    public void testExportToCsv() {
//        // Call the export function
//        byte[] csvBytes = CSVExporter.exportStatisticsToCsv(sampleDataList);
//        String csvOutput = new String(csvBytes, StandardCharsets.UTF_8);
//
//
//
//        // Expected CSV output content
//        String expectedHeader = "name,age,score";
//        String expectedRow1 = "Alice,30,85.5";
//        String expectedRow2 = "Bob,25,92.0";
//        String expectedRow3 = "Charlie,28,78.0";
//
//        // Verify the CSV content
//        assertTrue(csvOutput.contains(expectedHeader), "CSV should contain header");
//        assertTrue(csvOutput.contains(expectedRow1), "CSV should contain row for Alice");
//        assertTrue(csvOutput.contains(expectedRow2), "CSV should contain row for Bob");
//        assertTrue(csvOutput.contains(expectedRow3), "CSV should contain row for Charlie");
//
//        // Save the CSV content to a file
//        try (FileOutputStream fos = new FileOutputStream("src/output/output.csv")) {
//            fos.write(csvBytes);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}

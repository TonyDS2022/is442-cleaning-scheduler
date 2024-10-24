package com.homecleaningsg.t1.is442_cleaning_scheduler.medicalrecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v0.1/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    // Endpoint to simulate the saving of fake image metadata
    @PostMapping("/upload-medical-img")
    public ResponseEntity<Map<String, Object>> uploadMedicalRecord(
            @RequestParam("file") MultipartFile file,
            @RequestParam String mcStartDate,
            @RequestParam String mcEndDate) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }


        // Convert the string dates to LocalDate
        OffsetDateTime clean_mcStartDate = OffsetDateTime.parse(mcStartDate);
        OffsetDateTime clean_mcEndDate = OffsetDateTime.parse(mcEndDate);

        // Call the service to save the image metadata and get the generated mcId
        String mcId = medicalRecordService.saveImageMetadata(file, clean_mcStartDate, clean_mcEndDate);

        // Create the response map
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Image metadata uploaded successfully!");
        response.put("mcId", mcId);

        // Return the response with a 200 OK status
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to download medical image by mcId
    @GetMapping("/{mcId}/download")
    public ResponseEntity<byte[]> downloadMedicalImage(@PathVariable String mcId) throws IOException {
        byte[] imageData = medicalRecordService.getMedicalImage(mcId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "image/jpeg"); // Adjust based on the content type of the image

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }


    @GetMapping
    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecordService.getMedicalRecords();
    }
}
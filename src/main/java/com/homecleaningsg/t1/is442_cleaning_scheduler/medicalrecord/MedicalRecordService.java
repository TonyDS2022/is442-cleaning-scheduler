package com.homecleaningsg.t1.is442_cleaning_scheduler.medicalrecord;

import com.homecleaningsg.t1.is442_cleaning_scheduler.imagehandler.GoogleImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final GoogleImageService googleImageService;

    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, GoogleImageService googleImageService) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.googleImageService = googleImageService;
    }

    // Save image metadata (mcId is auto-generated)
    public String saveImageMetadata(MultipartFile file, OffsetDateTime mcStartDate, OffsetDateTime mcEndDate) throws IOException {
        // Upload image to Google Cloud Storage and get the unique BlobId
        String uniqueBlobId = googleImageService.uploadImage(file);

        // Store the original filename
        String originalFilename = file.getOriginalFilename();

        // Logic to check for existing filename
        if (medicalRecordRepository.existsByFilename(originalFilename)) {
            throw new IllegalStateException("Filename already exists, please rename the file.");
        }

        // Generate custom mcId
        String customMcId = generateCustomMcId();

        // Save metadata into MedicalRecord
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .mcId(customMcId)
                .blobId(uniqueBlobId)
                .filename(originalFilename)  // Store the original filename
                .mcStartDate(mcStartDate)
                .mcEndDate(mcEndDate)
                .build();

        MedicalRecord savedMedicalRecord = medicalRecordRepository.save(medicalRecord);

        return savedMedicalRecord.getMcId();  // Return mcId after successful save
    }

    public String generateCustomMcId() {
        // Use the current timestamp and convert to Base36 (numbers + lowercase letters)
        long timestamp = System.currentTimeMillis();
        String uniqueId = Long.toString(timestamp, 36).toUpperCase(); // Converts timestamp to Base36 and makes it uppercase
        return "MC" + uniqueId.substring(uniqueId.length() - 8);  // Use last 8 characters
    }

    // Retrieve image metadata by mcId and download the image
    public byte[] getMedicalImage(String mcId) throws IOException {
        // Retrieve the metadata from the database
        MedicalRecord medicalRecord = medicalRecordRepository.findById(mcId)
                .orElseThrow(() -> new IllegalStateException("Medical records not found for mcId: " + mcId));

        // Use the inherited downloadImage method from GoogleImageService to get the image content
        return googleImageService.downloadImage(medicalRecord.getBlobId());
    }

    // Method to retrieve all medical images from the database
    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecordRepository.findAll();
    }
}
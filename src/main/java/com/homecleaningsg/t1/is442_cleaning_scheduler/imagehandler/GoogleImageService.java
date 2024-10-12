package com.homecleaningsg.t1.is442_cleaning_scheduler.imagehandler;

import com.google.cloud.storage.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

@Service
@RequiredArgsConstructor

public abstract class GoogleImageService {

    private final Storage storage = StorageOptions.getDefaultInstance().getService();

    @Value("${cloud.gcp.bucket-name}")
    private String bucketName;

    public String uploadImage(MultipartFile file) throws IOException {
        // Generate unique BlobId
        String uniqueBlobId = NanoIdUtils.randomNanoId();

        BlobId blobId = BlobId.of(bucketName, uniqueBlobId);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        // Try to upload the file
        try {
            Blob blob = storage.create(blobInfo, file.getBytes());
            return blob.getBlobId().getName();  // Return the unique BlobId if successful
        } catch (StorageException e) {
            throw new IOException("Failed to connect to Google Cloud Storage", e);
        }
    }

    public byte[] downloadImage(String blobId) throws IOException {
        if (!StringUtils.hasText(blobId)) {
            throw new IllegalArgumentException("BlobId cannot be null or empty");
        }

        Blob blob = storage.get(BlobId.of(bucketName, blobId));

        if (blob == null || !blob.exists()) {
            throw new IllegalStateException("No file found in Google Cloud Storage for BlobId: " + blobId);
        }

        return blob.getContent();
    }

}
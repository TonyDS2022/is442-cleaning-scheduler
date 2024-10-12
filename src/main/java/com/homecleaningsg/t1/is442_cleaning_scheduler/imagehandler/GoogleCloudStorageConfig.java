package com.homecleaningsg.t1.is442_cleaning_scheduler.imagehandler;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleCloudStorageConfig {

    @Bean
    public Storage storageService() {
        try {
            return StorageOptions.getDefaultInstance().getService();  // Use Google Cloud Storage
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Google Cloud Storage unavailable, using fallback NoOpsStorage.");
            return new NoOpsStorage();  // Fallback to NoOpStorage
        }
    }
}
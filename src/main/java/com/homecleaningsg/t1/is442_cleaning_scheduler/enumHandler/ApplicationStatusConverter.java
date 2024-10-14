package com.homecleaningsg.t1.is442_cleaning_scheduler.enumHandler;

import com.homecleaningsg.t1.is442_cleaning_scheduler.enumHandler.ApplicationStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ApplicationStatusConverter implements AttributeConverter<ApplicationStatus, String> {

    @Override
    public String convertToDatabaseColumn(ApplicationStatus status) {
        if (status == null) {
            return null;
        }
        return status.name(); // Converts enum to its string representation (e.g., "APPROVED", "PENDING", etc.)
    }

    @Override
    public ApplicationStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return ApplicationStatus.valueOf(dbData); // Converts database value back to enum
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unknown value for ApplicationStatus: " + dbData, e);
        }
    }
}

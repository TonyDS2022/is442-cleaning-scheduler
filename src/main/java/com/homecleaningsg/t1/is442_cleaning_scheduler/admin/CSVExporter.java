package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import com.opencsv.CSVWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

public class CSVExporter {
    public static <T> byte[] exportToCsv(List<T> data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos),
                     CSVWriter.DEFAULT_SEPARATOR,
                     CSVWriter.NO_QUOTE_CHARACTER,
                     CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                     CSVWriter.DEFAULT_LINE_END)) {
            if (!data.isEmpty()) {
                // Write header
                Field[] fields = data.get(0).getClass().getDeclaredFields();
                String[] headers = Stream.of(fields)
                        .map(Field::getName)
                        .toArray(String[]::new);
                writer.writeNext(headers);

                // Write each row of data
                for (T row : data) {
                    String[] rowData = Stream.of(fields)
                            .map(field -> {
                                field.setAccessible(true);
                                try {
                                    return field.get(row) != null ? field.get(row).toString() : "";
                                } catch (IllegalAccessException e) {
                                    return "";
                                }
                            })
                            .toArray(String[]::new);
                    writer.writeNext(rowData);
                }
            }
            writer.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV file", e);
        }
    }

}

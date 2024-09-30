package com.homecleaningsg.t1.is442_cleaning_scheduler.seriesJob;

import java.io.Serializable;
import java.util.Objects;

public class SeriesJobId implements Serializable {
    private int series;
    private int jobId;

    // Default constructor
    public SeriesJobId() {}

    // Parameterized constructor
    public SeriesJobId(int series, int jobId) {
        this.series = series;
        this.jobId = jobId;
    }

    // Getters, setters, equals, and hashCode methods
    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeriesJobId that = (SeriesJobId) o;
        return series == that.series && jobId == that.jobId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(series, jobId);
    }
}
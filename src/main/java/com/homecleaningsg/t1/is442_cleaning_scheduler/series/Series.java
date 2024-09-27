package com.homecleaningsg.t1.is442_cleaning_scheduler.series;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table(name = "Series")
public class Series {
    @Id
    @SequenceGenerator(
            name = "series_sequence",
            sequenceName = "series_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "series_sequence"
    )

    @Column(name = "series_id")
    private int seriesId;

    @Column(name = "series_type_id")
    private int seriesTypeId;

    @Column(name = "geolocation_id")
    private int geolocationId;

    @Column(name = "acct_id")
    private int acctId;

    @NonNull
    @Column(name = "package_start")
    private Timestamp packageStart;

    @NonNull
    @Column(name = "package_end")
    private Timestamp packageEnd;

    @NonNull
    @Column(name = "package_comment")
    private String packageComment;

    @NonNull
    @Column(name = "is_ongoing")
    private boolean isOngoing;

    @NonNull
    @Column(name = "price")
    private float price;

    @NonNull
    @Column(name = "pax_assigned")
    private int paxAssigned;

    @NonNull
    @Column(name = "rooms")
    private int rooms;
}
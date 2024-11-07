package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table
public class Worker {
    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_sequence"
    )
    private Long workerId;

    @NonNull
    private String name;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String email;

    @NonNull
    private String phone;

    @NonNull
    private String bio;

    @NonNull
    private LocalTime startWorkingHours;

    @NonNull
    private LocalTime endWorkingHours;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    @NonNull
    private boolean isActive = true;

    @NonNull
    private Timestamp lastModified;

    public Worker(String name,
                  String username,
                  String password,
                  String email,
                  String phone,
                  String bio,
                  LocalTime startWorkingHours,
                  LocalTime endWorkingHours){
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.bio = bio;
        this.startWorkingHours = startWorkingHours;
        this.endWorkingHours = endWorkingHours;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastModified = new Timestamp(System.currentTimeMillis());
    }

}

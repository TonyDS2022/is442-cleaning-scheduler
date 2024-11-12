package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "workerId") // temp solution to prevent infinite recursion
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

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "location_id")
    private Location homeLocation;

    private String homeUnitNumber;

    public Worker(Location homeLocation){
        this.homeLocation = homeLocation;
    }

    // establish relationship with leaveApplications
    @OneToMany(mappedBy = "workerId", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonIgnore
    @JsonBackReference("leaveApplications-worker") // prevent infinite recursion when serializing
    private List<LeaveApplication> leaveApplications = new ArrayList<>();

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("worker-shift")
    private List<Shift> shifts = new ArrayList<>();

    @NonNull
    private boolean isActive = true;

    @NonNull
    private Timestamp lastModified;

    private LocalDate deactivatedAt;

    @NonNull
    private LocalDate joinDate;

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

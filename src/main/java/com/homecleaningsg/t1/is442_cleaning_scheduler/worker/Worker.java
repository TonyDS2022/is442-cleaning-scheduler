package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import com.fasterxml.jackson.annotation.*;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
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

    public Worker(Location homeLocation){
        this.homeLocation = homeLocation;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaveApplicationId", nullable = true)
    private LeaveApplication leaveApplication;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Shift> shifts;

    // // establish relationship with leaveApplications
    // @OneToMany(mappedBy = "workerId", cascade = CascadeType.ALL, orphanRemoval = true)
    // // @JsonIgnore
    // @JsonBackReference // prevent infinite recursion when serializing
    // private List<LeaveApplication> leaveApplications;

    @NonNull
    private boolean isActive = true;

    @NonNull
    private Timestamp lastModified;

    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }

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

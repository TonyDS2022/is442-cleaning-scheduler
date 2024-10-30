package com.homecleaningsg.t1.is442_cleaning_scheduler.worker;

import com.fasterxml.jackson.annotation.*;
import com.homecleaningsg.t1.is442_cleaning_scheduler.leaveapplication.LeaveApplication;
import com.homecleaningsg.t1.is442_cleaning_scheduler.location.Location;
import com.homecleaningsg.t1.is442_cleaning_scheduler.shift.Shift;
import com.homecleaningsg.t1.is442_cleaning_scheduler.contract.Contract;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    // // establish relationship with leaveApplications
    // @OneToMany(mappedBy = "workerId", cascade = CascadeType.ALL, orphanRemoval = true)
    // // @JsonIgnore
    // @JsonBackReference // prevent infinite recursion when serializing
    // private List<LeaveApplication> leaveApplications;

    // @OneToMany(mappedBy = "workerId", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonBackReference
    // private List<Shift> shifts;

    // @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonBackReference
    // private List<Contract> contracts;

}

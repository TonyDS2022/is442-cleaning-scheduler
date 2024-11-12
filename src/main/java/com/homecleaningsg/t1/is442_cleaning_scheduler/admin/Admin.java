package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Entity
@Table
public class Admin {
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
    private Long adminId;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private boolean isRootAdmin;

    @NonNull
    private Timestamp lastModified;

    @NonNull
    private boolean isActive = true;

    @NonNull
    private LocalDate joinDate;

    private LocalDate deactivatedAt;

    public Admin(String username,
                 String password,
                 boolean isRootAdmin){
        this.username = username;
        this.password = password;
        this.isRootAdmin = isRootAdmin;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastModified = new Timestamp(System.currentTimeMillis());
    }
}

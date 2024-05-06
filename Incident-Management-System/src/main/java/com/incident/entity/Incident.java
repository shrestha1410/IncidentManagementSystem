package com.incident.entity;

import com.incident.utils.IncidentIdGenerator;
import com.incident.utils.Priority;
import com.incident.utils.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Incident {
    @Id
    private  String id;

    public Incident() {
        this.id = IncidentIdGenerator.generateUniqueId();
    }
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column
    @Enumerated(EnumType.STRING)
    private com.incident.utils.Enterprise enterprise;
    @Column
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Column
    private String details;
    @Column
    private String reporterName;
    @Column
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
    @ManyToOne
    private UserInfo userId;

}

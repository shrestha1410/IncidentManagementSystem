package com.incident.dto;

import com.incident.entity.UserInfo;
import com.incident.utils.Enterprise;
import com.incident.utils.Priority;
import com.incident.utils.Status;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class IncidentResponseDto {
    private  String id;
    private Status status;
    private Enterprise enterprise;
    private Priority priority;
    private String details;
    private String reporterName;
    private String updatedAt;
    private String createdAt;
}

package com.incident.dto;

import com.incident.utils.Enterprise;
import com.incident.utils.Priority;
import com.incident.utils.Status;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class IncidentDto {
    private String id;
    private Status status;
    private Enterprise enterprise;
    private Priority priority;
    private String details;
}

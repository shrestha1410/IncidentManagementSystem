package com.incident.service;

import com.incident.dto.IncidentDto;
import com.incident.dto.IncidentResponseDto;

import java.util.List;

public interface IncidentService {
     List<IncidentResponseDto> allIncident(String userName);
     void create(IncidentDto incidentDto,String userName);
     void update(IncidentDto incidentDto,String userName);
     IncidentResponseDto search(String incidentId);
}

package com.incident.service;

import com.incident.dto.IncidentDto;
import com.incident.dto.IncidentResponseDto;
import com.incident.entity.Incident;
import com.incident.entity.UserInfo;
import com.incident.exception.ServiceException;
import com.incident.repository.IncidentRepository;
import com.incident.repository.UserinfoRepository;
import com.incident.utils.ErrorCodes;
import com.incident.utils.IncidentIdGenerator;
import com.incident.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentServiceImpl implements IncidentService {
    private final UserinfoRepository userDetailsRepository;
    private final IncidentRepository incidentRepository;

    @Override
    public List<IncidentResponseDto> allIncident(String userName) {
        Optional<UserInfo> userDetails = userDetailsRepository.findByEmail(userName);
        UserInfo userInfo = Optional.ofNullable(userDetails).get().orElse(null);
        List<IncidentResponseDto> incidentResponseDto = new ArrayList<>();
        if (userInfo != null) {
            try {
                List<Incident> incidentList = incidentRepository.findByUserId(userInfo.getId());
                if (incidentList != null && !incidentList.isEmpty()) {
                    for (Incident incident : incidentList) {
                        IncidentResponseDto.IncidentResponseDtoBuilder incidentResponseDtoBuilder = IncidentResponseDto.builder();
                        incidentResponseDtoBuilder
                                .id(incident.getId())
                                .details(incident.getDetails())
                                .status(incident.getStatus())
                                .priority(incident.getPriority())
                                .enterprise(incident.getEnterprise())
                                .reporterName(userInfo.getFirstName() + " " + userInfo.getLastName())
                                .updatedAt(Optional.ofNullable(incident.getUpdatedAt()).map(Object::toString).orElse(null))
                                .createdAt(Optional.ofNullable(incident.getCreatedAt()).map(Object::toString).orElse(null));
                        incidentResponseDto.add(incidentResponseDtoBuilder.build());
                    }
                }
            } catch (Exception e) {
                log.error("Not Found " + e);
            }
            return incidentResponseDto;
        } else {
            throw new ServiceException(HttpStatus.NOT_FOUND, ErrorCodes.NOT_FOUND, "User Not found");
        }
    }

    @Override
    public void create(IncidentDto incidentDto, String userName) {
        Optional<UserInfo> userDetails = userDetailsRepository.findByEmail(userName);
        UserInfo userInfo = Optional.ofNullable(userDetails).get().orElse(null);
        if (userInfo != null) {
            Incident.IncidentBuilder incidentBuilder = Incident.builder()
                    .id(IncidentIdGenerator.generateUniqueId())
                    .status(incidentDto.getStatus())
                    .enterprise(incidentDto.getEnterprise())
                    .priority(incidentDto.getPriority())
                    .details(incidentDto.getDetails())
                    .reporterName(userInfo.getFirstName() + " " + userInfo.getLastName())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .userId(userInfo);
            incidentRepository.save(incidentBuilder.build());
            log.info("Incident created successfully");
        }
    }

    @Override
    public void update(IncidentDto incidentDto, String userName) {
        Optional<UserInfo> userDetails = userDetailsRepository.findByEmail(userName);
        UserInfo userInfo = Optional.ofNullable(userDetails).get().orElse(null);
        Optional<Incident> incidentOptional = incidentRepository.findById(incidentDto.getId());
        Incident incident = Optional.ofNullable(incidentOptional).get().orElse(null);
        if (userInfo != null && incident != null) {
            if (!incident.getStatus().equals(Status.CLOSED)) {
                Incident.IncidentBuilder incidentBuilder = Incident.builder()
                        .id(incident.getId())
                        .status(incidentDto.getStatus())
                        .enterprise(incidentDto.getEnterprise())
                        .priority(incidentDto.getPriority())
                        .details(incidentDto.getDetails())
                        .reporterName(userInfo.getFirstName() + " " + userInfo.getLastName())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .userId(userInfo);
                incidentRepository.save(incidentBuilder.build());
                log.info("Incident updated successfully");
            } else {
                throw new ServiceException(HttpStatus.BAD_REQUEST, ErrorCodes.BAD_REQUEST, "Incident is not editable");
            }
        } else {
            throw new ServiceException(HttpStatus.BAD_REQUEST, ErrorCodes.BAD_REQUEST, "Recoded doesn't exist with incident Id:" + incidentDto.getId());
        }
    }

    @Override
    public IncidentResponseDto search(String incidentId) {
        Optional<Incident> incidentOptional = incidentRepository.findById(incidentId);
        Incident incident = Optional.ofNullable(incidentOptional).get().orElse(null);
        if (incident != null) {
            IncidentResponseDto.IncidentResponseDtoBuilder incidentBuilder = IncidentResponseDto.builder()
                    .id(incident.getId())
                    .status(incident.getStatus())
                    .enterprise(incident.getEnterprise())
                    .priority(incident.getPriority())
                    .details(incident.getDetails())
                    .reporterName(incident.getReporterName())
                    .updatedAt(Optional.ofNullable(incident.getUpdatedAt()).map(Object::toString).orElse(null))
                    .createdAt(Optional.ofNullable(incident.getCreatedAt()).map(Object::toString).orElse(null));
            return incidentBuilder.build();
        } else {
            throw new ServiceException(HttpStatus.NOT_FOUND, ErrorCodes.NOT_FOUND, "Not found Record with incident Id" + incidentId);
        }
    }
}

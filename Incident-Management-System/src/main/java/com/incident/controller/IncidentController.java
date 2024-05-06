package com.incident.controller;

import com.incident.dto.IncidentDto;
import com.incident.dto.IncidentResponseDto;
import com.incident.service.IncidentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incident")
public class IncidentController {
    private final IncidentService incidentService;
    @PostMapping("/view")
    public List<IncidentResponseDto> allIncidentsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            System.out.println("User '" + username + "' is authenticated.");
            return incidentService.allIncident(userDetails.getUsername());
        }
        return List.of();
    }
    @PostMapping("/create")
    public void incidentCreate(@RequestBody IncidentDto incidentDto, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request){
        incidentService.create(incidentDto,userDetails.getUsername());
    }
    @PostMapping("/edit")
    public void incidentEdit(@RequestBody IncidentDto incidentDto, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request){
        incidentService.update(incidentDto,userDetails.getUsername());
    }
    @GetMapping("{incidentId}")
    public IncidentResponseDto incidentSearch(@PathVariable String incidentId){
        return incidentService.search(incidentId);
    }

}

package com.incident.dto;

import com.incident.entity.Incident;
import jakarta.persistence.*;
import lombok.*;

import javax.annotation.processing.Generated;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserDetailsDto {
    private Long id;
    private String psw;
    private String firstName;
    private String lastName;
    private String Enterprise;
    private String email;
    private  String address;
    private String country;
    private String state;
    private String city;
    private String pincode;
    private String mobile;
    private  String fax;
    private String phoneNo;
}

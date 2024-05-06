package com.incident.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.annotation.processing.Generated;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_details_id")
    @SequenceGenerator(name = "user_details_id", sequenceName = "user_details_id_sequence", allocationSize = 1, initialValue = 10000)
    private Long id;
    @Column
    private String password;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;
    @Column
    private  String address;
    @Column
    private String country;
    @Column
    private String state;
    @Column
    private String city;
    @Column
    private String pincode;
    @Column
    private String mobileNumber;
    @Column
    private  String fax;
    @Column
    private String phoneNo;
    @OneToMany(mappedBy = "id")
    private List<Incident> incident_id;
}

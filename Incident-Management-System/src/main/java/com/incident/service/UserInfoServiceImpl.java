package com.incident.service;

import com.incident.dto.UserDetailsDto;
import com.incident.dto.UserInfoDetails;
import com.incident.entity.UserInfo;
import com.incident.repository.UserinfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserDetailsService {

    @Autowired
    private UserinfoRepository userDetailsRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserInfo> userDetail = userDetailsRepository.findByEmail(username);

        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    public void addUser(UserDetailsDto userDetailsDto) {
        UserInfo.UserInfoBuilder userInfoBuilder = UserInfo.builder();
        userInfoBuilder
                .password(encoder.encode(userDetailsDto.getPsw()))
                .firstName(userDetailsDto.getFirstName())
                .lastName(userDetailsDto.getLastName())
                .email(userDetailsDto.getEmail())
                .address(userDetailsDto.getAddress())
                .country(userDetailsDto.getCountry())
                .state(userDetailsDto.getState())
                .city(userDetailsDto.getCity())
                .pincode(userDetailsDto.getPincode())
                .mobileNumber(userDetailsDto.getMobile())
                .fax(userDetailsDto.getFax())
                .phoneNo(userDetailsDto.getPhoneNo());
        userDetailsRepository.save(userInfoBuilder.build());
        log.info("User Added Successfully");
    }
}

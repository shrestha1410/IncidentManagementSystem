package com.incident.controller;

import com.incident.config.JwtHelper;
import com.incident.dto.JwtRequest;
import com.incident.dto.JwtResponse;
import com.incident.dto.UserDetailsDto;
import com.incident.service.UserInfoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserInfoServiceImpl userDetailsService;

    private final AuthenticationManager manager;

    private final JwtHelper helper;


    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);
        return JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
    }

    private void doAuthenticate(String email, String password) {
     try {
          manager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

    @PostMapping("/register")
    public void addNewUser(@RequestBody UserDetailsDto userInfo) {
       userDetailsService.addUser(userInfo);
    }
}
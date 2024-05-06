package com.incident.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorResponse {
    private String errorCode;
    @JsonAlias("message")
    private String errorMessage;
}

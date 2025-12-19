package com.djokic.applicationservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError{
    private final LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String message;
    private String path;
    private String error;
    private List<String> details;
}

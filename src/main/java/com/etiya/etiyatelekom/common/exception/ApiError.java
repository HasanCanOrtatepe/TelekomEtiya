package com.etiya.etiyatelekom.common.exception;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {

    private OffsetDateTime timestamp;
    private int status;
    private String message;
    private String path;

    private Map<String, String> validationErrors;
}

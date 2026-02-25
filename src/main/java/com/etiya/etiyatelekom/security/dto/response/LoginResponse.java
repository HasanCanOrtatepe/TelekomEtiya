package com.etiya.etiyatelekom.security.dto.response;

import com.etiya.etiyatelekom.common.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private Long userId;
    private String email;
    private UserType userType;
    private List<String> authorities;
}

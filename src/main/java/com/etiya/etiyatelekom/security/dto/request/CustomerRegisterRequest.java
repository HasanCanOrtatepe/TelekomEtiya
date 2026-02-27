package com.etiya.etiyatelekom.security.dto.request;

import com.etiya.etiyatelekom.common.validation.ValidationPatterns;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegisterRequest {

    @NotBlank
    @Size(max = 80)
    private String firstName;

    @NotBlank
    @Size(max = 80)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Pattern(regexp = ValidationPatterns.PHONE)
    @Size(max = 30)
    private String phone;

    @NotBlank()
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank()
    private String confirmPassword;
}

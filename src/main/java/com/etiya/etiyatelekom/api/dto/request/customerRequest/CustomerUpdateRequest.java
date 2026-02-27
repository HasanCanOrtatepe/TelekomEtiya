package com.etiya.etiyatelekom.api.dto.request.customerRequest;

import com.etiya.etiyatelekom.common.validation.ValidationPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerUpdateRequest {

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
}
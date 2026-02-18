package com.etiya.etiyatelekom.api.dto.response.customerResponse;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long id;
    private String customerNo;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

}
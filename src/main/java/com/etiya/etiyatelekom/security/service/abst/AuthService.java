package com.etiya.etiyatelekom.security.service.abst;

import com.etiya.etiyatelekom.api.dto.response.customerResponse.CustomerResponse;
import com.etiya.etiyatelekom.security.dto.request.CustomerRegisterRequest;
import com.etiya.etiyatelekom.security.dto.request.LoginRequest;
import com.etiya.etiyatelekom.security.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    CustomerResponse register(CustomerRegisterRequest request);
}

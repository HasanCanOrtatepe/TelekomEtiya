package com.etiya.etiyatelekom.security.service.impl;

import com.etiya.etiyatelekom.api.dto.response.customerResponse.CustomerResponse;
import com.etiya.etiyatelekom.common.exception.exceptions.InvalidCredentialsException;
import com.etiya.etiyatelekom.common.exception.exceptions.PasswordsDoNotMatchException;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceAlreadyExistsException;
import com.etiya.etiyatelekom.common.mapper.ModelMapperService;
import com.etiya.etiyatelekom.entity.Agent;
import com.etiya.etiyatelekom.entity.Customer;
import com.etiya.etiyatelekom.repository.AgentRepository;
import com.etiya.etiyatelekom.repository.CustomerRepository;
import com.etiya.etiyatelekom.security.dto.request.CustomerRegisterRequest;
import com.etiya.etiyatelekom.security.dto.request.LoginRequest;
import com.etiya.etiyatelekom.security.dto.response.LoginResponse;
import com.etiya.etiyatelekom.security.jwt.JwtUtil;
import com.etiya.etiyatelekom.security.model.AuthenticatedUser;
import com.etiya.etiyatelekom.security.service.abst.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ModelMapperService modelMapperService;

    @Override
    public LoginResponse login(LoginRequest request) {
        AuthenticatedUser authenticatedUser = null;

        Optional<Customer> customerOpt = customerRepository.findByEmail(request.getEmail());
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            if (customer.getPassword() != null && passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
                authenticatedUser = AuthenticatedUser.fromCustomer(customer);
            }
        }

        if (authenticatedUser == null) {
            Optional<Agent> agentOpt = agentRepository.findByEmail(request.getEmail());
            if (agentOpt.isPresent()) {
                Agent agent = agentOpt.get();
                if (agent.getIsActive() == null || !agent.getIsActive()) {
                    throw new InvalidCredentialsException("Hesap aktif değil");
                }
                if (agent.getPassword() == null) {
                    throw new InvalidCredentialsException("Geçersiz hesap yapılandırması");
                }
                if (passwordEncoder.matches(request.getPassword(), agent.getPassword())) {
                    authenticatedUser = AuthenticatedUser.fromAgent(agent);
                }
            }
        }

        if (authenticatedUser == null) {
            throw new InvalidCredentialsException();
        }

        String token = jwtUtil.generateToken(authenticatedUser);

        return LoginResponse.builder()
                .accessToken(token)
                .userId(authenticatedUser.getId())
                .email(authenticatedUser.getEmail())
                .userType(authenticatedUser.getUserType())
                .authorities(authenticatedUser.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public CustomerResponse register(CustomerRegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Customer", "Email", request.getEmail());
        }
        if (agentRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Agent", "Email", request.getEmail());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank() && customerRepository.existsByPhone(request.getPhone())) {
            throw new ResourceAlreadyExistsException("Customer", "Phone", request.getPhone());
        }

        Customer customer = modelMapperService.forRequest().map(request, Customer.class);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setCustomerNo("C" + System.currentTimeMillis());
        customer.setCreatedAt(java.time.OffsetDateTime.now());
        Customer saved = customerRepository.save(customer);
        return modelMapperService.forResponse().map(saved, CustomerResponse.class);
    }
}

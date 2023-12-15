package com.enigma.shopeymart.service.impl;

import com.enigma.shopeymart.constant.ERole;
import com.enigma.shopeymart.dto.request.AuthRequest;
import com.enigma.shopeymart.dto.response.LoginResponse;
import com.enigma.shopeymart.dto.response.RegisterResponse;
import com.enigma.shopeymart.entity.*;
import com.enigma.shopeymart.repository.UserCredentialRepository;
import com.enigma.shopeymart.security.JwtUtil;
import com.enigma.shopeymart.service.AdminService;
import com.enigma.shopeymart.service.AuthService;
import com.enigma.shopeymart.service.CustomerService;
import com.enigma.shopeymart.service.RoleService;
import com.enigma.shopeymart.util.ValidationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationUtil validationUtil;
    private final CustomerService customerService;
    private final RoleService roleService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AdminService adminService;
    @Override
    @Transactional(rollbackOn = Exception.class)
    public RegisterResponse registerCustomer(AuthRequest authRequest) {
        try {

//            todo 1 : set role
            Role role = Role.builder()
                    .name(ERole.ROLE_CUSTOMER)
                    .build();

            role = roleService.getOrSave(role);

//            todo 2 : set credentials
            UserCredential userCredential = UserCredential.builder()
                    .username(authRequest.getUsername())
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .role(role)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

//            todo 3 : set customer
            Customer customer = Customer.builder()
                    .userCredential(userCredential)
                    .email(authRequest.getEmail())
                    .mobilePhone(authRequest.getMobilePhone())
                    .address(authRequest.getAddress())
                    .name(authRequest.getCustomerName())
                    .build();
            customerService.createNewCustomer(customer);

            return RegisterResponse.builder()
                    .username(userCredential.getUsername())
                    .role(userCredential.getRole().getName().toString())
                    .build();


        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"user already exist");
        }
    }

    @Override
    public LoginResponse login(AuthRequest authRequest) {
//        tempat untuk logic login
        validationUtil.valdate(authRequest);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        object App user
        AppUser appUser = (AppUser) authentication.getPrincipal();
        String token = jwtUtil.generateToken(appUser);
        return LoginResponse.builder()
                .token(token)
                .role(appUser.getRole().name())
                .build();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public RegisterResponse registerAdmin(AuthRequest authRequest) {

        try{
//            todo 1 : set role
            Role role = Role.builder()
                    .name(ERole.ROLE_ADMIN)
                    .build();
            role = roleService.getOrSave(role);

//            todo 2 : set credentials

            UserCredential userCredential = UserCredential.builder()
                    .username(authRequest.getUsername())
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .role(role)
                    .build();
            userCredentialRepository.saveAndFlush(userCredential);

//            todo 3 : set admin
            Admin admin = Admin.builder()
                    .phoneNumber(authRequest.getMobilePhone())
                    .name(authRequest.getCustomerName())
                    .userCredential(userCredential)
                    .build();
            adminService.create(admin);

            return RegisterResponse.builder()
                    .username(userCredential.getUsername())
                    .role(role.getName().toString())
                    .build();



        }catch (DataIntegrityViolationException e){
          throw new RuntimeException();
        }

    }

}

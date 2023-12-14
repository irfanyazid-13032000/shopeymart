package com.enigma.shopeymart.controller;

import com.enigma.shopeymart.dto.request.AuthRequest;
import com.enigma.shopeymart.dto.response.CommonResponse;
import com.enigma.shopeymart.dto.response.LoginResponse;
import com.enigma.shopeymart.dto.response.RegisterResponse;
import com.enigma.shopeymart.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/api/auth/register")
    public RegisterResponse register(@RequestBody AuthRequest authRequest){
        return authService.registerCustomer(authRequest);

    }


    @PostMapping("api/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        LoginResponse loginResponse = authService.login(authRequest);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("berhasil login")
                        .data(loginResponse)
                        .build());
    }

}

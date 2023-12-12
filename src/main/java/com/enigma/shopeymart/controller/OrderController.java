package com.enigma.shopeymart.controller;

import com.enigma.shopeymart.dto.request.OrderRequest;
import com.enigma.shopeymart.dto.response.CommonResponse;
import com.enigma.shopeymart.dto.response.OrderResponse;
import com.enigma.shopeymart.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse = orderService.create(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .data(orderResponse)
                        .build());
    }

}

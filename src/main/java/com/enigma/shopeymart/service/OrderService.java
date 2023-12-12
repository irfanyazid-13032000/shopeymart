package com.enigma.shopeymart.service;

import com.enigma.shopeymart.dto.request.OrderRequest;
import com.enigma.shopeymart.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest orderRequest);
    OrderResponse getById(OrderRequest orderRequest);

    List<OrderResponse> getAllOrder(OrderRequest orderRequest);
}

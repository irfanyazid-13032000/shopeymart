package com.enigma.shopeymart.service;

import com.enigma.shopeymart.dto.request.StoreRequest;
import com.enigma.shopeymart.dto.response.StoreResponse;
import com.enigma.shopeymart.entity.Store;

import java.util.List;

public interface StoreService {
    Store create(Store store);
//    Store getById(String id);
    List<StoreResponse> getAll();
    StoreResponse update(StoreRequest storeRequest);
    void delete(String id);

    StoreResponse create(StoreRequest storeRequest);

    StoreResponse getById(String id);

}

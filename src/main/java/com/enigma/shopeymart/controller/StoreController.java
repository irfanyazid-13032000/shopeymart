package com.enigma.shopeymart.controller;

import com.enigma.shopeymart.dto.request.StoreRequest;
import com.enigma.shopeymart.dto.response.StoreResponse;
import com.enigma.shopeymart.entity.Store;
import com.enigma.shopeymart.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @PostMapping("/store")
    public Store save(@RequestBody Store store){
        return storeService.create(store);
    }

    @GetMapping("/store")
    public List<Store> getAll(){
        return storeService.getAll();
    }

    @GetMapping("/store/{id}")
    public Store getById(@PathVariable String id){
        return storeService.getById(id);
    }
    @PutMapping("/store")
    public Store update(@RequestBody Store store){
        return storeService.update(store);
    }

    @DeleteMapping("/store/{id}")
    public void delete(@PathVariable String id){
        storeService.delete(id);
    }

    @PostMapping("/store/v2")
    public StoreResponse createStores(@RequestBody StoreRequest storeRequest){
        return storeService.create(storeRequest);
    }




}

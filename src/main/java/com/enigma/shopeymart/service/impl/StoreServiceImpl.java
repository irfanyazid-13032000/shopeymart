package com.enigma.shopeymart.service.impl;

import com.enigma.shopeymart.dto.request.StoreRequest;
import com.enigma.shopeymart.dto.response.StoreResponse;
import com.enigma.shopeymart.entity.Store;
import com.enigma.shopeymart.repository.StoreRepository;
import com.enigma.shopeymart.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    @Override
    public Store create(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public Store getById(String id) {
        return storeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Store> getAll() {
        return storeRepository.findAll();
    }

    @Override
    public Store update(Store store) {
        if (!Objects.isNull(getById(store.getId()))){
            return storeRepository.save(store);
        }
        return null;
    }

    @Override
    public void delete(String id) {
        storeRepository.deleteById(id);
    }


    public StoreResponse create(StoreRequest storeRequest){
        Store store = Store.builder()
                .name(storeRequest.getName())
                .noSiup(storeRequest.getNoSiup())
                .address(storeRequest.getAddress())
                .mobilePhone(storeRequest.getMobilePhone())
                .build();
        storeRepository.save(store);
        return StoreResponse.builder()
                .address(store.getAddress())
                .storeName(store.getName())
                .build();
    }


}

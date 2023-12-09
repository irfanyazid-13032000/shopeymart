package com.enigma.shopeymart.service.impl;

import com.enigma.shopeymart.dto.request.StoreRequest;
import com.enigma.shopeymart.dto.response.StoreResponse;
import com.enigma.shopeymart.entity.Store;
import com.enigma.shopeymart.repository.StoreRepository;
import com.enigma.shopeymart.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    @Override
    public Store create(Store store) {
        return storeRepository.save(store);
    }

//    @Override
//    public Store getById(String id) {
//        return storeRepository.findById(id).orElse(null);
//    }

    @Override
    public List<StoreResponse> getAll() {
        List<StoreResponse> res = new ArrayList<>();
        List<Store> semua = storeRepository.findAll();
        for (var sem : semua){
           res.add(
                   StoreResponse.builder()
                           .phone(sem.getMobilePhone())
                           .address(sem.getAddress())
                           .storeName(sem.getName())
                           .noSiup(sem.getNoSiup())
                           .id(sem.getId())
                           .build()
           );
        }

        return res;
    }

    @Override
    public StoreResponse update(StoreRequest storeRequest) {
        StoreResponse checkStoreRequest = getById(storeRequest.getId());
        if (!Objects.isNull(checkStoreRequest)){
            Store store = Store.builder()
                    .id(storeRequest.getId())
                    .name(storeRequest.getName())
                    .noSiup(storeRequest.getNoSiup())
                    .address(storeRequest.getAddress())
                    .mobilePhone(storeRequest.getMobilePhone())
                    .build();
            storeRepository.save(store);
            return StoreResponse.builder()
                    .id(store.getId())
                    .storeName(store.getName())
                    .phone(store.getMobilePhone())
                    .build();
        }
        return null;
    }

    @Override
    public void delete(String id) {
        storeRepository.deleteById(id);
    }


    @Override
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

    @Override
    public StoreResponse getById(String id){

        try {
            Store store = storeRepository.findById(id).orElse(null);

            return StoreResponse.builder()
                    .id(store.getId())
                    .address(store.getAddress())
                    .storeName(store.getName())
                    .phone(store.getMobilePhone())
                    .noSiup(store.getNoSiup())
                    .build();
        }catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


}

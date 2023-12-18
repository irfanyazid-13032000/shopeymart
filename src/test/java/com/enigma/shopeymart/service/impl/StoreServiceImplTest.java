package com.enigma.shopeymart.service.impl;

import com.enigma.shopeymart.dto.request.StoreRequest;
import com.enigma.shopeymart.dto.response.StoreResponse;
import com.enigma.shopeymart.entity.Store;
import com.enigma.shopeymart.repository.StoreRepository;
import com.enigma.shopeymart.service.StoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class StoreServiceImplTest {
    private final StoreRepository storeRepository = mock(StoreRepository.class);
    private final StoreService storeService = new StoreServiceImpl(storeRepository);


    @Test
    void itShouldReturnStoreResponseWhenCreatedNewStore() {
        StoreRequest dummyStoreRequest = new StoreRequest();
        dummyStoreRequest.setId("afwrgr");
        dummyStoreRequest.setName("yajed");

        StoreResponse dummyStoreResponse = storeService.create(dummyStoreRequest);

      verify(storeRepository).save(any(Store.class));
      assertEquals(dummyStoreRequest.getName(),dummyStoreResponse.getStoreName());
      assertNotEquals(dummyStoreRequest.getId(),dummyStoreResponse.getId());
      assertNull(dummyStoreResponse.getId(),"haduh kok null sih");

    }

    @Test
    void itShouldGetAllDataStoreWhenCallGetAll(){
        List<Store> dummyStore = new ArrayList<>();
        dummyStore.add(new Store("asdrwv","dwefwef","yajed","majalengka","03453987"));
        dummyStore.add(new Store("dweewfv","sdcwe","yajed","majalengka","esdadq3"));

        when(storeRepository.findAll()).thenReturn(dummyStore);

        List<StoreResponse> retrieved = storeService.getAll();

        assertEquals(dummyStore.size(),retrieved.size());

        for (int i = 0; i < dummyStore.size(); i++) {
            assertEquals(dummyStore.get(i).getAddress(),retrieved.get(i).getAddress());
        }

    }


    @Test
    void itSouldGetOneDataStoreWhenCallGetById(){
        String storeId = "1";
        Store store = new Store("1","sdcwe","yajed","majalengka","esdadq3");
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        StoreResponse storeResponse = storeService.getById(storeId);

        verify(storeRepository).findById(storeId);

        assertNotNull(storeResponse);
        assertEquals(storeId,storeResponse.getId());
        assertEquals("yajed",storeResponse.getStoreName());


    }
    @Test
    void itShouldDeleteByIdWhenCallDeleteById(){
        String id = "1";
        storeService.delete(id);
        verify(storeRepository,times(1)).deleteById(id);
    }
    @Test
    void itShouldUpdatedByIdWhenCallUpdate(){
        String id = "1";
        StoreRequest storeRequest = new StoreRequest("1","ewfwef","yajed","majalengka","092384023");

        Store existingStore = Store.builder()
                .id(storeRequest.getId())
                .name(storeRequest.getName())
                .noSiup(storeRequest.getNoSiup())
                .mobilePhone(storeRequest.getMobilePhone())
                .address(storeRequest.getAddress())
                .build();

//        untuk mengembalikan store nya ketika findById nya di eksekusi
        when(storeRepository.findById(storeRequest.getId())).thenReturn(Optional.of(existingStore));


        StoreResponse storeResponse = storeService.update(storeRequest);

//        verifikasi apakah method tersebut dipanggil 1 kali
        verify(storeRepository,times(1)).findById(id);
        verify(storeRepository,times(1)).save(existingStore);

        assertEquals(storeRequest.getName(),storeResponse.getStoreName());

    }





}
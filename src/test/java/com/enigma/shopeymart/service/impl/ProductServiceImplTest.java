package com.enigma.shopeymart.service.impl;

import com.enigma.shopeymart.dto.request.ProductRequest;
import com.enigma.shopeymart.dto.response.ProductResponse;
import com.enigma.shopeymart.dto.response.StoreResponse;
import com.enigma.shopeymart.entity.Product;
import com.enigma.shopeymart.entity.ProductPrice;
import com.enigma.shopeymart.repository.ProductRepository;
import com.enigma.shopeymart.service.ProductPriceService;
import com.enigma.shopeymart.service.ProductService;
import com.enigma.shopeymart.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductPriceService productPriceService = mock(ProductPriceService.class);
    private final StoreService storeService = mock(StoreService.class);
    private final ProductService productService = new ProductServiceImpl(productRepository,storeService,productPriceService);

    @BeforeEach
    void setUp(){
        reset(productRepository,storeService,productPriceService);
    }

    @Test
    void createProductAndProductPrice() {
        StoreResponse dummyStoreResponse = new StoreResponse();
        dummyStoreResponse.setId("store1");
        dummyStoreResponse.setStoreName("berkah selalu uang ku");
        dummyStoreResponse.setNoSiup("asf3r2f4");

        when(storeService.getById(anyString())).thenReturn(dummyStoreResponse);

        Product saveProduct = new Product();
        saveProduct.setName("oreo");
        saveProduct.setDescription("manis enak");

        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(saveProduct);

//        dummy request
        ProductRequest dummyProductRequest = mock(ProductRequest.class);
        when(dummyProductRequest.getStoreId()).thenReturn(StoreResponse.builder()
                        .id("store1")
                .build());
        when(dummyProductRequest.getProductName()).thenReturn("oreo");
        when(dummyProductRequest.getDescription()).thenReturn("manis enak");
        when(dummyProductRequest.getPrice()).thenReturn(10000L);
        when(dummyProductRequest.getStock()).thenReturn(10);

//        call method create
        ProductResponse productResponse = productService.createProductAndProductPrice(dummyProductRequest);

//        validate response
        assertNotNull(productResponse);
        assertEquals(saveProduct.getName(),productResponse.getProductName());

//        validate that the product price was set correct
//        assertEquals(dummyProductRequest.getPrice(),productResponse.getPrice());
        assertEquals(dummyProductRequest.getStock(),productResponse.getStock());

//        validate interaction with store
        assertEquals(dummyStoreResponse.getId(),productResponse.getStore().getId());

//        verify interaction with mock object
        verify(storeService).getById(anyString());
        verify(productRepository).save(any(Product.class));
        verify(productPriceService).create(any(ProductPrice.class));

    }
}
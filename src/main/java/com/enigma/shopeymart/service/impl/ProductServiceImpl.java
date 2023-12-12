package com.enigma.shopeymart.service.impl;

import com.enigma.shopeymart.dto.request.ProductRequest;
import com.enigma.shopeymart.dto.response.ProductResponse;
import com.enigma.shopeymart.dto.response.StoreResponse;
import com.enigma.shopeymart.entity.Product;
import com.enigma.shopeymart.entity.ProductPrice;
import com.enigma.shopeymart.entity.Store;
import com.enigma.shopeymart.repository.ProductRepository;
import com.enigma.shopeymart.service.ProductPriceService;
import com.enigma.shopeymart.service.ProductService;
import com.enigma.shopeymart.service.StoreService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final StoreService storeService;

    private final ProductPriceService productPriceService;

    @Override
    public ProductResponse create(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getProductName())
                .description(productRequest.getDescription())
                .build();
        productRepository.save(product);
        return ProductResponse.builder()
                .ProductId(product.getId())
                .build();
    }

    @Override
    public ProductResponse getById(String id) {
        try {
            Product product = productRepository.findById(id).orElse(null);
            return ProductResponse.builder()
                    .ProductId(product.getId())
                    .ProductName(product.getName())
                    .build();
        }catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<ProductResponse> getAll() {
        List<ProductResponse> res = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        products.stream().forEach(product -> {
            res.add(
            ProductResponse.builder()
                    .description(product.getDescription())
                    .productPrices(product.getProductPrices())
                    .ProductId(product.getId())
                    .ProductName(product.getName())
                .build());
        });
        return res;
    }

    @Override
    public ProductResponse update(ProductRequest productRequest) {
        ProductResponse productResponse1 = getById(productRequest.getProductId());
        if (productResponse1 != null){
            Product product = Product.builder()
                    .id(productRequest.getProductId())
                    .name(productRequest.getProductName())
                    .description(productRequest.getDescription())
                    .build();
            productRepository.save(product);
            return ProductResponse.builder()
                    .ProductId(product.getId())
                    .ProductName(product.getName())
                    .build();
        }
        return null;
    }

    @Override
    public void delete(String id) {
        ProductResponse productResponse1 = getById(id);
        if (productResponse1 != null){
            productRepository.deleteById(id);
            System.out.println("berhasil delete");
        }else {
            System.out.println("gagal delete");
        }

    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse createProductAndProductPrice(ProductRequest productRequest) {
        StoreResponse storeResponse = storeService.getById(productRequest.getStoreId().getId());

        Product product = Product.builder()
                .name(productRequest.getProductName())
                .description(productRequest.getDescription())
                .build();
        productRepository.save(product);

        Store store = Store.builder()
                .id(storeResponse.getId())
                .name(storeResponse.getStoreName())
                .mobilePhone(storeResponse.getPhone())
                .noSiup(storeResponse.getNoSiup())
                .build();

        ProductPrice productPrice = ProductPrice.builder()
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .isActive(true)
                .product(product)
                .store(store)
                .build();

        productPriceService.create(productPrice);

        return ProductResponse.builder()
                .ProductId(product.getId())
                .ProductName(product.getName())
                .description(product.getDescription())
//                .price(productPrice.getPrice())
//                .stock(productPrice.getStock())
//                .store(
//                        storeResponse.toBuilder()
//                                .id(storeResponse.getId())
//                                .storeName(storeResponse.getStoreName())
//                                .phone(storeResponse.getPhone())
//                                .address(storeResponse.getAddress())
//                                .noSiup(storeResponse.getNoSiup())
//                                .build()
//                )
                .build();
    }
}

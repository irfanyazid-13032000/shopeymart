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
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
                .price(productPrice.getPrice())
                .stock(productPrice.getStock())
                .store(
                        storeResponse.toBuilder()
                                .id(storeResponse.getId())
                                .storeName(storeResponse.getStoreName())
                                .phone(storeResponse.getPhone())
                                .address(storeResponse.getAddress())
                                .noSiup(storeResponse.getNoSiup())
                                .build()
                )
                .build();
    }

    @Override
    public Page<ProductResponse> getAllByNameOrPrice(String name, Long maxPrice, Integer page, Integer size) {
//        specification untuk menentukan criteria pencarian, disini criteria pencarian ditandai dengan root, root yang dimaksud adalah entity product
        Specification<Product> specification = (root, query, criteriaBuilder)->{
//            join digunakan untuk merelasikan antara  product dan product price
            Join<Product,ProductPrice> productPrices = root.join("productPrices");
//            predicate digunakan untuk menggunakan LIKE dimana nanti kita akan menggunakan kondisi pencarian parameter
//            disini kita akan mencari nama product atau harga yg sama atau harga dibawahnya, makannya menggunakan lessThanOrEquals
            List<Predicate> predicates = new ArrayList<>();
            if (name != null){
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%"+name.toLowerCase() + "%"));
            }

            if (maxPrice != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(productPrices.get("price"),maxPrice));
            }
//            kode return mengembalikan query dimana pada dasarnya kita membangun klausa where yg sudah ditentukan dari predicate atau kriteria
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(page,size);
        Page<Product> products = productRepository.findAll(specification,pageable);
//        ini digunakan untuk menyimpan response product yg baru
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products.getContent()){
//            for disini digunakan untuk mengiterasi daftar produk yg disimpan dalam object
            Optional<ProductPrice> productPrice = product.getProductPrices()
                    .stream()
                    .filter(ProductPrice::getIsActive).findFirst();
            if (productPrice.isEmpty()) continue;
            Store store = productPrice.get().getStore();
            productResponses.add(toProductResponse(store,product,productPrice.get()));

        }

        return new PageImpl<>(productResponses,pageable,products.getTotalElements());
    }


    private static ProductResponse toProductResponse(Store store, Product product, ProductPrice productPrice){
        return ProductResponse.builder()
                .ProductId(product.getId())
                .ProductName(product.getName())
                .price(productPrice.getPrice())
                .description(product.getDescription())
                .stock(productPrice.getStock())
                .store(StoreResponse.builder()
                        .id(store.getId())
                        .phone(store.getMobilePhone())
                        .noSiup(store.getNoSiup())
                        .address(store.getAddress())
                        .build())

                .build();
    }


}

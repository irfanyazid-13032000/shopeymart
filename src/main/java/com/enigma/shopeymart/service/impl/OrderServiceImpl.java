package com.enigma.shopeymart.service.impl;

import com.enigma.shopeymart.dto.request.OrderRequest;
import com.enigma.shopeymart.dto.response.*;
import com.enigma.shopeymart.entity.Customer;
import com.enigma.shopeymart.entity.Order;
import com.enigma.shopeymart.entity.OrderDetail;
import com.enigma.shopeymart.entity.ProductPrice;
import com.enigma.shopeymart.repository.OrderRepository;
import com.enigma.shopeymart.service.CustomerService;
import com.enigma.shopeymart.service.OrderService;
import com.enigma.shopeymart.service.ProductPriceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductPriceService productPriceService;


    @Transactional(rollbackOn = Exception.class)
    @Override
    public OrderResponse create(OrderRequest orderRequest) {
//        todo 1 : validate customer
        CustomerResponse customerResponse = customerService.getById(orderRequest.getCustomerId());
//        todo 2 : convert orderDetailRequest to OrderDetail
        List<OrderDetail> orderDetails = orderRequest.getOrderDetailRequests().stream().map(orderDetailRequest -> {
//            todo 3 : validate product price
            ProductPrice productPrice = productPriceService.getById(orderDetailRequest.getProductPriceId());
            return OrderDetail.builder()
                    .productPrice(productPrice)
                    .quantity(orderDetailRequest.getQuantity())
                    .build();

        }).toList();
//        todo 4 : create new order
        Order order = Order.builder()
                .customer(Customer.builder()
                        .id(customerResponse.getId())
                        .build())
                .transDate(LocalDateTime.now())
                .orderDetails(orderDetails)
                .build();

        orderRepository.saveAndFlush(order);

        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream().map(orderDetail -> {
//            todo 5 : set order from orderDetail after creating new order
            orderDetail.setOrder(order);
            System.out.println(order);
//            todo 6 : change the stock from purchase quantity
            ProductPrice currentProductPrice = orderDetail.getProductPrice();
            currentProductPrice.setStock(currentProductPrice.getStock() - orderDetail.getQuantity());
            return OrderDetailResponse.builder()
                    .orderDetailId(orderDetail.getId())
                    .quantity(orderDetail.getQuantity())
//                    todo 7 : convert product to productResponse(product price)
                    .productResponse(
                            ProductResponse.builder()
                                    .ProductId(currentProductPrice.getProduct().getId())
                                    .ProductName(currentProductPrice.getProduct().getName())
                                    .description(currentProductPrice.getProduct().getDescription())
                                    .stock(currentProductPrice.getStock())
                                    .price(currentProductPrice.getPrice())
//                                    todo 8 : convert store to storeReponse
                                    .store(StoreResponse.builder()
                                            .id(currentProductPrice.getStore().getId())
                                            .storeName(currentProductPrice.getStore().getName())
                                            .noSiup(currentProductPrice.getStore().getNoSiup())
                                            .build())
                                    .build()
                    )
                    .build();
        }).toList();


//        todo 9 return order response


        return OrderResponse.builder()
                .customerResponse(customerResponse)
                .orderDetailResponses(orderDetailResponses)
                .orderId(order.getId())
                .transDate(order.getTransDate())
                .build();
    }

    @Override
    public OrderResponse getById(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public List<OrderResponse> getAllOrder(OrderRequest orderRequest) {
        return null;
    }
}

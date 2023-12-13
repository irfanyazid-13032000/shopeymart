package com.enigma.shopeymart.service.impl;

import com.enigma.shopeymart.dto.request.CustomerRequest;
import com.enigma.shopeymart.dto.response.CustomerResponse;
import com.enigma.shopeymart.entity.Customer;
import com.enigma.shopeymart.repository.CustomerRepository;
import com.enigma.shopeymart.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse getById(String id) {
        try {
            Customer customer = customerRepository.findById(id).orElse(null);
            return CustomerResponse.builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .address(customer.getAddress())
                    .phone(customer.getMobilePhone())
                    .build();

        }catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
        return null;

    }

    @Override
    public CustomerResponse createNewCustomer(Customer request) {
        Customer customer = customerRepository.saveAndFlush(request);
        return CustomerResponse.builder()
                .id(customer.getId())
                .address(customer.getAddress())
                .phone(customer.getMobilePhone())
                .name(customer.getName())
                .build();
    }

    @Override
    public List<CustomerResponse> getAll() {
        List<CustomerResponse> res = new ArrayList<>();
        List<Customer> customers = customerRepository.findAll();
        for (var customer : customers){
            res.add(
                    CustomerResponse.builder()
                            .id(customer.getId())
                            .name(customer.getName())
                            .phone(customer.getMobilePhone())
                            .address(customer.getAddress())
                            .build()
            );
        }
        return res;
    }

    @Override
    public CustomerResponse update(CustomerRequest customerRequest) {
        Customer customer = Customer.builder()
                .name(customerRequest.getName())
                .mobilePhone(customerRequest.getMobilePhone())
                .id(customerRequest.getId())
                .address(customerRequest.getAddress())
                .build();
        customerRepository.save(customer);
      return CustomerResponse.builder().build();
    }

    @Override
    public void delete(String id) {
        customerRepository.deleteById(id);

    }

    @Override
    public CustomerResponse create(CustomerRequest customerRequest) {
        Customer customer = Customer.builder()
                .name(customerRequest.getName())
                .address(customerRequest.getAddress())
                .mobilePhone(customerRequest.getMobilePhone())
                .build();
        customerRepository.save(customer);
        return CustomerResponse.builder()
                .address(customer.getAddress())
                .name(customer.getName())
                .build();
    }
}

package com.enigma.shopeymart.controller;

import com.enigma.shopeymart.dto.request.CustomerRequest;
import com.enigma.shopeymart.dto.response.CustomerResponse;
import com.enigma.shopeymart.entity.Customer;
import com.enigma.shopeymart.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping("/customer")
    public CustomerResponse save(@RequestBody CustomerRequest customerRequest){
        return customerService.create(customerRequest);
    }
    @GetMapping("/customer")
    public List<CustomerResponse> getAll(){
        return customerService.getAll();
    }
    @GetMapping("/customer/{id}")
    public CustomerResponse getById(@PathVariable String id){
        return customerService.getById(id);
    }
    @PutMapping("/customer")
    public CustomerResponse update(@RequestBody CustomerRequest customerRequest){
        return customerService.update(customerRequest);
    }
    @DeleteMapping("/customer/{id}")
    public void delete(@PathVariable String id){
        customerService.delete(id);
    }
    @PostMapping("customer/v2")
    public CustomerResponse createCustomers(@RequestBody CustomerRequest customerRequest){
        return customerService.create(customerRequest);
    }


}

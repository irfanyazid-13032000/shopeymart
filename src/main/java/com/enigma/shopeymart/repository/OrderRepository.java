package com.enigma.shopeymart.repository;

import com.enigma.shopeymart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<Order,String>, JpaSpecificationExecutor<Order> {
}

package com.enigma.shopeymart.repository;

import com.enigma.shopeymart.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,String> {
}

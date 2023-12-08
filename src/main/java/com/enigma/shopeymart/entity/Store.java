package com.enigma.shopeymart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "m_store")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "no_siup",unique = true,nullable = false,length = 30)
    private String noSiup;
    @Column(name = "name",nullable = false,length = 30)
    private String name;
    @Column(name = "address",nullable = false,length = 30)
    private String address;
    @Column(name = "mobile_phone",unique = true,nullable = false,length = 30)
    private String mobilePhone;



}

package com.enigma.shopeymart.service;

import com.enigma.shopeymart.dto.request.AdminRequest;
import com.enigma.shopeymart.dto.request.AuthRequest;
import com.enigma.shopeymart.dto.response.AdminResponse;
import com.enigma.shopeymart.entity.Admin;

public interface AdminService {
    AdminResponse create(Admin admin);

}

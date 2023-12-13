package com.enigma.shopeymart.service.impl;

import com.enigma.shopeymart.entity.AppUser;
import com.enigma.shopeymart.entity.UserCredential;
import com.enigma.shopeymart.repository.UserCredentialRepository;
import com.enigma.shopeymart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public AppUser loadUserById(String id) { //method ini untuk menverifikasi JWT nya
        UserCredential userCredential = userCredentialRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("invalid credential"));
        return AppUser.builder()
                .id(userCredential.getId())
                .role(userCredential.getRole().getName())
                .password(userCredential.getPassword())
                .username(userCredential.getUsername())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
//        method ini untuk cek by username sebagai authentication untuk login
        UserCredential userCredential = userCredentialRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("invalid credentials"));
        return AppUser.builder()
                .id(userCredential.getId())
                .role(userCredential.getRole().getName())
                .password(userCredential.getPassword())
                .username(userCredential.getUsername())
                .build();
    }




}

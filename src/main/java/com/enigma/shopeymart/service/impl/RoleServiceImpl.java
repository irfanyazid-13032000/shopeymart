package com.enigma.shopeymart.service.impl;

import com.enigma.shopeymart.entity.Role;
import com.enigma.shopeymart.repository.RoleRepository;
import com.enigma.shopeymart.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(Role role) {
        Optional<Role> optRole = roleRepository.findByName(role.getName());
//        jika ada di db, maka di get saja
        if (!optRole.isEmpty()){
            return optRole.get();
        }
        return roleRepository.save(role);
    }
}

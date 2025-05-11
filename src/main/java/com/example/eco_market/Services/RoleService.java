package com.example.eco_market.Services;

import com.example.eco_market.Models.Role;
import com.example.eco_market.Repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER");
    }

    public Role getAdminRole() {
        return roleRepository.findByName("ROLE_ADMIN");
    }
}
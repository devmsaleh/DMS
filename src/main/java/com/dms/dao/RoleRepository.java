package com.dms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dms.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByName(String name);

}

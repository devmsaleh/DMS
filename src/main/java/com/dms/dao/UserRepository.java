package com.dms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.dms.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserNameIgnoreCase(String username);

	User findByEmailIgnoreCase(String email);

	int countByUserNameIgnoreCase(String username);

	int countByEmailIgnoreCase(String email);

	int countByMobileNumber(String mobile);

	User findByUuid(@Param("uuid") String uuid);

	int countByUuid(@Param("uuid") String uuid);

}

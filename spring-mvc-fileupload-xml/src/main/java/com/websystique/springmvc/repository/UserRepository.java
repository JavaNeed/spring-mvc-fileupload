package com.websystique.springmvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.websystique.springmvc.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	User findById(Integer id);

	User findBySsoId(String ssoId);
}

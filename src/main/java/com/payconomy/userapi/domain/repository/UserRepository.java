package com.payconomy.userapi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payconomy.userapi.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
}

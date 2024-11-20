package com.example.demo.repo;

import com.example.demo.model.loginm;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface loginrepo extends JpaRepository<loginm,Long> {
	//Boolean existsByUsername(String username);
	public loginm findByEmail(String email);
	public loginm findById(long id);
	public Page<loginm> findAllByStatus(String status,Pageable pg);//current page users per page
    Boolean existsByEmail(String email);
}

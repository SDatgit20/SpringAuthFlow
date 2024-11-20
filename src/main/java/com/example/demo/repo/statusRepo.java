package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.status;

public interface statusRepo extends JpaRepository<status,Long>{
	public status findById(long id);
	public boolean existsById(long id);
}

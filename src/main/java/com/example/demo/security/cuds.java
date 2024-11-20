package com.example.demo.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.model.loginm;
import com.example.demo.repo.loginrepo;

@Service
public class cuds implements UserDetailsService{
	@Autowired
	private loginrepo repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(repo.existsByEmail(username)) {
		     loginm l=repo.findByEmail(username);
			 BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		        String encodedPassword = passwordEncoder.encode(l.getPass());
			return new org.springframework.security.core.userdetails.User(l.getEmail(),encodedPassword,new ArrayList<>());
		}
		else {
			throw new UsernameNotFoundException("User not found");
		}
	}
}

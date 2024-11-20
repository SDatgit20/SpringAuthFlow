package com.example.demo.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class jwtAuthFilter extends OncePerRequestFilter {
	
	@Autowired
	private com.example.demo.security.cuds cuds;
	
	@Autowired
	private jwtUtil jwtu;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//get jwt
		//bearer and validate
		String reqtokenHeader=request.getHeader("Authorization");
		String username=null;
		String jwtToken=null;
		if(reqtokenHeader!=null&& reqtokenHeader.startsWith("Bearer ")) {
			jwtToken=reqtokenHeader.substring(7);
			try {
				username=this.jwtu.extractUsername(jwtToken);
			}
			catch(Exception e) {
				
			}
			UserDetails usd=this.cuds.loadUserByUsername(username);
			//security
			if(username!=null&&SecurityContextHolder.getContext().getAuthentication()==null) {
				UsernamePasswordAuthenticationToken upat=new UsernamePasswordAuthenticationToken(usd,null,usd.getAuthorities());
				upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(upat);
			}
			else {
				System.out.println("Not Authenticated");
			}
			
		}
		filterChain.doFilter(request, response);
		
	}
	
}

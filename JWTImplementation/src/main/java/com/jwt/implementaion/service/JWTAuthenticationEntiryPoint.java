package com.jwt.implementaion.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JWTAuthenticationEntiryPoint extends OncePerRequestFilter{
	
	

	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private HandlerExceptionResolver handlerExceptionResolver;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authHeader = request.getHeader("Autherization");
		
		//check authHearder null or not 
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return ;
		}
		
		try {
			final String jwtToken = authHeader.substring(7);
			
			final String userEmail = jwtService.extractUsername(jwtToken);
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			
			if(userEmail != null && authentication == null) {
				
				UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
				
				if(jwtService.isTokenValid(jwtToken, userDetails)) {
					
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
				
			}
			
			filterChain.doFilter(request, response);
			
			
		} catch (Exception e) {
			handlerExceptionResolver.resolveException(request, response, null, e);
		}
		
	}
	
	
	

}

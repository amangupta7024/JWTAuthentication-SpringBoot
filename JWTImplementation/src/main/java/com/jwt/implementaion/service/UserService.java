package com.jwt.implementaion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.implementaion.dto.LoginDto;
import com.jwt.implementaion.entity.User;
import com.jwt.implementaion.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public User signup(User userData) {
		userData.setPassword(passwordEncoder.encode(userData.getPassword()));
		
		return userRepository.save(userData);
	}
	
	public User loginUser(LoginDto loginDto) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
		
		return userRepository.findByEmail(loginDto.getEmail())
				.orElseThrow();
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAll();		
	}
}

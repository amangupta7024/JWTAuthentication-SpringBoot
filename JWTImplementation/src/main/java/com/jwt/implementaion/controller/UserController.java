package com.jwt.implementaion.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jwt.implementaion.dto.LoginDto;
import com.jwt.implementaion.entity.User;
import com.jwt.implementaion.response.LoginResponse;
import com.jwt.implementaion.service.JWTService;
import com.jwt.implementaion.service.UserService;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JWTService jwtService;
	
	
	@PostMapping("/auth/signup")
	public ResponseEntity<User> postMethodName(@RequestBody User user) {
		
		User user2 = userService.signup(user);
		
		return ResponseEntity.ok(user2);
		
	}
	
	@PostMapping("/auth/login")
	public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginDto loginDto) {
		
		User user = userService.loginUser(loginDto);
		
		String jwtToken = jwtService.generateToken(new HashMap<>(), user);
		
		LoginResponse loginResponse = new LoginResponse();
		
		loginResponse.setToken(jwtToken);
		loginResponse.setTokenExpireTime(jwtService.getExpirationTime());
		
		return ResponseEntity.ok(loginResponse);
		
	}
	
	
	@GetMapping("/getUsers")
	public ResponseEntity<List<User>> getAllUsers() {
		
		List<User> users = userService.getAllUsers();
		
		return ResponseEntity.ok(users);
	}
}

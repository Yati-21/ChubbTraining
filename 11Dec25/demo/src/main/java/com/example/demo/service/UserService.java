package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.model.UserRequest;
import com.example.demo.model.UserResponse;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public UserResponse create(UserRequest req) {
		User user = new User(req.getName(), req.getAge());
		User saved = userRepository.save(user);
		return new UserResponse(saved.getId(), saved.getName(), saved.getAge());
	}

	public UserResponse get(int id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return new UserResponse(user.getId(), user.getName(), user.getAge());
	}
}

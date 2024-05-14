package com.example.demo.resources;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.User;
import com.example.demo.dto.UserDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;

@RestController
public class UserResource {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/users")
	public ResponseEntity<List<UserDTO>> findAll() {

		List<User> list = userService.findAll();
		List<UserDTO> listDto = list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());

		return ResponseEntity.ok().body(listDto);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<Object> findById(@PathVariable String id) {

		Optional<User> obj = userRepository.findById(id);

		 if(obj.isEmpty()){
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
	        }
		return ResponseEntity.status(HttpStatus.OK).body(obj.get());

	}
}

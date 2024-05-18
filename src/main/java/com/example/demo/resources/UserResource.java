package com.example.demo.resources;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.User;
import com.example.demo.dto.UserDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;

import jakarta.validation.Valid;

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

		if (obj.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(obj.get());

	}

	@PostMapping("/users")
	public ResponseEntity<Object> saveUser(@RequestBody @Valid UserDTO userDTO) {

		var user = new User();
		BeanUtils.copyProperties(userDTO, user);

		if (!userDTO.getName().isBlank() && !userDTO.getEmail().isBlank()) {

			return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
		}
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid data.");

	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable String id) {
		Optional<User> obj = userRepository.findById(id);

		if (obj.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
		userRepository.delete(obj.get());
		return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");

	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable String id,
												@RequestBody @Valid UserDTO userDTO){
			Optional<User> obj=userRepository.findById(id);
			if (obj.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
			}
			var user = obj.get();
			BeanUtils.copyProperties(userDTO, user, "id");
			 return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(user));
		
		
		
	}

}

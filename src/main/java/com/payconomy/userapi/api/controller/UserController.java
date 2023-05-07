package com.payconomy.userapi.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.payconomy.userapi.api.DTO.UserDTO;
import com.payconomy.userapi.api.DTO.input.UserInput;
import com.payconomy.userapi.api.assembler.UserAssembler;
import com.payconomy.userapi.api.assembler.UserDTOAssembler;
import com.payconomy.userapi.domain.exception.EntityNotFoundException;
import com.payconomy.userapi.domain.model.User;
import com.payconomy.userapi.domain.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDTOAssembler userDTOAssembler;
	
	@Autowired
	private UserAssembler userAssembler;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDTO add(@RequestBody @Valid UserInput userInput){
		User user = userAssembler.toDomainObject(userInput);
		return userDTOAssembler.toModelLink(userService.save(user));
	}
	
	@GetMapping(value = "/{userId}")
	public UserDTO find(@PathVariable Long userId) {
		User user = userService.findOrFail(userId);
		return userDTOAssembler.toModelLink(user);
	}
	
	@GetMapping
	public ResponseEntity<List<UserDTO>> list() {
		List<UserDTO> userList = userDTOAssembler.toCollectionDTO(userService.list());
		
		if (userList.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok().body(userList);
		}
	}
	
	@PutMapping("/{userId}")
	public UserDTO update(@PathVariable Long userId, @RequestBody @Valid UserInput userInput) {
		User user = userService.findOrFail(userId);
		userAssembler.copyToDomainObject(userInput, user);
		return userDTOAssembler.toModel(userService.save(user));
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<?> delete(@PathVariable Long userId){
		try {
			userService.delete(userId);

			return ResponseEntity.noContent().build();

		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}

package com.payconomy.userapi.utils;

import com.payconomy.userapi.api.DTO.UserDTO;
import com.payconomy.userapi.api.DTO.input.UserInput;
import com.payconomy.userapi.domain.model.User;

public class UserCreator {
	public static User createUser() {
		return User.builder()
				.name("John Wick")
				.address("Berliner Str.")
				.age(33L)
				.email("johnwick@gmail.com")
				.build();
	}
	
	public static User createValidUser() {
		return User.builder()
				.id(1L)
				.name("John Wick")
				.address("Berliner Str.")
				.age(33L)
				.email("johnwick@gmail.com")
				.build();
	}
	
	public static User createUserMissingEmail() {
		return User.builder()
				.name("John Wick")
				.address("Berliner Str.")
				.age(33L)
				.build();
	}
	
	
	public static User createUserMissingAge() {
		return User.builder()
				.name("John Wick")
				.address("Berliner Str.")
				.email("johnwick@gmail.com")
				.age(0L)
				.build();
	}
	
	public static UserInput createValidUserInput() {
		return UserInput.builder()
				.name("John Wick")
				.address("Berliner Str.")
				.age(33L)
				.email("johnwick@gmail.com")
				.build();
	}
	
	public static UserInput createValidUserInput2() {
		return UserInput.builder()
				.name("Obi Wan")
				.address("Berliner Str.")
				.age(25L)
				.email("obiwan@gmail.com")
				.build();
	}
	
	public static UserDTO createValidUserDTO() {
		return UserDTO.builder()
				.id(1L)
				.name("John Wick")
				.address("Berliner Str.")
				.age(33L)
				.email("johnwick@gmail.com")
				.build();
	}
	
	public static UserInput createEmptyUserInput() {
		return UserInput.builder()
				.build();
	}
	public static UserInput createEmptyFieldsUserInput() {
		return UserInput.builder()
				.name("John Wick")
				.address("Berliner Str.")
				.age(0L)
				.email("")
				.build();
	}
	
}

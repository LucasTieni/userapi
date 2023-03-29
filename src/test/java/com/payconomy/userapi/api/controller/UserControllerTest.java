package com.payconomy.userapi.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconomy.userapi.api.DTO.UserDTO;
import com.payconomy.userapi.api.DTO.input.UserInput;
import com.payconomy.userapi.api.assembler.UserAssembler;
import com.payconomy.userapi.api.assembler.UserDTOAssembler;
import com.payconomy.userapi.domain.exception.UserNotFoundException;
import com.payconomy.userapi.domain.model.User;
import com.payconomy.userapi.domain.service.UserService;
import com.payconomy.userapi.utils.UserCreator;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters=false)
@DisplayName("Tests for User Controller")
class UserControllerTest {
	
	@MockBean
	private UserAssembler userAssembler;
	
	@MockBean
	private UserDTOAssembler userDTOAssembler;
	
	@MockBean
	private UserService userService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	@DisplayName("save persist User when successful")
	void savePersistUserWhenSuccessful() throws Exception {
		User validUser = UserCreator.createValidUser();
		
		when(userAssembler.toDomainObject(any(UserInput.class))).thenReturn(UserCreator.createUser());
		when(userService.save(any(User.class))).thenReturn(validUser);
		when(userDTOAssembler.toModel(any(User.class))).thenReturn(UserCreator.createValidUserDTO());
		
		mockMvc.perform(post("/users")
				.content(objectMapper.writeValueAsString(UserCreator.createValidUserInput()))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$").value(validUser));
	}
	
	@Test
	@DisplayName("save should not persist User with invalid UserInput parameters")
	void saveShouldNotPersistUserWhenFail() throws Exception {
		mockMvc.perform(post("/users")
				.content(objectMapper.writeValueAsString(UserCreator.createEmptyFieldsUserInput()))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	@DisplayName("save should not persist User with empty UserInput")
	void saveShouldFailWhenEmptyInput() throws Exception {
		mockMvc.perform(post("/users")
				.content(objectMapper.writeValueAsString(UserCreator.createEmptyUserInput()))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	@DisplayName("save should not persist User with existing email")
	void saveShouldNotPersistUserWithExistingArticleAndNumber() throws Exception {
		when(userAssembler.toDomainObject(any(UserInput.class))).thenReturn(UserCreator.createUser());
		when(userService.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);
		when(userDTOAssembler.toModel(any(User.class))).thenReturn(UserCreator.createValidUserDTO());

		mockMvc.perform(post("/users")
				.content(objectMapper.writeValueAsString(UserCreator.createValidUserInput()))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
	}
	
	@Test
	@DisplayName("list should returns list of all users")
	void listReturnsListOfAllUsers() throws Exception{
		List<User> users = List.of(UserCreator.createValidUser());
		List<UserDTO> usersDTO = List.of(UserCreator.createValidUserDTO());
		
		when(userService.list()).thenReturn(users);
	    when(userDTOAssembler.toCollectionDTO(users)).thenReturn(usersDTO);
	    
	    mockMvc.perform(get("/users"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)));
	}
	
	@Test
	@DisplayName("list should returns no users")
	void listShouldReturnsNoUsers() throws Exception{
		
		when(userService.list()).thenReturn(Collections.emptyList());
	    when(userDTOAssembler.toCollectionDTO(any())).thenReturn(Collections.emptyList());
	    
	    mockMvc.perform(get("/users"))
			.andExpect(status().isNoContent());
	  }

	@Test
	@DisplayName("update should updates the user")
	void updateEditUserWhenSuccessful() throws Exception {
		User validUser = UserCreator.createValidUser();
		
		when(userService.findOrFail(anyLong())).thenReturn(validUser);
		when(userService.save(any(User.class))).thenReturn(validUser);
		when(userDTOAssembler.toModel(any(User.class))).thenReturn(UserCreator.createValidUserDTO());
		
		mockMvc.perform(put("/users/1")
				.content(objectMapper.writeValueAsString(UserCreator.createValidUserInput()))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(validUser));
	}
	
	@Test
	@DisplayName("update should throw exception when user not exists")
	void updatePersistUserWhenSuccessful() throws Exception {
		doThrow(UserNotFoundException.class).when(userService).findOrFail(anyLong());
		
		mockMvc.perform(put("/users/1")
				.content(objectMapper.writeValueAsString(UserCreator.createValidUserInput()))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
		
	@Test
	@DisplayName("delete removes User when successful")
	void deleteRemovesUserWhenSuccessful() throws Exception {
		doNothing().when(userService).delete(anyLong());
		mockMvc.perform(delete("/users/1"))
			.andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("delete should throws UserNotFoundException")
	void deleteThrowsUserNotFoundException() throws Exception {
		doThrow(UserNotFoundException.class).when(userService).delete(anyLong());
		
		mockMvc.perform(delete("/users/1"))
			.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("find should return UserDTO when successful")
	void findReturnUserDTOWhenSuccessful() throws Exception {
		when(userService.findOrFail(anyLong())).thenReturn(UserCreator.createValidUser());
		when(userDTOAssembler.toModel(any(User.class))).thenReturn(UserCreator.createValidUserDTO());

		mockMvc.perform(get("/users/1")
				.content(objectMapper.writeValueAsString(UserCreator.createValidUserInput()))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("find throws exception when user not found")
	void findThrowsUserNotFoundException() throws Exception {
		doThrow(UserNotFoundException.class).when(userService).findOrFail(anyLong());
		
		mockMvc.perform(get("/users/1"))
			.andExpect(status().isNotFound());
	}

}

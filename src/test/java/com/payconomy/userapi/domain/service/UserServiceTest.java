package com.payconomy.userapi.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import com.payconomy.userapi.domain.exception.UserNotFoundException;
import com.payconomy.userapi.domain.model.User;
import com.payconomy.userapi.domain.repository.UserRepository;
import com.payconomy.userapi.utils.UserCreator;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

		@Mock
		private UserRepository userRepository;
		
		@InjectMocks
		private UserService userService;
		
		@Test
		@DisplayName("save persist User when successful")
		void savePersistUserWhenSuccessful() {
			when(userRepository.save(UserCreator.createUser()))
				.thenReturn(UserCreator.createValidUser());
			
			User userSaved = userService.save(UserCreator.createUser());
			
			assertThat(userSaved).isNotNull();
			assertThat(userSaved.getId()).isNotNull();
			assertThat(userSaved.getEmail()).isEqualTo("johnwick@gmail.com");
	        verify(userRepository, times(1)).save(any(User.class));
			
		}
		
		@Test
		@DisplayName("save throws Exception With invalid User")
		void saveThrowsExceptionWithInvalidUser() throws Exception {
			when(userRepository.save(UserCreator.createUserMissingEmail())).thenThrow(RuntimeException.class);
			
			assertThatThrownBy(() -> userService.save(UserCreator.createUserMissingEmail()))
				.isInstanceOf(RuntimeException.class);
		}
		
		@Test
		@DisplayName("list should returns List of all users when successful")
		void listShouldReturnsListOfUserWhenSuccessful() throws Exception {
			User user = UserCreator.createValidUser();
			List<User> users = List.of(user);
			
			when(userRepository.findAll()).thenReturn(users);
			List<User> usesReturn = userService.list();
			
		    assertThat(usesReturn).isNotEmpty();
		    assertThat(usesReturn).hasSize(1);
		    assertThat(usesReturn.get(0)).isEqualTo(user);
	        verify(userRepository, times(1)).findAll();
		}
		
		@Test
		@DisplayName("list should returns no users when successful")
		void listShouldReturnsNoUsers() throws Exception {
			when(userRepository.findAll()).thenReturn(Collections.emptyList());
			List<User> usesReturn = userService.list();
			
		    assertThat(usesReturn).isEmpty();
	        verify(userRepository, times(1)).findAll();
		}

		@Test
		@DisplayName("delete removes User when successful")
		void deleteRemovesUserWhenSuccessful() {
			doNothing().when(userRepository).deleteById(anyLong());

			userService.delete(anyLong());
			
			verify(userRepository, times(1)).deleteById(anyLong());
		}
		
		@Test
		@DisplayName("delete should throws UserNotFoundException")
		void deleteThrowsUserNotFoundException() {
			doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(anyLong());
			
			assertThatThrownBy(() -> userService.delete(anyLong()))
				.isInstanceOf(UserNotFoundException.class);
		}
		
		@Test
		@DisplayName("findOrFail should return User when successful")
		void findOrFailReturnUserDTOWhenSuccessful() throws Exception {
			when(userRepository.findById(anyLong())).thenReturn(Optional.of(UserCreator.createValidUser()));
			User user = userService.findOrFail(anyLong());
			
			assertThat(user).isNotNull();
			assertThat(user.getId()).isNotNull();
	        verify(userRepository, times(1)).findById(anyLong());
		}
		
		@Test
		@DisplayName("findOrFail throws exception when user not found")
		void findOrFailThrowsUserNotFoundException() throws Exception {
			when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

			assertThatThrownBy(() -> userService.findOrFail(anyLong()))
				.isInstanceOf(UserNotFoundException.class);
		}
		
		@Test
		@DisplayName("list return list of Users")
		void listByArticleShouldReturnListOfUsers() throws Exception {
			
			when(userRepository.findAll()).thenReturn(List.of(UserCreator.createValidUser()));
			
			List<User> usersFound = userService.list();
			
			assertThat(usersFound).isNotNull().isNotEmpty();
	        verify(userRepository, times(1)).findAll();
		}
}

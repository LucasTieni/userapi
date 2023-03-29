package com.payconomy.userapi.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.payconomy.userapi.domain.model.User;
import com.payconomy.userapi.utils.UserCreator;

@DataJpaTest
//@AutoConuserTestDatabase(replace=Replace.NONE)
@DisplayName("Tests for User Repository")
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	@DisplayName("Save persist User when successful")
	void savePersistUserWhenSuccessful() {
		User userSaved = userRepository.save(UserCreator.createUser());
		
		assertThat(userSaved).isNotNull();
		assertThat(userSaved.getId()).isNotNull();
	}
	
	@Test
	@DisplayName("save throws Exception with user missing a propertie")
	void saveThrowsExceptionWithUserMissingPropertie() throws Exception {
		assertThatThrownBy(() -> userRepository.save(UserCreator.createUserMissingEmail()))
			.isInstanceOf(RuntimeException.class);
	}
	
	@Test
	@DisplayName("save throws Exception with user missing age propertie")
	void saveThrowsExceptionWithUserMissingAgePropertie() throws Exception {
		assertThatThrownBy(() -> userRepository.save(UserCreator.createUserMissingAge()))
			.isInstanceOf(RuntimeException.class);
	}
	
	@Test
	@DisplayName("Save should fail when try save two users with same email")
	void saveShouldThrowExceptionUsersWithSameEmail() {
		userRepository.save(UserCreator.createUser());
		
		assertThatThrownBy(() -> userRepository.save(UserCreator.createUser()))
			.isInstanceOf(RuntimeException.class);
	}
	
	@Test
	@DisplayName("Save updates User when successful")
	void saveUpdatesUserWhenSuccessful() {
		User userToBeSaved = UserCreator.createUser();
		
		User userSaved = userRepository.save(userToBeSaved);
		
		userSaved.setEmail("yoda@gmail.com");
		userSaved.setAge(1000L);
		
		User userUpdated = userRepository.save(userSaved);
		
		assertThat(userUpdated).isNotNull();
		assertThat(userUpdated.getId()).isEqualTo(userSaved.getId());
		assertThat(userUpdated.getEmail()).isEqualTo(userUpdated.getEmail());
		assertThat(userUpdated.getAge()).isEqualTo(userUpdated.getAge());
	}
	
	@Test
	@DisplayName("Delete removes User when successful")
	void deleteRemovesUserWhenSuccessful() {
		User userSaved = userRepository.save(UserCreator.createUser());
		userRepository.deleteById(userSaved.getId());
		Optional<User> userOptional = userRepository.findById(userSaved.getId());
		assertThat(userOptional).isEmpty();
	}
	
	@Test
	@DisplayName("Delete throws exception when user not found")
	void deleteThrowsExceptionWhenUserNotFound() {
		assertThatThrownBy(() -> userRepository.deleteById(anyLong()))
			.isInstanceOf(RuntimeException.class);
	}
	
	@Test
	@DisplayName("findById should return User when successful")
	void findByIdReturnUserWhenSuccessful() {
		User userSaved = userRepository.save(UserCreator.createUser());
		
		Optional<User> userFound = userRepository.findById(userSaved.getId());
		
		assertThat(userFound).isNotEmpty();
		assertThat(userFound.get().getId()).isNotNull().isEqualTo(userSaved.getId());
	}
	
	@Test
	@DisplayName("findById throws Exception when User not Found")
	void findByIdThrowsExceptionWhenUserNotFound() {
		Optional<User> userFound = userRepository.findById(1L);
		
		assertThat(userFound).isEmpty();
	}
}

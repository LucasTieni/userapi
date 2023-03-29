package com.payconomy.userapi.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payconomy.userapi.domain.exception.EntityInUseException;
import com.payconomy.userapi.domain.exception.UserNotFoundException;
import com.payconomy.userapi.domain.model.User;
import com.payconomy.userapi.domain.repository.UserRepository;


@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public User save(User user) {
		return userRepository.save(user);
	}
	
	public List<User> list(){
		return userRepository.findAll();
	}
	
	@Transactional
	public void delete(Long userId) {
		try {
			userRepository.deleteById(userId);
			userRepository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new UserNotFoundException(userId);

		} catch (DataIntegrityViolationException e) {
			throw new EntityInUseException(
					String.format("User with code %d cannot be removed, because it is in use.", userId));
		}
	}
	
	public User findOrFail(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));
	}
}




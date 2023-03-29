package com.payconomy.userapi.api.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.payconomy.userapi.api.DTO.UserDTO;
import com.payconomy.userapi.api.controller.UserController;
import com.payconomy.userapi.domain.model.User;

@Component
public class UserDTOAssembler  
	extends RepresentationModelAssemblerSupport<User, UserDTO>{
	
	@Autowired  
	private ModelMapper modelMapper;
	
	public UserDTOAssembler() {
		super(UserController.class, UserDTO.class);
	}
	
	public UserDTO toModel(User user) {
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper.map(user, UserDTO.class);
	}
	
	public List<UserDTO> toCollectionDTO(List<User> file){
		return file.stream()
				.map(imageArticle -> toModel(imageArticle))
				.toList();
	}
}

package com.payconomy.userapi.api.DTO;

import org.springframework.hateoas.RepresentationModel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO extends RepresentationModel<UserDTO>{
	private Long id;
	private String name;
	private String email;	
	private String address;	
	private Long age;
}



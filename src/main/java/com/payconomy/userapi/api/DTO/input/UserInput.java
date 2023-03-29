package com.payconomy.userapi.api.DTO.input;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInput{
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String email;	
	
	@NotBlank
	private String address;	
	
	@NotNull
	private Long age;
}



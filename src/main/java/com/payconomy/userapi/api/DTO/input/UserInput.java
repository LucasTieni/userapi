package com.payconomy.userapi.api.DTO.input;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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



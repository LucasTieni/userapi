package com.payconomy.userapi.api;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.payconomy.userapi.api.controller.UserController;

@Component
public class UserapiLinks {

	public Link linkTo(Long userId) {
		return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.find(userId)).withSelfRel();
	}
	
	public Link linkToUsers(String rel) {
		return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
				.list()).withRel(rel);
	}
	
	public Link linkToUsers() {
		return linkToUsers(IanaLinkRelations.SELF.value());
	}
}

package com.payconomy.userapi;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.payconomy.userapi.api.DTO.UserDTO;
import com.payconomy.userapi.api.DTO.input.UserInput;
import com.payconomy.userapi.utils.UserCreator;

//@Testcontainers
@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserIT {
	
	@Test
	public void contextLoads() {
		
	}
	
//	@Autowired
//	private WebTestClient webTestClient;
//	
//	@Autowired
//	private EntityManagerFactory entityManagerFactory;
//	
//	@AfterEach
//	public void cleanup() {
//	    EntityManager em = entityManagerFactory.createEntityManager();
//	    em.getTransaction().begin();
//	    em.createNativeQuery("truncate table user").executeUpdate();
//	    em.getTransaction().commit();
//	}
//	
//	@Test
//	void addUser_ReturnsCreated() {
//		UserInput userToBeSaved = UserCreator.createValidUserInput();
//		
//		EntityExchangeResult<UserDTO> userSaved = webTestClient.post().uri("/users").bodyValue(userToBeSaved)
//			.exchange().expectStatus().isCreated().expectBody(UserDTO.class)
//			.returnResult();
//			 
//		assertThat(userSaved.getStatus()).isEqualTo(HttpStatus.CREATED);
//		assertThat(userSaved.getResponseBody()).isNotNull();
//		assertThat(userSaved.getResponseBody().getName()).isEqualTo(userToBeSaved.getName());
//		assertThat(userSaved.getResponseBody().getEmail()).isEqualTo(userToBeSaved.getEmail());
//	}

	
}




//	@Test
//	void buscarUser_ReturnsUserDTO() {
//		UserInput userToBeSaved = UserCreator.createValidUserInput();
//		
//		EntityExchangeResult<UserDTO> userSaved = webTestClient.post().uri("/user").bodyValue(userToBeSaved)
//				.exchange().expectStatus().isCreated().expectBody(UserDTO.class)
//				.returnResult();
//		
//		EntityExchangeResult<UserDTO> userFound = webTestClient.get().uri("/user/" + userSaved.getResponseBody().getId())
//				.exchange().expectStatus().isOk().expectBody(UserDTO.class)
//				.returnResult();
//		
//		assertThat(userFound.getStatus()).isEqualTo(HttpStatus.OK);
//		assertThat(userFound.getResponseBody().getId()).isNotNull().isEqualTo(userSaved.getResponseBody().getId());
//		assertThat(userFound.getResponseBody().getName()).isEqualTo(userSaved.getResponseBody().getName());
//		assertThat(userFound.getResponseBody().getTitle()).isEqualTo(userSaved.getResponseBody().getTitle());
//	}
//	
//	@Test
//	void listByArticle_ReturnsListOfUserDTO() {
//		UserInput userToBeSaved = UserCreator.createValidUserInput();
//		UserInput userToBeSaved2 = UserCreator.createValidUserInput2();
//		
//		EntityExchangeResult<UserDTO> userSaved = webTestClient.post().uri("/user").bodyValue(userToBeSaved)
//				.exchange().expectStatus().isCreated().expectBody(UserDTO.class)
//				.returnResult();
//		
//		EntityExchangeResult<UserDTO> userSaved2 = webTestClient.post().uri("/user").bodyValue(userToBeSaved2)
//				.exchange().expectStatus().isCreated().expectBody(UserDTO.class)
//				.returnResult();
//		
//		EntityExchangeResult<List<UserDTO>> response = webTestClient.get().uri("/user/").header("articleId", "1")
//				.exchange().expectStatus().isOk().expectBody(new ParameterizedTypeReference<List<UserDTO>>() {})
//				.returnResult();
//		
//		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);
//		assertThat(response).isNotNull();
//		assertThat(response.getResponseBody().get(0).getId()).isNotNull().isEqualTo(userSaved.getResponseBody().getId());
//		assertThat(response.getResponseBody().get(1).getId()).isNotNull().isEqualTo(userSaved2.getResponseBody().getId());
//	}
//	
//	@Test
//	@DisplayName("removerById should remove User by Id ")
//	void removerByIdUser_ReturnsVoid() {
//		UserInput userToBeSaved = UserCreator.createValidUserInput();
//		
//		EntityExchangeResult<UserDTO> userSaved = webTestClient.post().uri("/user").bodyValue(userToBeSaved)
//				.exchange().expectStatus().isCreated().expectBody(UserDTO.class)
//				.returnResult();
//		
//		webTestClient.delete().uri("/user/" + userSaved.getResponseBody().getId()).exchange().expectStatus().isNoContent();
//		
//		webTestClient.get().uri("/user/").header("articleId", "1")
//				.exchange().expectStatus().isNoContent().expectBody(new ParameterizedTypeReference<List<UserDTO>>() {})
//				.returnResult();
//	}
//	
//	@Test
//	@DisplayName("removerById should returns Entity not Found ")
//	void removerByIdUser_ReturnsEntityNotFound() {
//		webTestClient.delete().uri("/user/" + 1L).exchange().expectStatus().isNotFound();
//	}
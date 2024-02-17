package com.devsuperior.dscommerce.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscommerce.factory.CriaUserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;
	
	private String emailExistente , emailInexistente;

	@BeforeEach
	void setUp() throws Exception {
		emailExistente = "maria@gmail.com";
		emailInexistente = "lucas@gmail.com";
		
		Mockito.when(repository.searchUserAndRolesByEmail(emailExistente)).thenReturn(CriaUserDetailsProjection.novaProjection());
		Mockito.when(repository.searchUserAndRolesByEmail(emailInexistente)).thenReturn(new ArrayList<>());

	}
	
	@DisplayName("loadUserByUsername Deve Retorna UserDetails Quando Email For Correto")
	@Test
	public void loadUserByUsernameDeveRetornaUserDetailsQuandoEmailForCorreto() {
		
		 UserDetails user = service.loadUserByUsername(emailExistente);
		 
		 assertNotNull(user);
		 assertEquals(user.getUsername(), "maria@gmail.com");
		 assertEquals(user.getAuthorities().size(), 1);
		 assertEquals(user.getPassword(), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
	}
	
	@DisplayName("loadUserByUsername Deve Retorna UsernameNotFoundException Quando Email For Invalido")
	@Test
	public void loadUserByUsernameDeveRetornaUsernameNotFoundExceptionQuandoEmailForInvalido() {
		
		assertThrows(UsernameNotFoundException.class, () -> {
			
			 UserDetails user = service.loadUserByUsername(emailInexistente);
		});
	}
}

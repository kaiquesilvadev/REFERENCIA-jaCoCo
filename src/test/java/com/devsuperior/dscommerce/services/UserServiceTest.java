package com.devsuperior.dscommerce.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscommerce.dto.UserDTO;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.factory.CriaUser;
import com.devsuperior.dscommerce.factory.CriaUserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository repository;

	@Mock
	private Jwt jwt;

	@Mock
	private Authentication authentication;

	@Mock
	private SecurityContext securityContext;

	private String emailExistente, emailInexistente;
	private User user;

	@BeforeEach
	void setUp() throws Exception {
		emailExistente = "maria@gmail.com";
		emailInexistente = "lucas@gmail.com";
		user = CriaUser.novoUser();

		Mockito.when(repository.searchUserAndRolesByEmail(emailExistente))
				.thenReturn(CriaUserDetailsProjection.novaProjection());
		Mockito.when(repository.searchUserAndRolesByEmail(emailInexistente)).thenReturn(new ArrayList<>());

		// Simula o Jwt
		jwt = Mockito.mock(Jwt.class);

		// Simula o objeto Authentication
		authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getPrincipal()).thenReturn(jwt);

		// Configura o SecurityContextHolder para retornar o objeto de autenticação
		// simulado
		securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

	}

	@DisplayName("loadUserByUsername Deve Retorna UserDetails Quando Email For Correto")
	@Test
	public void loadUserByUsernameDeveRetornaUserDetailsQuandoEmailForCorreto() {

		UserDetails Details = service.loadUserByUsername(emailExistente);

		assertNotNull(Details);
		assertEquals(Details.getUsername(), "maria@gmail.com");
		assertEquals(Details.getAuthorities().size(), 1);
		assertEquals(Details.getPassword(), "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
	}

	@DisplayName("loadUserByUsername Deve Retorna UsernameNotFoundException Quando Email For Invalido")
	@Test
	public void loadUserByUsernameDeveRetornaUsernameNotFoundExceptionQuandoEmailForInvalido() {

		assertThrows(UsernameNotFoundException.class, () -> {

			UserDetails Details = service.loadUserByUsername(emailInexistente);
		});
	}

	@DisplayName("authenticated Deve Retorna UserAutenticado Quando Email For Valido")
	@Test
	public void authenticatedDeveRetornaUserAutenticadoQuandoEmailForValido() {
		Mockito.when(jwt.getClaim("username")).thenReturn(emailExistente); // Supondo que emailExistente seja um email
																			// válido
		Mockito.when(repository.findByEmail(emailExistente)).thenReturn(Optional.of(CriaUser.novoUser()));

		User user = service.authenticated();
		assertNotNull(user);
	}

	@DisplayName("authenticated Deve Retorna Username Not FoundExceptionQuando Email For Invalido")
	@Test
	public void authenticatedDeveRetornaUsernameNotFoundExceptionQuandoEmailForInvalido() {
		Mockito.when(jwt.getClaim("username")).thenReturn(emailInexistente);
		Mockito.when(repository.findByEmail(emailInexistente)).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> {
			service.authenticated();
		});
	}

	@DisplayName("getMe Deve Buscar User Se estiver authenticated")
	@Test
	public void getMeDeveBuscarUserSeEstiveAuthenticated() {

		UserService userSky = Mockito.spy(service);
		Mockito.doReturn(user).when(userSky).authenticated();

		UserDTO userDTO = userSky.getMe();
		
		assertNotNull(userDTO);
	}
	
	@DisplayName("getMe Deve Retorna UsernameNotFoundException Se User não estiver Authenticated")
	@Test
	public void getMeDeveRetornaUsernameNotFoundExceptionSeUserNaoEstiverAuthenticated() {

		UserService userSky = Mockito.spy(service);
		Mockito.doThrow(UsernameNotFoundException.class).when(userSky).authenticated();

		assertThrows(UsernameNotFoundException.class,() -> {
			UserDTO userDTO = userSky.getMe();
		});
	}
}
package com.devsuperior.dscommerce.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscommerce.factory.CriaUser;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {

	@InjectMocks
	private AuthService service;

	@Mock
	private UserService userService;

	@DisplayName("validateSelfOrAdmin não Deve Retorna Nada Se User For Admin E id For Compativel")
	@Test
	public void validateSelfOrAdminNaoDeveRetornarNadaSeUserForAdminEIdForCompativel() {

		Mockito.when(userService.authenticated()).thenReturn(CriaUser.userAdmin());
		assertDoesNotThrow(() -> service.validateSelfOrAdmin(1L));
	}
	
	@DisplayName("validateSelfOrAdmin não Deve Retorna Nada Se User For Client Mais Id For Compativel")
	@Test
	public void validateSelfOrAdminNaoDeveRetornaNadaSeUserForClientMaisIdForCompativel() {

		Mockito.when(userService.authenticated()).thenReturn(CriaUser.userClient());
		assertDoesNotThrow(() -> service.validateSelfOrAdmin(1L));

	}

	@DisplayName("validateSelfOrAdmin Deve Retornar ForbiddenException Quando Id Nao For Compativel E User For Client")
	@Test
	public void validateSelfOrAdminDeveRetornarForbiddenExceptionQuandoIdNaoForCompativelEUserForClient() {

		Mockito.when(userService.authenticated()).thenReturn(CriaUser.userClient());
		assertThrows(ForbiddenException.class, () -> {
			service.validateSelfOrAdmin(2L);
		});

	}
}

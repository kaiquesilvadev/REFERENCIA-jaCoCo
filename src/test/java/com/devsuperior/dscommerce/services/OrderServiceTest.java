package com.devsuperior.dscommerce.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.factory.CriaOrder;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

	@InjectMocks
	private OrderService service;
	private Long idExistente , idInexistente;
	private Long  idOrdeClient ,idClient;

	@Mock
	private OrderRepository repository;
	
	@Mock
	private AuthService authService;

	@BeforeEach
	void setup() throws Exception {
		idClient = CriaOrder.novoOrder().getClient().getId();
		idInexistente = 100l;
		idExistente = 1l;
		

		Mockito.when(repository.findById(idOrdeClient)).thenReturn(Optional.of(CriaOrder.novoOrder()));
		Mockito.when(repository.findById(idExistente)).thenReturn(Optional.of(CriaOrder.novoOrder()));
		
		Mockito.when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		

	}

	@Test
	public void findByIdDeveRetornarOrderDTOQuandoIdForValido() {

		OrderDTO orderDTO = service.findById(idExistente);
		Mockito.doNothing().when(authService).validateSelfOrAdmin(idExistente);
		assertNotNull(orderDTO);
	}
	
	@Test
	public void findByIdDeveRetornarResourceNotFoundExceptionQuandoIdForInvalido() {

		Mockito.doNothing().when(authService).validateSelfOrAdmin(idInexistente);
		
		assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(idInexistente);
		});
	}
	
	@Test
	public void findByIdDeveRetornarForbiddenExceptionQuandoIdForValidoMaisNaoForPropioOuAdmin() {

        Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(idClient);		
		assertThrows(ForbiddenException.class, () -> {
			service.findById(idOrdeClient);
		});
	}
}

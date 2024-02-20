package com.devsuperior.dscommerce.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderStatus;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.factory.CriaItens;
import com.devsuperior.dscommerce.factory.CriaOrder;
import com.devsuperior.dscommerce.factory.CriaProduct;
import com.devsuperior.dscommerce.factory.CriaUser;
import com.devsuperior.dscommerce.repositories.OrderItemRepository;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

	@InjectMocks
	private OrderService service;

	private Long idExistente, idInexistente;
	private Long idOrdeClient, idClient;
	private Long productIdExistente, productIdInexistente;

	@Mock
	private OrderRepository repository;

	@Mock
	private AuthService authService;

	@Mock
	private UserService userService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private OrderItemRepository orderItemRepository;

	private User user;
	private Order order;
	private Product product, productInexistente;

	@BeforeEach
	void setup() throws Exception {
		idClient = CriaOrder.novoOrder().getClient().getId();
		idInexistente = 100l;
		idExistente = 1l;

		product = CriaProduct.productComId();
		productInexistente = CriaProduct.novoProdut();
		order = CriaOrder.novoOrder();
		user = CriaUser.novoUser();

		productIdExistente = product.getId();
		productIdInexistente = 1000L;

		// orde
		Mockito.when(repository.findById(idOrdeClient)).thenReturn(Optional.of(order));
		Mockito.when(repository.findById(idExistente)).thenReturn(Optional.of(order));
		Mockito.when(repository.findById(idInexistente)).thenReturn(Optional.empty());
		Mockito.when(repository.save(any())).thenReturn(order);

		// user
		Mockito.when(userService.authenticated()).thenReturn(user);

		// product
		Mockito.when(productRepository.getReferenceById(productIdExistente)).thenReturn(CriaProduct.novoProdut());
		Mockito.when(productRepository.getReferenceById(productIdInexistente)).thenThrow(EntityNotFoundException.class);

		// orderItem
		Mockito.when(orderItemRepository.saveAll(anyList())).thenReturn(List.of(CriaItens.novoItens()));
	}

	@Test
	public void findByIdDeveRetornaEntityNotFoundExceptionQuandoIdDeProductForInexistente() {

		productInexistente.setId(productIdInexistente);
		Product novoProduc = productInexistente;
		OrderDTO dto = new OrderDTO(CriaOrder.novoOrder(novoProduc));

		assertThrows(EntityNotFoundException.class, () -> {
			service.insert(dto);
		});

	}

	@Test
	public void findByIdDeveRetornaOrderDTOQuandoDadosForValido() {
		OrderDTO dto = new OrderDTO(order);

		OrderDTO orderDTO = service.insert(dto);

		assertNotNull(orderDTO);
		assertNotNull(orderDTO.getItems());
		assertNotNull(orderDTO.getMoment());
		assertEquals(orderDTO.getStatus(), OrderStatus.WAITING_PAYMENT);
		assertNotNull(orderDTO.getClient());
		assertEquals(orderDTO.getClient().getName(), user.getName());
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

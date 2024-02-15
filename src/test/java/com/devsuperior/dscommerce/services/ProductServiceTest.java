package com.devsuperior.dscommerce.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.factory.CriaProduct;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	private Long existente , inexistente;
	private Product product;
	
	@BeforeEach
	public void setUp() throws Exception{
		product = CriaProduct.productComId();
		ProductDTO dto = new ProductDTO(product);
		
		existente = 1L;
		inexistente = 1000L;
		
		Mockito.when(repository.findById(existente)).thenReturn(Optional.of(product));
	    Mockito.when(repository.findById(inexistente)).thenReturn(Optional.empty());
	}
	
	@Test
	@DisplayName("findById Deve Retornar Um Dto Quando Id For Existente")
	public void findByIdDeveRetornarUmDtoQuandoIdForExistente() {
		ProductDTO productDTO = service.findById(existente);
		
		assertEquals(productDTO.getId(), 1L);
		assertEquals(productDTO.getName() , product.getName());
	}
	
	@Test
	@DisplayName("findById Deve Retornar Uma Exception quando Id For Inexistente")
	public void findByIdDeveRetornarUmaExceptionquandoIdForInexistente() {
		
		assertThrows(ResourceNotFoundException.class, () -> {
			@SuppressWarnings("unused")
			ProductDTO productDTO = service.findById(inexistente);
		});
	}
}

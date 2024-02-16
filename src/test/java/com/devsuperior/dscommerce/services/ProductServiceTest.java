package com.devsuperior.dscommerce.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.factory.CriaProduct;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;
	private Long existente, inexistente , IdDependente;
	private Product product;
	private PageImpl<Product> page;

	@BeforeEach
	public void setUp() throws Exception {
		existente = 1L;
		inexistente = 1000L;
		product = CriaProduct.productComId();
		page = new PageImpl<>(List.of(product));
		
		Mockito.when(repository.getReferenceById(existente)).thenReturn(CriaProduct.productExistent());
		Mockito.when(repository.getReferenceById(inexistente)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.save(any())).thenReturn(product);
		
		Mockito.when(repository.findById(existente)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(inexistente)).thenReturn(Optional.empty());
		
		Mockito.when(repository.searchByName(any(), (Pageable) any())).thenReturn(page);
		
		Mockito.when(repository.existsById(existente)).thenReturn(true);
		Mockito.when(repository.existsById(IdDependente)).thenReturn(true);
		Mockito.when(repository.existsById(inexistente)).thenReturn(false);
		
		Mockito.doNothing().when(repository).deleteById(existente);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(IdDependente);

		
	}

	@Test
	@DisplayName("findById Deve Retornar Um Dto Quando Id For Existente")
	public void findByIdDeveRetornarUmDtoQuandoIdForExistente() {
		ProductDTO productDTO = service.findById(existente);

		assertEquals(productDTO.getId(), 1L);
		assertEquals(productDTO.getName(), product.getName());
	}

	@Test
	@DisplayName("findById Deve Retornar Uma Exception quando Id For Inexistente")
	public void findByIdDeveRetornarUmaExceptionQuandoIdForInexistente() {

		assertThrows(ResourceNotFoundException.class, () -> {
			@SuppressWarnings("unused")
			ProductDTO productDTO = service.findById(inexistente);
		});
	}

	@Test
	@DisplayName("findAll Deve Retornar Uma Pagina quando Passar Um Nome")
	public void findAllDeveRetornarUmaPaginaQuandoPassarUmNome() {

		PageRequest pageRequest = PageRequest.of(0, 12);
		Page<ProductMinDTO> pageProduct = service.findAll("The Lord of the Rings", pageRequest);
		
		assertNotNull(pageProduct);
		assertEquals(pageProduct.getSize(), 1L);
		assertEquals(pageProduct.iterator().next().getName(), "The Lord of the Rings");
	}
	
	@DisplayName("insert deve retorna a entidade depois de salva")
	@Test
	public void insertDeveRetornaAEntidadeDepoisDeSalva() {
		ProductDTO dto = new ProductDTO(CriaProduct.novoProdut());
		
		ProductDTO productDTO = service.insert(dto);
		
		assertNotNull(productDTO);
		assertEquals(productDTO.getName(), dto.getName());
	}
	
	@DisplayName("update Deve Retorna A Entidade Quando O Id For Existente")
	@Test
	public void updateDeveRetornaAEntidadeQuandoOIdForExistente() {
		ProductDTO dto = new ProductDTO(CriaProduct.novoProdut());
		
		ProductDTO productDTO = service.update(1l , dto);
		
		assertNotNull(productDTO);
		assertEquals(productDTO.getName(), dto.getName());
		assertEquals(productDTO.getId(), 1l);
	}
	
	@DisplayName("update Deve Retorna A Entidade Quando O Id For Existente")
	@Test
	public void updateDeveRetornaExceptionQuandoIdForInexistente() {
		ProductDTO dto = new ProductDTO(CriaProduct.novoProdut());
		
		assertThrows(ResourceNotFoundException.class, () -> {
			service.update(inexistente, dto);
		});
	}
	
	@DisplayName("delete Deve Retorna ResourceNotFoundException Quando Id For Inexistente")
	@Test
	public void deleteDeveRetornaExceptionQuandoIdForInexistente() {
		
		assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(inexistente);
		});
	}
	
	@DisplayName("delete Deve Retorna DatabaseException Quando Id For Dependente")
	@Test
	public void deleteDeveRetornaExceptionQuandoIdForDependente() {
		
		assertThrows(DatabaseException.class ,  () -> {
			service.delete(IdDependente);
		});
	}
	
	@Test
	@DisplayName("delete Deve Deletar Quando Id Existente")
	public void deleteDeveDeletarQuandoIdExistente() {
		 
		assertDoesNotThrow(() -> {
			
			service.delete(existente);
		});
	}

}

package com.devsuperior.dscommerce.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscommerce.dto.CategoryDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.repositories.CategoryRepository;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

	@InjectMocks
	private CategoryService Service;
	
	@Mock
	private CategoryRepository repository;
	private List<Category> list;
	private Category category;
	
	@BeforeEach
	public void upSet() throws Exception{
		category = new Category(2L , "Eletrônicos");
		list = new ArrayList<>();
		list.add(category);
		
		Mockito.when(repository.findAll()).thenReturn(list);
	}
	
	@Test
	@DisplayName("findAll Retorna Uma Lista Quando For Chamado")
	public void findAllRetornaUmaListaQuandoForChamado() {
		
		 List<CategoryDTO> lista = Service.findAll();
		 
		 assertEquals(lista.size(), list.size());
		 assertEquals(lista.get(0).getName(), list.get(0).getName());
		 assertEquals(lista.get(0).getId(), list.get(0).getId());
	}
}

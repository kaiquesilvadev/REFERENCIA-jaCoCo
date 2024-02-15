package com.devsuperior.dscommerce.factory;

import com.devsuperior.dscommerce.entities.Category;

public class CriaCategory {

	public static Category categoryComId() {
		return new Category(2L , "Eletrônicos");
	}
}

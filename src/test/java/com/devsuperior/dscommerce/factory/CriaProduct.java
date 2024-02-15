package com.devsuperior.dscommerce.factory;

import com.devsuperior.dscommerce.entities.Product;

public class CriaProduct {
	
	public static Product productComId() {
		Product product = new Product(
				1L , 
				"The Lord of the Rings" , 
				" Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." ,
				 90.5 , 
				"https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg" );
		
		product.getCategories().add(CriaCategory.categoryComId());
		return product;
	}
}

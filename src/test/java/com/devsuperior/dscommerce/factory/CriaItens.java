package com.devsuperior.dscommerce.factory;

import com.devsuperior.dscommerce.entities.OrderItem;

public class CriaItens {

	public static OrderItem novoItens() {
	
		OrderItem item = new OrderItem(
				CriaOrder.novoOrder() , 
				CriaProduct.novoProdut() , 
				3 ,
				200.0);
		
		return item;
	}
}

package com.devsuperior.dscommerce.factory;

import java.time.Instant;

import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.OrderStatus;

public class CriaOrder {

	public static Order novoOrder() {

		Order order = new Order();
		order.setId(2L);
		order.setMoment(Instant.now());
		order.setStatus(OrderStatus.WAITING_PAYMENT);
		order.setClient(CriaUser.novoUser());

		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		orderItem.setProduct(CriaProduct.productComId());
		orderItem.setPrice(25.0);
		orderItem.setQuantity(2);

		order.getItems().add(orderItem);

		return order;

	}
}

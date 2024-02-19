package com.devsuperior.dscommerce.factory;

import java.time.LocalDate;

import com.devsuperior.dscommerce.entities.Role;
import com.devsuperior.dscommerce.entities.User;

public class CriaUser {

	public static User novoUser() {

		User user = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", LocalDate.parse("2001-07-25"),
				"123456");

		user.addRole(new Role(1l, "ROLE_CLIENT"));

		return user;
	}
	
	public static User userAdmin() {

		User user = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", LocalDate.parse("2001-07-25"),
				"123456");

		user.addRole(new Role(1l, "ROLE_ADMIN"));

		return user;
	}
	
	public static User userClient() {

		User user = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", LocalDate.parse("2001-07-25"),
				"123456");

		user.addRole(new Role(1l, "ROLE_CLIENT"));

		return user;
	}
}

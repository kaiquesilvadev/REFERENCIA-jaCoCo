package com.devsuperior.dscommerce.factory;

import java.util.ArrayList;
import java.util.List;

import com.devsuperior.dscommerce.projections.UserDetailsProjection;

public class CriaUserDetailsProjection {

	public static List<UserDetailsProjection> novaProjection() {
		UserDetailsProjection projection = new UserDetailsProjection() {
			
			@Override
			public String getUsername() {
				return "maria@gmail.com";
			}
			
			@Override
			public Long getRoleId() {
				return 1L;
			}
			
			@Override
			public String getPassword() {
				return "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO";
			}
			
			@Override
			public String getAuthority() {
				return "ROLE_CLIENT";
			}
		};
		
		List<UserDetailsProjection> detailsProjections = new ArrayList<>();
		detailsProjections.add(projection);
		
		return detailsProjections;
	}
}

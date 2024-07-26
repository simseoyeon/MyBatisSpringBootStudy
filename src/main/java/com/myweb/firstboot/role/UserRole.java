package com.myweb.firstboot.role;

import lombok.Getter;

@Getter
public enum UserRole { //User의 권한
	ADMIN("ROLE_ADMIN"),
	MANAGER("ROLE_MANAGER"),
	USER("ROLE_USER");
	
	UserRole(String value) {
		this.value = value;
	}

	private String value;
}

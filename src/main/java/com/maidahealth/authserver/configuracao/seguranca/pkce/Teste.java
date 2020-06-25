package com.maidahealth.authserver.configuracao.seguranca.pkce;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Teste {

	
	public static void main(String[] args) {
		String senhaAdmin = new BCryptPasswordEncoder().encode("admin");
		String senhaUser = new BCryptPasswordEncoder().encode("123");
		
		System.out.println(senhaAdmin);
		System.out.println(senhaUser);
	}
}

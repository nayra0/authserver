package com.maidahealth.authserver.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.maidahealth.authserver.modelo.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Integer> {
	
	Optional<Cliente> findByClientId(String clientId);

}
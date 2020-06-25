package com.maidahealth.authserver.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.maidahealth.authserver.modelo.PerfisUsuarioPorCliente;

public interface PerfisUsuarioPorClienteRepository extends CrudRepository<PerfisUsuarioPorCliente, Integer> {

	Optional<PerfisUsuarioPorCliente> findByUsuarioEmailAndClienteClientId(String emailUsuario, String clientId);

}
package com.maidahealth.authserver.configuracao.seguranca;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import com.maidahealth.authserver.modelo.Cliente;
import com.maidahealth.authserver.repositorio.ClienteRepository;

@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

	@Autowired
	private ClienteRepository clientRepository;

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		Optional<Cliente> cliente = clientRepository.findByClientId(clientId);

		if (cliente.isPresent()) {
			return cliente.get();
		} else {
			throw new UsernameNotFoundException("Client não autorizado.");
		}
	}

}

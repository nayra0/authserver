package com.maidahealth.authserver.configuracao.seguranca;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.maidahealth.authserver.modelo.Usuario;
import com.maidahealth.authserver.repositorio.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

		if (usuario.isPresent()) {
			return usuario.get();
		} else {
			throw new UsernameNotFoundException("Usuário não autorizado.");
		}
	}

}

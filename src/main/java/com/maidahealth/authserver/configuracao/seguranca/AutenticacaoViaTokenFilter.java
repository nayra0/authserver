package com.maidahealth.authserver.configuracao.seguranca;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.maidahealth.authserver.modelo.PerfisUsuarioPorCliente;
import com.maidahealth.authserver.modelo.Usuario;
import com.maidahealth.authserver.repositorio.PerfisUsuarioPorClienteRepository;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

	PerfisUsuarioPorClienteRepository perfisUsuarioPorClienteRepository;

	public AutenticacaoViaTokenFilter(PerfisUsuarioPorClienteRepository perfisUsuarioPorClienteRepository) {
		this.perfisUsuarioPorClienteRepository = perfisUsuarioPorClienteRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getParameter("client_id") != null
				&& SecurityContextHolder.getContext().getAuthentication() != null) {

			String clientId = request.getParameter("client_id");
			Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			Optional<PerfisUsuarioPorCliente> perfilUsuarioPorCliente = perfisUsuarioPorClienteRepository
					.findByUsuarioEmailAndClienteClientId(usuario.getEmail(), clientId);

			if (perfilUsuarioPorCliente.isPresent()) {
				PerfisUsuarioPorCliente perfil = perfilUsuarioPorCliente.get();
				Set<String> roles = StringUtils.commaDelimitedListToSet(perfil.getPerfis());
				Collection<SimpleGrantedAuthority> authorities = obterListaDeAuthority(roles);
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usuario,
						SecurityContextHolder.getContext().getAuthentication().getCredentials(), authorities);
				token.setDetails(SecurityContextHolder.getContext().getAuthentication().getDetails());

				SecurityContextHolder.getContext().setAuthentication(token);
			}

		}

		filterChain.doFilter(request, response);
	}

	private Collection<SimpleGrantedAuthority> obterListaDeAuthority(Set<String> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toSet());
	}

}

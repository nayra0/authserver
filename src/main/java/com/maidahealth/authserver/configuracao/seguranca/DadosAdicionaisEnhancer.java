package com.maidahealth.authserver.configuracao.seguranca;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.maidahealth.authserver.modelo.PerfisUsuarioPorCliente;
import com.maidahealth.authserver.modelo.Usuario;
import com.maidahealth.authserver.repositorio.PerfisUsuarioPorClienteRepository;

@Component
public class DadosAdicionaisEnhancer implements TokenEnhancer {

	@Autowired
	PerfisUsuarioPorClienteRepository perfisUsuarioPorClienteRepository;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
			return accessToken;
		}

		String clientId = authentication.getOAuth2Request().getClientId();
		Usuario usuario = (Usuario) authentication.getPrincipal();
		Map<String, Object> additionalInformation = new HashMap<>();

		Optional<PerfisUsuarioPorCliente> perfilUsuarioPorCliente = perfisUsuarioPorClienteRepository
				.findByUsuarioEmailAndClienteClientId(usuario.getEmail(), clientId);

		if (perfilUsuarioPorCliente.isPresent()) {
			PerfisUsuarioPorCliente perfil = perfilUsuarioPorCliente.get();
			Set<String> roles = StringUtils.commaDelimitedListToSet(perfil.getPerfis());

			additionalInformation.put("authorities", roles);
		}

		DefaultOAuth2AccessToken defaultAccessToken = (DefaultOAuth2AccessToken) accessToken;

		defaultAccessToken.setAdditionalInformation(additionalInformation);

		return defaultAccessToken;
	}

}

package com.maidahealth.authserver.modelo;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "oauth_client_details")
public class Cliente implements ClientDetails {

	private static final long serialVersionUID = 1L;

	private static final ObjectMapper mapper = new ObjectMapper();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "client_id")
	private String clientId;

	@Column(name = "resource_ids")
	private String resources;

	@Column(name = "client_secret")
	private String clientSecret;

	@Column(name = "authorities")
	private String perfis;

	@Column(name = "scope")
	private String scopes;

	@Column(name = "authorized_grant_types")
	private String authorizedGrantTypes;

	@Column(name = "web_server_redirect_uri")
	private String registeredRedirectUri;

	@Column(name = "access_token_validity")
	private Integer accessTokenValiditySeconds;

	@Column(name = "refresh_token_validity")
	private Integer refreshTokenValiditySeconds;

	@Column(name = "additional_information")
	private String additionalInformation;

	@Column(name = "autoapprove")
	private String autoApprove;

	@Override
	public String getClientId() {
		return this.clientId;
	}

	@Override
	public Set<String> getResourceIds() {
		return StringUtils.commaDelimitedListToSet(this.resources);
	}

	// Secret é obrigatória quando client é confidential
	@Override
	public boolean isSecretRequired() {
		return true;
	}

	@Override
	public String getClientSecret() {
		return this.clientSecret;
	}

	@Override
	public boolean isScoped() {
		return scopes != null && !scopes.trim().isEmpty();
	}

	@Override
	public Set<String> getScope() {
		return StringUtils.commaDelimitedListToSet(this.scopes);
	}

	@Override
	public Set<String> getAuthorizedGrantTypes() {
		return StringUtils.commaDelimitedListToSet(this.authorizedGrantTypes);
	}

	@Override
	public Set<String> getRegisteredRedirectUri() {
		return StringUtils.commaDelimitedListToSet(this.registeredRedirectUri);
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
//		Collection<GrantedAuthority> perfis = this.authorities.stream().map(perfil -> perfil)
//				.collect(Collectors.toList());
//		return perfis;

		Set<String> set = StringUtils.commaDelimitedListToSet(this.perfis);

		Set<GrantedAuthority> result = new HashSet<>();
		set.forEach(authority -> result.add(new GrantedAuthority() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				return authority;
			}
		}));
		return result;
	}

	@Override
	public Integer getAccessTokenValiditySeconds() {
		return this.accessTokenValiditySeconds;
	}

	@Override
	public Integer getRefreshTokenValiditySeconds() {
		return this.refreshTokenValiditySeconds;
	}

	@Override
	public boolean isAutoApprove(String scope) {
		return Boolean.valueOf(this.autoApprove);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAdditionalInformation() {
		try {
			return mapper.readValue(this.additionalInformation, Map.class);
		} catch (IOException e) {
			return new HashMap<>();
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String getPerfis() {
		return perfis;
	}

	public void setPerfis(String perfis) {
		this.perfis = perfis;
	}

	public String getScopes() {
		return scopes;
	}

	public void setScopes(String scopes) {
		this.scopes = scopes;
	}

	public String getAutoApprove() {
		return autoApprove;
	}

	public void setAutoApprove(String autoApprove) {
		this.autoApprove = autoApprove;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
		this.authorizedGrantTypes = authorizedGrantTypes;
	}

	public void setRegisteredRedirectUri(String registeredRedirectUri) {
		this.registeredRedirectUri = registeredRedirectUri;
	}

	public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}

	public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

}

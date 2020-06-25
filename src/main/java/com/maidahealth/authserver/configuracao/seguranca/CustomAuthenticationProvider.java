//package com.maidahealth.authserver.configuracao.seguranca;
//
//import java.nio.charset.Charset;
//import java.util.Base64;
//import java.util.Collection;
//import java.util.Optional;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.support.MessageSourceAccessor;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.encoding.PasswordEncoder;
//import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.SpringSecurityMessageSource;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.maidahealth.authserver.modelo.PerfisUsuarioPorCliente;
//import com.maidahealth.authserver.repositorio.PerfisUsuarioPorClienteRepository;
//
//@SuppressWarnings("deprecation")
//@Component
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//	@Autowired
//	PerfisUsuarioPorClienteRepository perfisUsuarioPorClienteRepository;
//
//	@Autowired
//	private UserDetailsServiceImpl userDetailsService;
//
//	private PasswordEncoder passwordEncoder;
//
//	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
//
//	protected final Log logger = LogFactory.getLog(getClass());
//
//	public CustomAuthenticationProvider() {
//		setPasswordEncoder(new PlaintextPasswordEncoder());
//	}
//
//	@Override
//	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//
//		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
//		UserDetails usuario;
//
//		try {
//			usuario = obterUsuario(username, (UsernamePasswordAuthenticationToken) authentication);
//		} catch (UsernameNotFoundException notFound) {
//			logger.debug("User '" + username + "' not found");
//
//			throw new BadCredentialsException(
//					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
//		}
//
//		try {
//			validarCredenciais(usuario, (UsernamePasswordAuthenticationToken) authentication);
//		} catch (AuthenticationException exception) {
//			throw exception;
//		}
//
//		String clientId = obterClientId();
//		Optional<PerfisUsuarioPorCliente> perfilUsuarioPorCliente = perfisUsuarioPorClienteRepository
//				.findByUsuarioEmailAndClienteClientId(username.toLowerCase(), clientId);
//
//		if (perfilUsuarioPorCliente.isPresent()) {
//			PerfisUsuarioPorCliente perfil = perfilUsuarioPorCliente.get();
//
//			Set<String> roles = StringUtils.commaDelimitedListToSet(perfil.getPerfis());
//
//			Collection<SimpleGrantedAuthority> authorities = obterListaDeAuthority(roles);
//			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usuario,
//					authentication.getCredentials(), authorities);
//			token.setDetails(authentication.getDetails());
//
//			return token;
//
//		} else {
//			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usuario,
//					authentication.getCredentials(), null);
//			token.setDetails(authentication.getDetails());
//			return token;
//		}
//
//	}
//
//	protected void validarCredenciais(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
//			throws AuthenticationException {
//		Object salt = null;
//
//		if (authentication.getCredentials() == null) {
//			logger.debug("Authentication failed: no credentials provided");
//
//			throw new BadCredentialsException(
//					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
//		}
//
//		String passwordApresentado = authentication.getCredentials().toString();
//
//		if (!passwordEncoder.isPasswordValid(userDetails.getPassword(), passwordApresentado, salt)) {
//			logger.debug("Authentication failed: password does not match stored value");
//
//			throw new BadCredentialsException(
//					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
//		}
//	}
//
//	protected final UserDetails obterUsuario(String username, UsernamePasswordAuthenticationToken authentication)
//			throws AuthenticationException {
//		UserDetails usuarioCarregado;
//
//		try {
//			usuarioCarregado = this.userDetailsService.loadUserByUsername(username);
//		} catch (UsernameNotFoundException notFound) {
//			throw notFound;
//		} catch (Exception repositoryProblem) {
//			throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
//		}
//
//		if (usuarioCarregado == null) {
//			throw new InternalAuthenticationServiceException(
//					"UserDetailsService returned null, which is an interface contract violation");
//		}
//		return usuarioCarregado;
//	}
//
//	private void setPasswordEncoder(PasswordEncoder passwordEncoder) {
//		this.passwordEncoder = passwordEncoder;
//	}
//
//	protected PasswordEncoder getPasswordEncoder() {
//		return passwordEncoder;
//	}
//
//	private Collection<SimpleGrantedAuthority> obterListaDeAuthority(Set<String> roles) {
//		return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toSet());
//	}
//
//	@Override
//	public boolean supports(Class<?> authentication) {
//		return authentication.equals(UsernamePasswordAuthenticationToken.class);
//	}
//
//	private String obterClientId() {
//		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//				.getRequest();
//
//		final String authorizationHeader = request.getHeader("Authorization");
//
//		if (authorizationHeader != null) {
//			final String base64AuthorizationHeader = Optional.ofNullable(authorizationHeader)
//					.map(header -> header.substring("Basic ".length())).orElse("");
//
//			if (base64AuthorizationHeader != null && !base64AuthorizationHeader.trim().isEmpty()) {
//				String decodedAuthorizationHeader = new String(Base64.getDecoder().decode(base64AuthorizationHeader),
//						Charset.forName("UTF-8"));
//				return decodedAuthorizationHeader.split(":")[0];
//			}
//		}
//
//		return "";
//	}
//
//}
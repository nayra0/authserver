package com.maidahealth.authserver.configuracao.seguranca.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.maidahealth.authserver.configuracao.seguranca.AutenticacaoViaTokenFilter;
import com.maidahealth.authserver.configuracao.seguranca.UserDetailsServiceImpl;
import com.maidahealth.authserver.repositorio.PerfisUsuarioPorClienteRepository;

@EnableWebSecurity
public class ConfiguracaoDeSeguranca {

	@Configuration
	public static class ConfiguracaoParaUsuario extends WebSecurityConfigurerAdapter {

		@Autowired
		PerfisUsuarioPorClienteRepository perfisUsuarioPorClienteRepository;
		
		@Autowired
		private UserDetailsServiceImpl userDetailsService;
		
		@Bean
		public PasswordEncoder passwordEncoder() {
		    return new BCryptPasswordEncoder();
		}
		
		@Bean
		public DaoAuthenticationProvider authProvider() {
		    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		    authProvider.setUserDetailsService(userDetailsService);
		    authProvider.setPasswordEncoder(passwordEncoder());
		    return authProvider;
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(authProvider());
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			String[] caminhosPermitidos = new String[] { "/", "/home", "/webjars/**", "/static/**", "/jquery*" };

			http.authorizeRequests().antMatchers(caminhosPermitidos).permitAll().anyRequest().authenticated().and()
					.formLogin().permitAll().loginPage("/login")
					.and().logout().permitAll()
					.and().csrf().disable()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()
					.addFilterBefore(new AutenticacaoViaTokenFilter(perfisUsuarioPorClienteRepository), UsernamePasswordAuthenticationFilter.class)
					;

		}

	}

}

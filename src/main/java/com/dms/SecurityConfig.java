package com.dms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity(debug = false)
class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private CustomAuthenticationSuccessHandler successHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		applySecurity(http);
		http.csrf().disable();

		// cache resources
		http.headers().addHeaderWriter(new DelegatingRequestMatcherHeaderWriter(
				new AntPathRequestMatcher("/javax.faces.resource/**"), new HeaderWriter() {

					@Override
					public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
						response.addHeader("Cache-Control", "private, max-age=86400");
					}
				})).defaultsDisabled();
	}

	private void applySecurity(HttpSecurity http) throws Exception {
		// removed "/faces/*"
		http.authorizeRequests()
				.antMatchers("/", "/landing.xhtml", "/landing", "/404.xhtml", "/error.xhtml", "/company.xhtml",
						"/company/*", "/contactUs.xhtml", "/contactUs", "/aboutUs.xhtml", "/activate.xhtml",
						"/searchCompanies.xhtml", "/findInvoiceStatus.xhtml", "/findInvoiceStatus", "/resources/**",
						"/saas/**", "/WEB-INF/**", "/template/**", "/faces/fonts/*", "/javax.faces.resource/**",
						"/login.xhtml", "/share.xhtml", "/share", "/login", "/register.xhtml", "/register",
						"/generateNewCaptcha", "/error")
				.permitAll().antMatchers("/manageRegistration.xhtml").hasAuthority("ROLE_ADMIN")
				.antMatchers("/company/**").hasAnyAuthority("ROLE_COMPANY").anyRequest().fullyAuthenticated().and()
				.formLogin().loginPage("/login").loginProcessingUrl("/login").permitAll().successHandler(successHandler)
				.failureUrl("/login?error").usernameParameter("username").passwordParameter("password").and().logout()
				.deleteCookies("JSESSIONID").logoutUrl("/logout").deleteCookies("remember-me").logoutSuccessUrl("/")
				.permitAll().and().rememberMe();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}

}
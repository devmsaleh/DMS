package com.dms.service;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dms.dao.UserRepository;
import com.dms.entities.User;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CurrentUserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public User findByUserNameIgnoreCase(String username) {
		User user = userRepository.findByUserNameIgnoreCase(username);
		if (user != null) {
			Hibernate.initialize(user.getRoles());
		}
		return user;
	}

	public void save(User user) {
		userRepository.save(user);
	}

	public boolean isAuthenticated(String userName, String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, password, userDetails.getAuthorities());
		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		return authentication.isAuthenticated();
	}

}

package com.dms;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.dms.entities.Role;
import com.dms.entities.User;
import com.dms.enums.RoleTypeEnum;
import com.dms.service.UserService;
import com.dms.util.Constants;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String targetUrl = decideTargetUrl(request, response, authentication);
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	private String decideTargetUrl(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		String targetUrl = "/searchDocument.xhtml";
		User user = userService.findByUserNameIgnoreCase(authentication.getName());
		if (isUserHasRole(user, RoleTypeEnum.ROLE_ADMIN)) {

		} else if (isUserHasRole(user, RoleTypeEnum.ROLE_COMPANY)) {

		}
		user.setLastLoginDate(new Date());
		userService.save(user);
		request.getSession().setAttribute(Constants.CURRENT_USER_SESSION_ATTRIBUTE, user);

		return targetUrl;
	}

	private boolean isUserHasRole(User user, RoleTypeEnum roleTypeEnum) {
		for (Role role : user.getRoles()) {
			if (role.getName().equalsIgnoreCase(roleTypeEnum.getName()))
				return true;
		}
		return false;
	}

}

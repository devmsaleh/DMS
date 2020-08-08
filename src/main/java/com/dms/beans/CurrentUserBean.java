package com.dms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.dms.dao.UserRepository;
import com.dms.entities.Role;
import com.dms.entities.User;
import com.dms.enums.RoleTypeEnum;
import com.dms.service.UserService;

@Component("currentUserBean")
@Scope("session")
public class CurrentUserBean implements Serializable {

	private static final long serialVersionUID = -3493756271104748886L;

	private static final Logger log = LoggerFactory.getLogger(CurrentUserBean.class);

	private User user;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	private boolean admin;

	private boolean startTimer;

	private List<String> resultUUIDList = new ArrayList<String>();

	@PostConstruct
	public void init() {
		try {

		} catch (Exception e) {
			log.error("Exception in init CurrentUserBean", e);
		}
	}

	public void extendSession() {
		startTimer = false;
	}

	public void onIdleScreen() {
		startTimer = true;
	}

	public void onActiveScreen() {
		startTimer = false;
	}

	private void loadUserData() {
		if (user == null && !getLoggedInUserId().equalsIgnoreCase("anonymousUser")) {
			user = userService.findByUserNameIgnoreCase(getLoggedInUserId());
			admin = isUserHasRole(user, RoleTypeEnum.ROLE_ADMIN);
		}
	}

	private String getLoggedInUserId() {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			return "anonymousUser";
		}
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal.getClass().equals(org.springframework.security.core.userdetails.User.class)) {
			org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) principal;
			return springUser.getUsername();
		} else
			return "anonymousUser";

	}

	private boolean isUserHasRole(User user, RoleTypeEnum roleTypeEnum) {
		for (Role role : user.getRoles()) {
			if (role.getName().equalsIgnoreCase(roleTypeEnum.getName()))
				return true;
		}
		return false;
	}

	public User getUser() {
		loadUserData();
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isAdmin() {
		loadUserData();
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isStartTimer() {
		return startTimer;
	}

	public void setStartTimer(boolean startTimer) {
		this.startTimer = startTimer;
	}

	public List<String> getResultUUIDList() {
		return resultUUIDList;
	}

	public void setResultUUIDList(List<String> resultUUIDList) {
		this.resultUUIDList = resultUUIDList;
	}

}

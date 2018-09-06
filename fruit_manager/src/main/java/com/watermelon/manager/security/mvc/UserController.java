package com.watermelon.manager.security.mvc;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import com.watermelon.manager.security.entity.User;
import com.watermelon.manager.security.service.LoginService;


/**
 * 后台帐号管理
 *
 */
@Controller
public class UserController {
	
	@Autowired
	private LoginService loginService;
	
	@RequestMapping(value="login.do", method = RequestMethod.POST)
	private String login(Model model, String userName, String password) {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		User user = loginService.getUser(userName);
		if(user == null || !user.getPassword().equals(password)){
			return "../../../login";
		}
		WebUtils.setSessionAttribute(req, "user", user);
		
		return "index";
	}
	
	@RequestMapping(value = "updatePassword.do", method = RequestMethod.GET)
	public String updatePassword() {
		return "security/updatePassword";
	}

//	@RequestMapping(value = "listUser", method = RequestMethod.GET)
//	public String listUser(Model model) {
//		List<User> user = securityService.listUser();
//		model.addAttribute("users", user);
//		return "listUser";
//	}

//	@RequestMapping(value = "addUser", method = RequestMethod.GET)
//	public String addUser(Model model) {
//		UserDetails userDetail = UserUtil.getUserDetails();
//		List<Group> groupList = securityService.listGroup(securityService.getUser(userDetail.getUsername()));
//		model.addAttribute("groupList", groupList);
//		return "addUser";
//	}
//
//	@RequestMapping(value = "addUser", method = RequestMethod.POST)
//	public String addUser(UserForm user, Model model) {
//		securityService.createUser(user.getName(), user.getTrueName(), "123456", null, user.getGroups());
//		return listUser(model);
//	}

}

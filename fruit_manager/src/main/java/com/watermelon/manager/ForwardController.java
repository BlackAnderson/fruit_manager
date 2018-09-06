package com.watermelon.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.watermelon.manager.security.entity.UserDetails;

/**
 * 页面 Forward 控制器<br/>
 * 如: /security/index.html 或 /security/index.jsp 甚至 /security/index <br/>
 * 将会用 /WEB-INF/views/security/index.jsp 进行显示
 * 
 * @author Frank
 */
@Controller("gameForward")
public class ForwardController {

	
	/** 转发全部请求 */
	@RequestMapping("/{view}")
	public String forward(@PathVariable String view, Model model) {
		
		UserDetails userDetails = null;
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (obj instanceof UserDetails) {
			userDetails = (UserDetails) obj;
		}
		if (userDetails != null) {
//			List<Operator> operatorList = serverService.listOperatorByUserName(userDetails.getUsername());
//			model.addAttribute("operatorList", operatorList);
//			model.addAttribute("userDetail", UserUtil.getUserDetails());
		} else {
			return "redirect:../relogin.jsp";
		}
		return view;
	}

}

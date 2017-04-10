package com.cross.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cross.pojo.TUser;
import com.cross.service.TUserService;

/**
 * 后台登录
 * @author panhongguang
 *
 */
@Controller
@RequestMapping(value="/shiro")
public class ShiroController {
	
	@Autowired
	TUserService tUserService;
	
	@RequestMapping(value="/login")
	public String login(TUser tUser, Model model) {
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(tUser.getUserName(), tUser.getPassword());
		try {
			subject.login(token);
			return "admin";
		} catch (Exception e) {
			model.addAttribute("error","用户名或密码错误") ;
            return "../../login";
		}
		
	}
	
	@RequestMapping("/admin")
    public String admin(){
        return "admin";
    }
	
    @RequestMapping("/student")
    public String student(){
        return "admin" ;
    }
    
    @RequestMapping("/teacher")
    public String teacher(){
        return "admin" ;
    }

}

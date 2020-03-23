package com.xs.controller;

import com.xs.pojo.ResponseBo;
import com.xs.pojo.User;
import com.xs.pojo.UserVo;
import com.xs.utils.MD5Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	@ResponseBody
	public ResponseBo login(@RequestBody UserVo vo) {
		String password = MD5Utils.encrypt(vo.getUsername(), vo.getPassword());
		UsernamePasswordToken token = new UsernamePasswordToken(vo.getUsername(), password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);

			return ResponseBo.ok();
		} catch (UnknownAccountException e) {
			return ResponseBo.error(e.getMessage());
		} catch (IncorrectCredentialsException e) {
			return ResponseBo.error(e.getMessage());
		} catch (LockedAccountException e) {
			return ResponseBo.error(e.getMessage());
		} catch (AuthenticationException e) {
			return ResponseBo.error("认证失败！");
		}
	}

	@RequestMapping("/")
	public String redirectIndex() {
		return "redirect:/index";
	}

	@RequestMapping("/index")
	public String index(Model model) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		model.addAttribute("user", user);
		return "index";
	}
}

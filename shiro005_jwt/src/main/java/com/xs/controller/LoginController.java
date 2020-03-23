package com.xs.controller;

import com.xs.pojo.ResponseBo;
import com.xs.pojo.User;
import com.xs.shiro.sms.PhoneToken;
import com.xs.utils.MD5Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.security.pkcs11.wrapper.Constants;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	@ResponseBody
	public ResponseBo login(String username, String password,Boolean rememberMe) {
		password = MD5Utils.encrypt(username, password);
		UsernamePasswordToken token = new UsernamePasswordToken(username, password,rememberMe);
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
	// 使用手机号和短信验证码登录
	@RequestMapping("/plogin")
	@ResponseBody
	public ResponseBo pLogin(@RequestParam("phone") String phone, @RequestParam("code") String code, HttpSession session){

		session.setAttribute(phone,"22222");
		// 根据phone从session中取出发送的短信验证码，并与用户输入的验证码比较
		String messageCode = (String) session.getAttribute(phone);

		if(messageCode!=null&&messageCode!="" && messageCode.equals(code)){

			PhoneToken token = new PhoneToken(phone);

			Subject subject = SecurityUtils.getSubject();

			subject.login(token);
			User user = (User) subject.getPrincipal();
			session.setAttribute("user", user);
			return ResponseBo.ok("登录成功！");

		}else{
			return ResponseBo.error(2,"验证码错误！");
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




	@GetMapping("/article")
	public ResponseBo article() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			return  ResponseBo.ok("You are already logged in");
		} else {
			return  ResponseBo.ok("You are guest");
		}
	}

	@GetMapping("/require_auth")
	@RequiresAuthentication
	public ResponseBo requireAuth() {
		return ResponseBo.ok("You are authenticated");
	}

	@GetMapping("/require_role")
	@RequiresRoles("admin")
	public ResponseBo requireRole() {
		return ResponseBo.ok("You are visiting require_role");
	}

	@GetMapping("/require_permission")
	@RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
	public ResponseBo requirePermission() {
		return  ResponseBo.ok( "You are visiting permission require edit,view");
	}

	@RequestMapping(path = "/401")
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseBo unauthorized() {
		return ResponseBo.ok(401, "Unauthorized");
	}


}

package tqs.group4.bestofbooks.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.group4.bestofbooks.dto.UserDto;
import tqs.group4.bestofbooks.exception.LoginFailedException;
import tqs.group4.bestofbooks.exception.LoginRequiredException;
import tqs.group4.bestofbooks.exception.UserNotFoundException;
import tqs.group4.bestofbooks.service.LoginServices;

@RestController
@RequestMapping("/api/session")
public class SessionController {

	@Autowired
	private LoginServices loginService;
	
	@GetMapping("/login")
	public UserDto login(HttpServletRequest request) throws LoginFailedException, UserNotFoundException {
		String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		String[] headerParts = auth.trim().split(" ");

		if (headerParts.length != 2) {
			throw new LoginFailedException("Bad authorization header");
		} else if (!headerParts[0].equals("Basic")) {
			throw new LoginFailedException("Unsupported authorization header type.");
		}

		String[] decodedTokenParts = new String(Base64.getDecoder().decode(headerParts[1])).split(":");

		if (decodedTokenParts.length != 2) {
			throw new LoginFailedException("Bad authorization header");
		}
		
		String username = decodedTokenParts[0];
		String password = decodedTokenParts[1];

		UserDto user = loginService.loginUser(username, password);
		
		
		request.getSession().setAttribute("username", user.getUsername());


		return user;
	}
	
	@GetMapping("/user-info")
	public UserDto getUserInfo(HttpServletRequest request) throws LoginFailedException, UserNotFoundException, LoginRequiredException{
		String user = (String) request.getSession().getAttribute("username");
		
		if (user == null) {
			throw new LoginRequiredException("Login Required for this request.");
		}
		UserDto u = loginService.getUserDtoByUsername(user);
		return u;
	}
	
	
}

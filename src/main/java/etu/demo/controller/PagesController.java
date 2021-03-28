package etu.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller

public class PagesController {
	
	@GetMapping("/login")
	public String home() {
		return "cnx/login";
	}
	
	@GetMapping("/inscription")
	public String inscription() {
		return "cnx/register";
	}
	@GetMapping("/jouer")
	public String jouer() {
		return "jouer";
	}
}

package com.project.eat.Register;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Slf4j
@Controller
public class HomeController {

	@GetMapping({"/","/home"})
	public String home(Model model) {
		log.info("/home...");
		
		Date to_day = new Date();//Wed Feb 21 16:35:13 KST 2024
		model.addAttribute("to_day",to_day);
		
//		return "thymeleaf/Register/register";
		return "register";
	}



	
	
	
}

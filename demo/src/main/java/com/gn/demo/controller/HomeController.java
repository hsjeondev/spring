package com.gn.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gn.demo.vo.Member;

@Controller
public class HomeController {
	
	@GetMapping({"","/"})
	public ModelAndView home() {
		// src/main/resources/templates/home.html
		ModelAndView mav = new ModelAndView();
		mav.setViewName("home");
		mav.addObject("age",1);
		return mav;
	}
	
	@GetMapping("/test")
	public String test(Model model) {
		// request.setAttribute("name", "김철수");과 같음
		model.addAttribute("name", "김철수");
		return "test";
	}
	
	@GetMapping("/bye")
	public String bye(Model model) {
		Member member = new Member("홍길동", 50);
		model.addAttribute("member", member);
		return "/member/bye";
	}
}
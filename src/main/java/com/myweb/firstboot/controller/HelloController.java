package com.myweb.firstboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myweb.firstboot.dto.MemberDto;

@RequestMapping("/hello") //클래스 자체에 Mapping
@Controller
public class HelloController {
	@GetMapping("/hello")
	@ResponseBody
	public String hello() {
		return "Hello Spring Boot!!";
	}
	
	//파라메터 받는 법
	@GetMapping("/testParam")
	@ResponseBody
	public String getRequestParam(@RequestParam (value="name") String name) {
		return "name = " + name;
	}
	
	//파라메터를 여러 개 받는 법
	@GetMapping("/testParams")
	@ResponseBody
	public String getRequestParams(@RequestParam (value="name") String name,
									@RequestParam (value="age") String age) {
		return "name = " + name + ", age = " + age;
	}
	
	//다량의 데이터를 받을 때 DTO사용
	@GetMapping("/testParamDto")
	@ResponseBody
	public String getRequestParams(MemberDto member) {
		return member.toString();
	}
	
	//파라메터를 변수로 받을 때
	@GetMapping("/variable/{variable}")
	@ResponseBody
	public String getRequestVal(@PathVariable("variable") String var) {
		return var;
	}
}

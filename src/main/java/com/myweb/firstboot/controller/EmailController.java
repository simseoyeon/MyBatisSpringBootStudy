package com.myweb.firstboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.myweb.firstboot.dto.EmailDto;
import com.myweb.firstboot.service.EmailService;

//@RequiredArgsConstructor
@Controller
public class EmailController {
	
	    private final EmailService email;
	    
	    @Autowired
	    public EmailController(EmailService email) {
	    	this.email = email;
	    }
	    
	    @GetMapping("/inquire")
	    public String inquireForm(){
	        return "inquireForm";
	    }

	    @PostMapping("/sendMail")
	    public String sendMail(EmailDto msg){

	        if(email.sendMailReject(msg)){
	            System.out.println("Email 전송 시작!");
	        }else System.out.println("실패");

	        return "redirect:/";
	   
	}

}
 
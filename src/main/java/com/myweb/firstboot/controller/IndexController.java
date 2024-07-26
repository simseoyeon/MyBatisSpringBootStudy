package com.myweb.firstboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;

import com.myweb.firstboot.dto.BoardDto;
import com.myweb.firstboot.service.BoardService;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice //전역 컨트롤러
//@Controller
public class IndexController {
	@Autowired
	private BoardService service;

	//메뉴
	@ModelAttribute("menu")
	public List<BoardDto> getMenu(){
		return service.getBoardMenu();
	}
}

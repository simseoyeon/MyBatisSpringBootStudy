package com.myweb.firstboot.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.myweb.firstboot.service.EmailService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.myweb.firstboot.dto.BoardDto;
import com.myweb.firstboot.dto.MemberDto;
import com.myweb.firstboot.service.BoardService;
import com.myweb.firstboot.service.MemberService;
import com.myweb.firstboot.service.UserSecurityService;

import lombok.RequiredArgsConstructor;

//로그인, 회원 관리 기능
@RequiredArgsConstructor
@Controller
public class MemberController {
	private final MemberService memberService;
	private final UserSecurityService userService;
	private final BoardService service;
	private final EmailService emailService;
	
	//로그인
	@GetMapping("/login")
	public String loginPage(Model model) {
		//게시판 메뉴
		List<BoardDto> menu = service.getBoardMenu();
		model.addAttribute("menu", menu);
		return "login";
	}
	/*
	@PostMapping("/login")
	public String loginAply(@RequestParam (value="userid") String userid,
							@RequestParam (value="userpwd") String usepwd) {
		//서비스에게 요청
		if(memberService.checkLogin(userid, usepwd)) {
			return "index";
		}
	
		return "login";
	}
	*/
	//회원가입
	@GetMapping("/join")
	public String joinPage(MemberDto dto, Model model) {
		//게시판 메뉴
		List<BoardDto> menu = service.getBoardMenu();
		model.addAttribute("menu", menu);
		return "join";
	}
	
	@PostMapping("/join") //DB에 저장
	public String joinAply(MemberDto dto, Model model) {
		userService.create(dto);
		
		//게시판 메뉴
		List<BoardDto> menu = service.getBoardMenu();
		model.addAttribute("menu", menu);
		
		return "index";
	}
	
	//아이디 중복 확인
	@GetMapping("/checkid")
	@ResponseBody
	public String checkId(@RequestParam(value="data") String userid) {
			
		return String.valueOf(memberService.checkId(userid));
	} //valueof: 문자열로 변환 String클래스가 제공해주는 메서드
	
	
	//사용자 로그인 후 회원정보 요청
	@PreAuthorize("isAuthenticated()")//로그인되지 않은 사용자가 회원정보를 선택했을 때 로그인할 수 있도록 하는 어노테이션 - 로그인이 필요한 기능들에 
	@GetMapping("/member")
	public ModelAndView getMemberInfo(Principal principal) { //세션에 기록된 userid를 가져옴
		ModelAndView mav = new ModelAndView("member"); //모델과 뷰를 한꺼번에 제어하는 클래스 1)뷰를 넘겨줌
		MemberDto dto = new MemberDto();
		dto = memberService.getMemberInfo(principal.getName());
		mav.addObject("member", dto);
		
		//게시판 메뉴
		List<BoardDto> menu = service.getBoardMenu();
		mav.addObject("menu", menu);
		return mav;
	}
	//회원 정보 수정
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/member")
	public String editMemberInfo(Model model, MemberDto dto) {
		//System.out.println(dto.toString());
		//회원정보 수정 저장
		dto = memberService.editMemberInfo(dto);
		model.addAttribute("member", dto);
		
		//게시판 메뉴
		List<BoardDto> menu = service.getBoardMenu();
		model.addAttribute("menu", menu);
		return "member";
	}
	
	//회원 탈퇴
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/unregist")
	public String unregistUser(Principal principal) {
		//회원탈퇴 서비스 요청
		memberService.unregistUser(principal.getName());
		//로그아웃 다이렉트
		return "redirect:/logout";
	}
	
	//회원관리
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/manage")
	public ModelAndView getMemberList() {
		ModelAndView mav = new ModelAndView("manage");
		List<MemberDto> list = new ArrayList<>();
		list = memberService.getMemberList();
		mav.addObject("memberList", list);
		
		//게시판 메뉴
		List<BoardDto> menu = service.getBoardMenu();
		mav.addObject("menu", menu);
		
		return mav;
	}
	
	// 회원관리 - 수정
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/editUser")
	public String editUser(MemberDto dto) {
		System.out.println("***** userid = " + dto.getUserid());
		System.out.println("***** userpwd = " + dto.getUserpwd());
		System.out.println("***** permit = " + dto.getPermit());
		
		memberService.editUser(dto);
		System.out.println("심서연 바보");
		return "redirect:/manage";
	}
	
	// 회원관리 - 삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/deleteUser/{userid}")
	public String deleteUser(@PathVariable ("userid") String userid){
		System.out.println("***** userid = " + userid);
		
		memberService.deleteUser(userid);

		return "redirect:/manage";
	}

	//비밀번호 찾기
	@GetMapping ("/findpw")
	public String findPw(){
		return "findPwForm";
	}
	@PostMapping("/findpw")
	public String findpw(Model model, MemberDto dto) {
		String msg = "";
		//memberDto에 해당 회원정보가 있는지 없는지 확인
		boolean ck = memberService.checkMember(dto);
		if(ck) msg = "ok";
		else {
			msg = "error";
			model.addAttribute("msg", msg);
			return "findRwForm";
		}

		//권한 사용자 설정
		dto.setPermit(0);
		//임시 패스워드 생성
		//랜덤하게 문자열 생성 -> 임시 비밀번호 발급(8자리)
		String tmppw = UUID.randomUUID().toString().substring(0,8);

		//임시 패스워드 설정
		dto.setUserpwd(tmppw);
		memberService.editUser(dto);

		ck = emailService.makeMsgTmpPw(dto);
		if(!ck){
			msg = "메일 전송에 실패했습니다. 잠시후 다시 시도해 주세요.";
		}
		msg = "메일을 통해 임시 비밀번호를 전송했습니다. 매일을 확인해 주세요.";
		model.addAttribute("msg", msg);
		return "sendmsg";
	}

	//아이디 찾기
	@GetMapping("/findid")
	public String findId() {
		return "findIdForm";
	}

	@PostMapping("/findid")
	public String findId(Model model, MemberDto dto) {
		String msg = "";

		String userid = memberService.getMemberByName(dto);
		if(userid == null) {
			msg = "해당 아이디가 존재하지 않습니다!!";
		} else {
			msg = "당신의 아이디는 '" + userid + "' 입니다."	;
		}
		model.addAttribute("msg",msg);

		return "sendmsg";
	}
}

package com.myweb.firstboot.service;

import java.util.List;

import com.myweb.firstboot.dto.MemberDto;

public interface MemberService {
	//boolean checkLogin(String userid, String userpwd);
	boolean putMember(MemberDto dto); //회원가입
	boolean checkId(String userid); //중복확인
	MemberDto getMemberInfo(String userid);
	MemberDto editMemberInfo(MemberDto dto);
	void unregistUser(String userid);
	List<MemberDto> getMemberList();
	void editUser(MemberDto dto);
	void deleteUser(String userid);

	boolean checkMember(MemberDto dto);

	String getMemberByName(MemberDto dto);
}

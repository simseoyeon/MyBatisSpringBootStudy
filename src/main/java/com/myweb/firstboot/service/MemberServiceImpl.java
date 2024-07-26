package com.myweb.firstboot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myweb.firstboot.dao.MemberDao;
import com.myweb.firstboot.dto.MemberDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
	private final MemberDao memberDao;
	private final PasswordEncoder passwordEncoder;
	
//	@Autowired
//	public MemberServiceImpl(MemberDao memberDao) {
//		this.memberDao = memberDao;
//	}
	
//	@Override
//	public boolean checkLogin(MemberDto dto) {
//		boolean result = false;
//
//		if(memberDao.checkMember(dto)  ) result = true;
//
//		return result;
//	}
	
	@Override
	public boolean putMember(MemberDto dto) {
		
		return memberDao.insertMember(dto);
	}
	
	@Override
	public boolean checkId(String userid) {
		boolean result = false;
		
		if(memberDao.checkId(userid) == 0 ) result = true;
		return result;
	}
	
	@Override //회원정보
	public MemberDto getMemberInfo(String userid) {
		MemberDto dto = new MemberDto();
		dto = memberDao.getByUserId(userid);
		return dto;
	}
	
	@Override
	public MemberDto editMemberInfo(MemberDto dto) {
		MemberDto org = new MemberDto();
		org = memberDao.getByUserId(dto.getUserid());
		
		//뷰로 입력받은 패스워드인지 확인하여 암호화
		if(dto.getUserpwd().length() <= 20) {
			dto.setUserpwd(passwordEncoder.encode(dto.getUserpwd()));
		}
		//원본 패스워드와 뷰 패스워드가 다르면 패스워드 업데이트
		if(!dto.getUserpwd().equals(org.getUserpwd()))
			memberDao.updateUserpwd(dto.getUserid(), dto.getUserpwd());
		//원본 이름과 뷰 이름이 다르면 이름 업데이트
		if(!dto.getUsername().equals(org.getUsername()))
			memberDao.updateUsername(dto.getUserid(), dto.getUsername());
		//원본 전화번호와 뷰 전화번호가 다르면 전화번호 업데이트
		if(!dto.getTelnumber().equals(org.getTelnumber()))
			memberDao.updateTelnumber(dto.getUserid(), dto.getTelnumber());
		//원본 주소와 뷰 주소가 다르면 주소 업데이트
		if(!dto.getAddr().equals(org.getAddr()))
			memberDao.updateAddr(dto.getUserid(), dto.getAddr());
		
		return dto;
		
	}
	
	@Override
	public void unregistUser(String userid) {
		memberDao.deleteUser(userid);
	}
	
	@Override
	public List<MemberDto> getMemberList(){
		List<MemberDto> list = new ArrayList<>();
		list = memberDao.selectMember();
		return list;
	}
	
	@Override
	public void editUser(MemberDto dto) {
		MemberDto org = new MemberDto();
		org = memberDao.getByUserId(dto.getUserid());
		String chngPw ="";
		//뷰로 입력받은 패스워드인지 확인하여 암호화
		if(dto.getUserpwd().length() <= 20) {
			chngPw = passwordEncoder.encode(dto.getUserpwd());
		}
		//원본 패스워드와 뷰 패스워드가 다르면 패스워드 업데이트
		if(!dto.getUserpwd().equals(org.getUserpwd()))
			memberDao.updateUserpwd(dto.getUserid(), chngPw);
		//원본 이름과 뷰 이름이 다르면 이름 업데이트
		if(dto.getPermit() != org.getPermit())
			memberDao.updatePermit(dto.getUserid(), dto.getPermit());
	}
	
	@Override
	public void deleteUser(String userid) {
		memberDao.deleteUser(userid);
	}

	@Override
	public boolean checkMember(MemberDto dto){
		return memberDao.checkMember(dto);
	}

	@Override
	public String getMemberByName(MemberDto dto) {
		return memberDao.selectUserId(dto);
	}
}

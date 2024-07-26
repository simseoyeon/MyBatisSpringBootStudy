package com.myweb.firstboot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myweb.firstboot.dao.MemberDao;
import com.myweb.firstboot.dto.MemberDto;
import com.myweb.firstboot.role.UserRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
	private final MemberDao dao;
	private final PasswordEncoder passwordEncoder;

	
	public MemberDto create(MemberDto dto) { //회원가입시 패스워드 암호화
		dto.setUserpwd(passwordEncoder.encode(dto.getUserpwd()));
		this.dao.insertMember(dto);
		return dto;
	}
	
	
	@Override                  //Security에서 말하는 Username = userid를 뜻함
	public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
		MemberDto member = this.dao.getByUserId(userid);
		if(member == null) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		//permit설정된 값에 따라서 권한 부여
		//permit이 9의 값을 가지면 관리자의 권한
		//권한을 늘리기 위해서는 UserRole에서
		if(member.getPermit() == 9) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		}
		else if(member.getPermit() == 8){
			authorities.add(new SimpleGrantedAuthority(UserRole.MANAGER.getValue()));
		}
		else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		return new User(member.getUserid(), member.getUserpwd(), authorities);
	}

}

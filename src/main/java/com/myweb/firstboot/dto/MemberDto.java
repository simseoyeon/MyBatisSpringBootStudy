package com.myweb.firstboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor //빈생성자 만들기
@AllArgsConstructor //모든 생성자
@ToString //문자열로 받겠다는 어노테이션
public class MemberDto {
	private String userid;
	private String userpwd;
	private String username;
	private String birthdate;
	private String gender;
	private String telnumber;
	private String addr;
	private int permit;
	private String email;
	
}

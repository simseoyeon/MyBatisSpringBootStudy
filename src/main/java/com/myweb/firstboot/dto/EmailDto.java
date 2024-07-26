package com.myweb.firstboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailDto {
	private String to; //받는 사람 주소
	private String from; //보낸 사람 주소
	private String subject; //제목
	private String text; //내용
}

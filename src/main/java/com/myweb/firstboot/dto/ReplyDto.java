package com.myweb.firstboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReplyDto {
	private int reply_no;
	private int post_no;
	private String user_id;
	private String comment;
	private String create_date;
	private String update_date;
}

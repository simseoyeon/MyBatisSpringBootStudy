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
public class PostDto {
	private int post_no;
	private int board_no;
	private String title;
	private String content;
	private String user_id;
	private String create_date;
	private String update_date;
	private int hit_cnt;
}

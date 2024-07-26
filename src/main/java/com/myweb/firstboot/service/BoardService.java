package com.myweb.firstboot.service;

import java.util.List;

import com.myweb.firstboot.com.Search;
import com.myweb.firstboot.dto.*;

public interface BoardService {
	List<PostDto> getPostList(); //게시글을 받아옴
	//List<PostDto> getPostListByBoard(int board_no);
	// 페이징


	List<PostDto> getPostListByBoard(int board_no, Search search);

	List<PostDto> getPostListByKeyword(int board_no, Search page);
	int getTotPostCount(int board_no, String keyword, String searchType, int page);

	PostDto putPost(PostDto dto);
	PostDto getPost(int post_no);
	void cntHitCnt(int post_no);
	
	void delPost(int post_no); //게시글 삭제
	PostDto editPost(PostDto dto); //게시글 수정
	
	void putReply(ReplyDto reply); //댓글 입력
	List<ReplyDto> getReply(int post_no);
	
	
	//indexController
	List<BoardDto> getBoardMenu();
	
	BoardDto getBoard(int board_no);
	
	int getBoardNo(int post_no);
	
	//게시글 관리
	void editBoard(BoardDto dto); //수정
	
	void deleteBoard(int board_no); //삭제
	
	void addBoard(BoardDto dto);//추가

	//이미지 추가
	void addGalary(GalaryDto dto);

	void imgUpload(ImgmngDto imgDto);

	// 이미지 가져오기
	List<GalaryDto> getGalaryList();

	//이미지 다운로드
	ImgmngDto downloadImage(int id);
	List<ImgmngDto> downloadImageList(int galary_id);
	ImgmngDto getGalaryId(int galary_id);
	GalaryDto getGalary(int galary_id);
}

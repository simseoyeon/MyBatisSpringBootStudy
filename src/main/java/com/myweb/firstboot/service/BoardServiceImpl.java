package com.myweb.firstboot.service;

import java.util.List;

import com.myweb.firstboot.com.Search;
import com.myweb.firstboot.dto.*;
import org.springframework.stereotype.Service;

import com.myweb.firstboot.dao.BoardDao;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {
	
	private final BoardDao dao;
	
	public List<PostDto> getPostList(){
		return dao.selectPostListAll();
	}
	
//	//오버로딩 페이징과 같이 합함
//	public List<PostDto> getPostListByBoard(int board_no){
//		return dao.selectPostListByBoardNo(board_no);
//	}

	// 페이징 기능
	/*변경 사항 : 40번째 줄
	   dao.selectPostCntByBoardNo
	-> dao.selectPostCntByKeyword

	return 값
	   dao.selectPostListByBoardNo
	-> dao.selectPostListByKeyword
	출처 : 현정 조's 소스코드 비교
	next step : Controller에서 boardPage 바꿔야함.
	*/
	public List<PostDto> getPostListByBoard(int board_no, Search search){
		search.calcPage(dao.selectPostCntByKeyword(board_no, search.getKeyword()));
		int offset = search.getOffset();
		int cnt = search.getRecordSize();
		String keyword = search.getKeyword();
		return dao.selectPostListByKeyword(board_no, offset, cnt, keyword);
	}

	// 검색기능(페이징과 합침)
	public List<PostDto> getPostListByKeyword(int board_no, Search page) {
		page.calcPage(dao.selectPostCntByKeyword(board_no, page.getKeyword()));
		int offset = page.getOffset();
		int cnt = page.getRecordSize();
		String keyword = page.getKeyword();
		return dao.selectPostListByKeyword(board_no, offset, cnt, keyword);
	}

	public int getTotPostCount(int board_no, String keyword, String searchType, int page) {
		return dao.countPosts(board_no, keyword, searchType, page);
	}

	public PostDto putPost(PostDto dto) {
		dao.insertPost(dto);
		return dao.selectPostByPostNo(dto.getPost_no());
	}
	
	
	public PostDto getPost(int post_no) {
		return dao.selectPostByPostNo(post_no);
	}

	//클릭할 때마다 조회수 업데이트
	public void cntHitCnt(int post_no) {
		dao.updateHitCnt(post_no);
	}
	
	public void delPost(int post_no) {
		dao.deletePost(post_no);
	}
	
	public PostDto editPost(PostDto dto) {
		dao.updatePost(dto);
		return dao.selectPostByPostNo(dto.getPost_no());
	}
	
	public void putReply(ReplyDto reply) {
		dao.insertReply(reply);
	}
	
	public List<ReplyDto> getReply(int post_no){
		return dao.selectReply(post_no);
	}
	
	public List<BoardDto> getBoardMenu(){
		return dao.selectBoardList();
	}
	
	public BoardDto getBoard(int board_no){
		return dao.selectBoard(board_no);
	}
	
	public int getBoardNo(int post_no) {
		return dao.selectBoardNoByPostNo(post_no);
	}
	 //관리자 게시판 수정
	public void editBoard(BoardDto dto) {
		dao.updateBoard(dto);
	}
	//관리자 게시판 삭제
	public void deleteBoard(int board_no) {
		dao.deleteBoard(board_no);
	}
	//관리자 게시판 추가
	public void addBoard(BoardDto dto) { 
		dao.insertBoard(dto);
	}

	@Override
	public void addGalary(GalaryDto dto) {
		dao.insertGalary(dto);
	}

	// 이미지 업로드
	public void imgUpload(ImgmngDto imgDto) {
		dao.insertImg(imgDto);
	}

	//이미지 리스트 조회
	public List<GalaryDto> getGalaryList() {
		return dao.selectGalaryList();
	}

	// 이미지 리스트 조회
	public List<ImgmngDto> downloadImageList(int galary_id) {
		return dao.selectImageByGalaryId(galary_id);
	}

	//이미지 다운로드
	public ImgmngDto downloadImage(int id) {
		return dao.selectImageById(id);
	}

	@Override
	public ImgmngDto getGalaryId(int galary_id) {
		return dao.selectImgGalaryId(galary_id);
	}

	@Override
	public GalaryDto getGalary(int galary_id) {
		return dao.selectGalaryId(galary_id);
	}
}

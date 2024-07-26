package com.myweb.firstboot.dao;

import java.util.List;

import com.myweb.firstboot.dto.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;

@Mapper
public interface BoardDao {
	@Select("select * from post order by post_no desc")
	public List<PostDto> selectPostListAll() throws DataAccessException;

	//오버로딩
	@Select("select * from post where board_no=#{board_no} order by post_no desc limit #{offset}, #{cnt}")
	public List<PostDto> selectPostListByBoardNo(@Param("board_no") int board_no,
												 @Param("offset") int offset, @Param("cnt") int cnt) throws DataAccessException;
	// 해당 게시판에 대한 총 건수
	@Select("select count(*) from post where board_no=#{board_no}")
	public int   selectPostCntByBoardNo(@Param("board_no") int board_no) throws DataAccessException;


	// 검색기능 (concat을 안해주면 문자로 인식해 오류가 난다.)
	@Select("select * from post where board_no=#{board_no} and"
			+ " title like concat('%',#{keyword},'%') or content like concat('%',#{keyword},'%')"
			+ " order by post_no desc limit #{offset}, #{cnt}")
	public List<PostDto> selectPostListByKeyword(@Param("board_no") int board_no,
												 @Param("offset") int offset,
												 @Param("cnt") int cnt,
												 @Param("keyword") String keyword) throws DataAccessException;

	@Select("select count(*) from post where board_no=#{board_no} and"
			+ " title like concat('%',#{keyword},'%') or content like concat('%',#{keyword},'%')")
	public int selectPostCntByKeyword(@Param("board_no") int board_no,
									  @Param("keyword") String keyword) throws DataAccessException;

	@Select("SELECT COUNT(*) FROM post WHERE board_no = #{board_no} "
			+ " AND"
			+ " title LIKE CONCAT('%', #{keyword}, '%')"
			+ " limit #{searchType},#{page}")
	public int countPosts(@Param("board_no") int board_no, @Param("keyword") String keyword,
						  @Param("searchType") String searchType, @Param("page") int page) throws DataAccessException;

	@Insert("insert into post(board_no, title, content, user_id, create_date, update_date, hit_cnt)"
			+"values (#{board_no}, #{title}, #{content}, #{user_id}, now(), now(), 0)")
	@Options(useGeneratedKeys=true, keyProperty="post_no") //자동으로 번호를 생성해주는 데이터를 사용할 때 사용하는 Options
	public void insertPost(PostDto dto) throws DataAccessException;
	
	@Select("select*from post where post_no=#{post_no}")
	public PostDto selectPostByPostNo(@Param("post_no") int post_no) throws DataAccessException;
	
	@Update("update post set hit_cnt = hit_cnt + 1 where post_no=#{post_no}")
	public void updateHitCnt(@Param("post_no") int post_no) throws DataAccessException;
	
	@Delete("delete from post where post_no=#{post_no}")
	public void deletePost(@Param ("post_no") int post_no) throws DataAccessException;
	
	@Update("update post set title=#{title}, content=#{content}, update_date=now() where post_no=#{post_no}")
	public void updatePost(PostDto dto) throws DataAccessException;
	
	@Insert("insert into reply values (null, #{post_no}, #{user_id}, #{comment}, now(), now())")
	public void insertReply(ReplyDto reply) throws DataAccessException;
	
	@Select("select * from reply where post_no=#{post_no} order by reply_no desc")
	public List<ReplyDto> selectReply(@Param ("post_no") int post_no) throws DataAccessException;
	
	
	
	
	@Select("select * from board") //게시글 전체
	public List<BoardDto> selectBoardList() throws DataAccessException;
	@Select("select * from board where board_no=#{board_no}") //게시글 번호
	public BoardDto selectBoard(@Param("board_no") int board_no) throws DataAccessException;
	
	@Select("select board_no from post where post_no=#{post_no}")
	public int selectBoardNoByPostNo(@Param ("post_no") int post_no) throws DataAccessException; 
	
	
	//관리자 게시판 수정
	@Update("update board set board_name=#{board_name}, descript=#{descript} where board_no=#{board_no}")
	public void updateBoard(BoardDto dto) throws DataAccessException;
	//관리자 게시판 삭제
	@Delete("delete from board where board_no=#{board_no}")
	public void deleteBoard(@Param("board_no") int board_no) throws DataAccessException;
	//관리자 게시판 추가
	@Insert("insert into board values(null, #{board_name}, #{descript})")
	public void insertBoard(BoardDto dto) throws DataAccessException;

	// 이미지 리스트 조회
	@Insert("insert into galary values(null,  #{title}, #{userid}, 0)")
	@Options(useGeneratedKeys=true, keyProperty="id")
	public void insertGalary(GalaryDto dto) throws DataAccessException;
	// 이미지 파일 업로드
	@Insert("insert into imgmng values(null,  #{file_name}, #{file_path}, #{org_file_name}, #{galary_id}, #{userid}, #{thumbnail})")
	public void insertImg(ImgmngDto imgDto) throws DataAccessException;
	// 리스트 가져오기
	@Select("select g.id, g.title, g.userid, g.hit_cnt, i.id as img_id\r\n"
			+ "from galary g\r\n"
			+ "join imgmng i on g.id = i.galary_id\r\n"
			+ "where i.thumbnail ='1'")
	public List<GalaryDto> selectGalaryList() throws DataAccessException;

	@Select("select * from imgmng where galary_id=#{galary_id}")
	public List<ImgmngDto> selectImageByGalaryId(@Param("galary_id") int galary_id) throws DataAccessException;

	@Select("select * from imgmng where id=#{id}")
	public ImgmngDto selectImageById(@Param("id") int id) throws DataAccessException;

	@Select("select * from imgmng where galary_id=#{galary_id}")
	public ImgmngDto selectImgGalaryId(int galary_id) throws DataAccessException;

	@Select("select * from galary where id=#{galary_id}")
	public GalaryDto selectGalaryId(int galary_id) throws DataAccessException;
}

package com.myweb.firstboot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;

import com.myweb.firstboot.dto.MemberDto;

@Mapper //sql쿼리문과 해당 메서드를 mapping시켜주는 역할
public interface MemberDao {
	@Select("select count(*) from member where userid=#{userid} and birthdate=#{birthdate} and telnumber=#{telnumber}")
	public boolean checkMember(MemberDto dto) throws DataAccessException;
	
	//public int checkMember(@Param("userid")String userid, @Param("userpwd")String userpwd) throws DataAccessException;

	@Insert("insert into member values(#{userid}, #{userpwd}, #{username}, #{birthdate}, #{gender}, #{telnumber}, #{addr}, 0)")
	public boolean insertMember(MemberDto dto) throws DataAccessException;
	
	@Select("select count(*) from member where userid=#{userid}")
	public int checkId(@Param("userid")String userid) throws DataAccessException;
	
	@Select("select * from member where userid=#{userid}")
	public MemberDto getByUserId(@Param("userid") String userid);
	
	@Update("update member set userpwd=#{userpwd} where userid=#{userid}")
	public void updateUserpwd(@Param("userid") String userid, @Param("userpwd")String userpwd) throws DataAccessException; 
	
	@Update("update member set username=#{username} where userid=#{userid}")
	public void updateUsername(@Param("userid") String userid, @Param("username")String username) throws DataAccessException;
	
	@Update("update member set telnumber=#{telnumber} where userid=#{userid}")
	public void updateTelnumber(@Param("userid") String userid, @Param("telnumber")String telnumber) throws DataAccessException;
	
	@Update("update member set addr=#{addr} where userid=#{userid}")
	public void updateAddr(@Param("userid") String userid, @Param("addr")String addr) throws DataAccessException;

	@Update("update member set permit=#{permit} where userid=#{userid}")
	public void updatePermit(@Param("userid") String userid, @Param("permit")int Permit) throws DataAccessException;
	
	@Delete("delete from member where userid=#{userid}")
	public void deleteUser(@Param("userid") String userid) throws DataAccessException;
	
	@Select("select * from member")
	public List<MemberDto> selectMember() throws DataAccessException;

	@Select("select userid from member where username=#{username} and birthdate=#{birthdate} and usertel=#{usertel}")
	public String selectUserId(MemberDto dto) throws DataAccessException;
}

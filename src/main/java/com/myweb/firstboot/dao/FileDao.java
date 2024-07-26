package com.myweb.firstboot.dao;

import com.myweb.firstboot.dto.FileDto;
import org.apache.ibatis.annotations.*;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface FileDao {
    // 파일 업로드
    @Insert("insert into filemng values(null, #{file_name}, #{file_path}, #{org_file_name}, #{userid}, #{post_no})")
    public void insertFile(FileDto file) throws DataAccessException;

    // 파일 리스트 조회
    @Select("select * from filemng where post_no=#{post_no}")
    public List<FileDto> selectFileByPostNo(@Param("post_no") int psot_no) throws DataAccessException;

    // 파일 건당 조회
    @Select("select * from filemng where id=#{id}")
    public FileDto selectFileById(@Param("id") int id) throws DataAccessException;

    // 파일 삭제
    @Delete("delete from filemng where id=#{id}")
    public void deleteFileById(@Param("id") int id) throws DataAccessException;
}

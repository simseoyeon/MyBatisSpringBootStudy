package com.myweb.firstboot.service;

import com.myweb.firstboot.dao.FileDao;
import com.myweb.firstboot.dto.FileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileDao fileDao;

    // 파일 업로드
    public void fileUpload(FileDto dto) {
        fileDao.insertFile(dto);
    }

    // 파일 리스트 조회
    public List<FileDto> fileDownloadList(int post_no) {
        return fileDao.selectFileByPostNo(post_no);
    }

    // 파일 건당 조회
    public FileDto fileDownload(int id) {
        return fileDao.selectFileById(id);
    }

}

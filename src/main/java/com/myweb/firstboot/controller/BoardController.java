package com.myweb.firstboot.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import com.myweb.firstboot.com.Search;
import com.myweb.firstboot.dto.*;
import com.myweb.firstboot.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.myweb.firstboot.service.BoardService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
public class BoardController { //게시판 컨트롤러
	private final BoardService service;
	private final FileService fileService;

	@GetMapping("/")
	public String indexPage(Model model) {
		List<BoardDto> menu = service.getBoardMenu();
		model.addAttribute("menu", menu);
		return "index";
	}

	//게시판 페이지 및 페이징
	@GetMapping("/board/{board_no}")
	public String boardPage(Model model, @PathVariable("board_no") int board_no,
							@RequestParam(value = "keyword", defaultValue = "") String keyword) {
		Search search = new Search(5,5);
		//검색 기능 추가(키워드 파라메타가 있으면 키워드 설정)
		search.setKeyword(keyword);
		//게시판 리스트
		List<PostDto> list = service.getPostListByBoard(board_no, search);
		model.addAttribute("list", list);
		//페이징에 대한 것도 같이 가야함
		model.addAttribute("page", search);
		//해당 게시글에 대한 번호(정보)
		BoardDto board = service.getBoard(board_no);
		model.addAttribute("board", board);
		
		return "board";
	}
	/*== @RequestParam 추가해야함, recordSize, searchType도 추가 필요
	     //게시판 리스트 부분의 getPostListByBoard도
	       getTotPostCount로 바뀜
	       service에서 getTotPostCount도 찾아서 바꿔야 함
	       -> getTotPostCount의 메서드도
	       dao 가서 추가 작업 필요
	       ㅅㄱ ㅋㅋ                                               ==*/
	@GetMapping("/board/{board_no}/{page}")
	public String boardPage(Model model, @PathVariable("board_no") int board_no,
							@PathVariable("page") int page,
							@RequestParam(value = "keyword", defaultValue = "")String keyword,
							@RequestParam(value = "recordSize", defaultValue = "") int recordSize,
							@RequestParam(value = "searchType", required = false) String searchType) {
		Search search = new Search(page, recordSize);
		//검색기능 추가(키워드 파라메타가 있으면 키워드 설정)
		search.setKeyword(keyword);
		search.setPage(page);
		search.getOffset();
		//게시판 리스트
		List<PostDto> list = service.getPostListByBoard(board_no, search);
		int totPostCount = service.getTotPostCount(board_no, keyword, searchType, page);
		model.addAttribute("list", list);
		// 페이징에 대한 것도 같이 가야함
		model.addAttribute("page",search);
		//해당 게시글에 대한 번호(정보)
		BoardDto board = service.getBoard(board_no);
		model.addAttribute("board", board);

		return "board";
	}

	//검색 기능(board 기능과 합침)
	@GetMapping("/search/{board_no}")
	public String searchPost(@RequestParam("keyword") String keyword, Model model,
							 @PathVariable("board_no") int board_no) {
		Search page = new Search(5, 5);
		page.setKeyword(keyword);
		// 게시판 리스트
		List<PostDto> list = service.getPostListByKeyword(board_no, page);
		model.addAttribute("list", list);
		// 페이징에 대한 것도 같이 가야함
		model.addAttribute("page", page);
		//해당 게시글에 대한 번호(정보)
		BoardDto board = service.getBoard(board_no);
		model.addAttribute("board", board);

		return "board";
	}


	//글쓰기 페이지
	@GetMapping("/write/{board_no}/{page}")
	public String writeForm(Model model, @PathVariable("board_no") int board_no) {
		//게시판 정보
		BoardDto board = service.getBoard(board_no);
		model.addAttribute("board", board);

		return "writeForm";
	}
	@PostMapping("/write")
	public String writePost(Model model, PostDto dto,
							@RequestParam(value="file", required = false) MultipartFile[] files) { //파일 가져오는 클래스 적용해서 첨부파일 기능 적용 // 여러 파일 적용하기 위해 배열로 변경
		final String path = "D:\\koreait\\JAVA-work\\firstboot\\repository\\";

		//게시글 저장
		dto = service.putPost(dto);
		model.addAttribute("post", dto);
		
		//게시판 정보
		BoardDto board = service.getBoard(dto.getBoard_no());
		model.addAttribute("board", board);

		// 파일 업로드 처리 (!file.isEmpty() 에러 발생해서 변겅)
		if (files != null) {
			for (MultipartFile file : files) { // 파일 개수만큼 돌려서 file로 넣어줌
				try {
					FileDto fileDto = new FileDto();
					String org_file_name = file.getOriginalFilename();
					String file_name = UUID.randomUUID().toString().substring(0, 8) + "_" + org_file_name;
					fileDto.setFile_name(file_name);
					fileDto.setFile_path(path);
					fileDto.setOrg_file_name(org_file_name);
					fileDto.setUserid(dto.getUser_id());
					fileDto.setPost_no(dto.getPost_no());

					// 실제 파일 저장위치에 파일 업로드
					file.transferTo(new File(path + file_name)); // 오브젝트로 넘기기
					fileService.fileUpload(fileDto);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			List<FileDto> file_list = fileService.fileDownloadList(dto.getPost_no());
			model.addAttribute("file_list", file_list);
		}
		return "viewForm";
	}

	//각 게시글마다 내용 보여주기
	@GetMapping("/view/{post_no}")
	public String viewPost(@PathVariable("post_no") int post_no, Model model) {
		PostDto dto = service.getPost(post_no);
		//조회수 늘리기
		service.cntHitCnt(post_no);
		model.addAttribute("post", dto);
		
		//게시판 정보
		BoardDto board = service.getBoard(dto.getBoard_no());
		model.addAttribute("board", board);
		
		//댓글
		List<ReplyDto> reply_list = service.getReply(dto.getPost_no());
		model.addAttribute("reply_list", reply_list);
		
		
		return"viewForm";
	}
	
	//게시글 삭제
	@GetMapping("/delete/{post_no}")
	public String delPost(@PathVariable("post_no") int post_no) {
		int board_no = service.getBoardNo(post_no);
		service.delPost(post_no);
		return "redirect:/board/"+ board_no;
	}
	
	//게시글 수정
	@GetMapping("/edit/{post_no}")
	public String editForm(@PathVariable("post_no") int post_no, Model model) {
		 //게시글 내용 가져오기
		PostDto dto = service.getPost(post_no);
		model.addAttribute("post", dto);
		
		//게시판 정보
		BoardDto board = service.getBoard(dto.getBoard_no());
		model.addAttribute("board", board);
		
		return "editForm";
	}
	@PostMapping("/edit")
	public String editPost(Model model, PostDto dto) {
		dto = service.editPost(dto);
		model.addAttribute("post", dto);
		
		//게시판 정보
		BoardDto board = service.getBoard(dto.getBoard_no());
		model.addAttribute("board", board);
		
		//댓글 전체 불러오기
		List<ReplyDto> reply_list = service.getReply(dto.getPost_no());
		model.addAttribute("reply_list", reply_list);
		return "viewForm";
	}
	
	//댓글 작성
	@PostMapping("/reply")
	public String setreply(ReplyDto reply, Model model) {
		//댓글 등록
		service.putReply(reply);
		List<ReplyDto> reply_list = service.getReply(reply.getPost_no()); //해당 게시물에 있는 모든 댓글 get
		model.addAttribute("reply_list", reply_list);
		//게시글 취득
		PostDto dto = service.getPost(reply.getPost_no());
		model.addAttribute("post", dto);
		
		//게시판 정보
		BoardDto board = service.getBoard(dto.getBoard_no());
		model.addAttribute("board", board);
		
		return "viewForm";
	}
	//관리자의 게시판 관리
	@GetMapping("/manageBoard")
	public String mngBoard(Model model) {
		return "mngBoard";
	}
	//수정
	@PostMapping("/editBoard")
	public String editMngBoard(BoardDto dto) {
		service.editBoard(dto);
		return "redirect:/manageBoard";
	}
	//삭제
	@GetMapping("/deleteBoard/{board_no}")
	public String deleteMngBoard(@PathVariable("board_no") int board_no) {
		service.deleteBoard(board_no);
		return "redirect:/manageBoard";
	}
	//추가
	@PostMapping("/addBoard")
	public String addMngBoard(BoardDto dto) {
		service.addBoard(dto);
		return "redirect:/manageBoard";
	}

	//파일 링크 선택시 다운로드
	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("id") int id){
		try {
			FileDto dto = fileService.fileDownload(id);
			// 파일이 자장된 위치와 UUID로 저장한 파일 아이디
			Path filePath = Paths.get(dto.getFile_path()).resolve(dto.getFile_name()).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if(resource.exists()) {
				String encodeFileName = URLEncoder.encode(dto.getOrg_file_name(), StandardCharsets.UTF_8.toString());  // 텍스트 파일은 오류나서 추가
				String contentDisposition = "attachment; filename=\"" + encodeFileName + "\"";  // 텍스트 파일은 오류나서 추가
				// 이부분은 공식으로 생각하고 외우자
				// http 형식을 맞춰서 다운로드 할때 이름 설정..?
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
						.header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
						.body(resource);
			}else {
				// 파일을 찾지 못했을 경우
				return ResponseEntity.notFound().build();
			}
		} catch(IOException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}
	// 갤러리 페이지
	@GetMapping("/galary")
	public String getGalaryList(Model model) {
		List<GalaryDto> list =  service.getGalaryList();
		model.addAttribute("list",list);
		return "galaryList";
	}

	// 갤러리 다운로드
	@GetMapping("/downloadImg/{id}")
	public ResponseEntity<Resource> downloadImg(@PathVariable("id") int id){
		try {
			ImgmngDto dto = service.downloadImage(id);
			// 파일이 자장된 위치와 UUID로 저장한 파일 아이디
			Path filePath = Paths.get(dto.getFile_path()).resolve(dto.getFile_name()).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if(resource.exists()) {
				String encodeFileName = URLEncoder.encode(dto.getOrg_file_name(), StandardCharsets.UTF_8.toString());  // 텍스트 파일은 오류나서 추가
				String contentDisposition = "attachment; filename=\"" + encodeFileName + "\"";  // 텍스트 파일은 오류나서 추가
				// 이부분은 공식으로 생각하고 외우자
				// http 형식을 맞춰서 다운로드 할때 이름 설정..?
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
						.header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
						.body(resource);
			}else {
				// 파일을 찾지 못했을 경우
				return ResponseEntity.notFound().build();
			}
		} catch(IOException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}

	// 갤러리 페이지
	@GetMapping("/addGalary")
	public String addGalary() {
		return "galaryForm";
	}

	// 갤러리 업로드
	@PostMapping("/addGalary")
	public String putGalary(Model model, GalaryDto dto,
							@RequestParam(value = "file", required = false) MultipartFile[] files,
							@RequestParam(value = "thumbnail", required = false) String[] thumbnail) {
		final String path = "D:\\koreait\\JAVA-work\\firstboot\\repository\\";
		service.addGalary(dto);

		// 파일 업로드 처리 (!file.isEmpty() 에러 발생해서 변겅)
		if (files != null) {
			for (int i=0; i<files.length; i++) {
				try {
					MultipartFile file = files[i];
//					System.out.println("thumbnail[" + i + "] = " + thumbnail[i]);  //디버깅
					ImgmngDto imgDto = new ImgmngDto();
					String org_file_name = file.getOriginalFilename();
					String file_name = UUID.randomUUID().toString().substring(0, 8) + "_" + org_file_name;
					imgDto.setFile_name(file_name);
					imgDto.setFile_path(path);
					imgDto.setOrg_file_name(org_file_name);
					imgDto.setUserid(dto.getUserid());
					imgDto.setGalary_id(dto.getId());
					imgDto.setThumbnail(thumbnail[i]);

					// 실제 파일 저장위치에 파일 업로드
					file.transferTo(new File(path + file_name)); // 오브젝트로 넘기기
					service.imgUpload(imgDto);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			List<GalaryDto> list = service.getGalaryList();
			model.addAttribute("list", list);
		}

		return "galaryList";
	}

	// 각 이미지마다 내용 보여주기
	@GetMapping("/imgview/{galary_id}")
	public String imgviewPost(@PathVariable("galary_id") int galary_id, Model model) {
		ImgmngDto dto = service.getGalaryId(galary_id);

		// 메뉴
//		List<GalaryDto> menu = service.getGalaryMenu();
//		model.addAttribute("menu", menu);

		// 게시판 정보
		GalaryDto galary = service.getGalary(dto.getGalary_id());
		model.addAttribute("galary", galary);

		// 파일 취득
//		List<ImgmngDto> img_list = service.downloadImageList(galary_id);
//		model.addAttribute("img_list", img_list);

		return "imgviewForm";
	}


}

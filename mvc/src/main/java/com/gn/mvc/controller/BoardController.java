package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	
	// 1. 필드 주입 -> 순환 참조
	// new 연산자 쓴 것과 다름 없음
//	@Autowired
//	BoardService service;
	
	// 2. 메소드(Setter) 주입 -> 불변성 보장X
	// 만들면 우리가 쓰는 것이 아닌 Spring이 의존성 주입할 때 씀
//	private BoardService service;
//	
//	@Autowired
//	public void setBoardSercie(BoardService service) {
//		this.service = service;
//	}
	
	// 3. 생성자 주입 + final
	private final BoardService service;
	
	// lombok의 @RequiredArgsConstructor이 대신 해줌
//	@Autowired //노란색 줄 신경 안 써도 됨.
//	public BoardController(BoardService service) {
//		this.service = service;
//	}

	@GetMapping("/board/create")
	public String createBoardView() {
		return "board/create";
	}
	
	@PostMapping("/board")
	@ResponseBody
	// ResponseBody와 Map이 한 세트여야지 json으로 반환 가능
	public Map<String,String> createBoardApi(
			// 3개가 같은 방식 개별 param, map param, dto
//			@RequestParam("board_title") String boardTitle,
//			@RequestParam("board_content") String boardContent
			
//			@RequestParam Map<String,String> param
			BoardDto dto
			) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "게시글 등록중 오류가 발생하였습니다.");
		System.out.println(dto);
		// Service가 가지고 있는 createBoard 메소드 호출
		service.createBoard(dto);
		
		return resultMap;
	}
}

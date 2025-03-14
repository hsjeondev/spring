package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	
	private Logger logger = LoggerFactory.getLogger(BoardController.class);	
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
		
		// Service가 가지고 있는 createBoard 메소드 호출
		BoardDto result = service.createBoard(dto);
		
		logger.debug("1 : " + result.toString());
		logger.info("2 : " + result.toString());
		logger.warn("3 : " + result.toString());
		logger.error("4 : " + result.toString());
		
		return resultMap;
	}
	
	@GetMapping("/board")
	public String selectBoardAll(Model model, SearchDto searchDto) {
		// 1. DB에서 목록 SELECT
		List<Board> resultList = service.selectBoardAll(searchDto);
		// 2. 목록을 Model에 등록
		model.addAttribute("boardList", resultList);
		model.addAttribute("searchDto", searchDto);
		// 3. list.html에 데이터 셋팅
		return "board/list";
	}
}

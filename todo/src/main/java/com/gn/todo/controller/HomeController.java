package com.gn.todo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.service.HomeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final HomeService homeService;

	@GetMapping({"","/"})
	public String HomeView(Model model, SearchDto searchDto, PageDto pageDto) {
		
		if(pageDto.getNowPage() == 0) pageDto.setNowPage(1);
		
		Page<TodoDto> todos = homeService.selectTodoAll(searchDto, pageDto);
		
		pageDto.setTotalPage(todos.getTotalPages());
		
		model.addAttribute("todos",todos.getContent());
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("pageDto",pageDto);
		
		return "home";
	}
	
	@PostMapping("/")
	@ResponseBody
	public Map<String, String> createTodoApi(Model model,TodoDto todoDto) {
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "할 일 추가 중 오류가 발생하였습니다. 다시 시도해주세요.");
		
		TodoDto result = homeService.createTodo(todoDto);
		
		if(result != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "할 일이 정상적으로 추가 되었습니다.");
		}
		
		return resultMap;
	}
	
	@PutMapping("/{id}")
	@ResponseBody
	public Map<String, String> updateFlagApi(@PathVariable("id") Long id) {
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "정상적으로 반영 되지 않았습니다. 다시 시도해주세요.");
		
		int result = homeService.updateTodoFlagOne(id);
		
		if(result > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "");
		}
		
		return resultMap;
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public Map<String, String> deleteTodoApi(@PathVariable("id") Long id) {
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "삭제 중 오류가 발생하였습니다.");
		
		int result = homeService.deleteTodoOne(id);
		
		if(result > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "정상적으로 삭제 되었습니다.");
		}
		
		return resultMap;
	}
	
}

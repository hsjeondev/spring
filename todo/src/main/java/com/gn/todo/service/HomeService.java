package com.gn.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.repository.HomeRepository;
import com.gn.todo.specification.TodoSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {
	
	private final HomeRepository homeRepository;

	public Page<Todo> selectTodoAll(SearchDto searchDto, PageDto pageDto) {
		
		Pageable pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage());
		
		Specification<Todo> spec = (root, query, criteriaBuilder) -> null;
		if(searchDto.getSearch_text() == null) {
			searchDto.setSearch_text("");
		}
		spec = spec.and(TodoSpecification.TodoCotentContains(searchDto.getSearch_text()));
		
		Page<Todo> todos = homeRepository.findAll(spec, pageable);
		
		return todos;
	}
	
	public Todo createTodo(Todo todo) {
		todo.setFlag("N");
		return homeRepository.save(todo);
	}
	
	public int updateTodoFlagOne(Long id) {
		int result = 0;
		
		try {
			Todo todo = homeRepository.findById(id).orElse(null);
			
			if(todo.getFlag().equals("N")) {
				todo.setFlag("Y");
			} else {
				todo.setFlag("N");
			}
			
			homeRepository.save(todo);
			
			result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int deleteTodoOne(Long id) {
		int result = 0;
		
		try {
			Todo todo = homeRepository.findById(id).orElse(null);
			
			if(todo != null) {
				homeRepository.delete(todo);
			}
			
			result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}

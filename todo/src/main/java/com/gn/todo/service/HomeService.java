package com.gn.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.repository.HomeRepository;
import com.gn.todo.specification.TodoSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {
	
	private final HomeRepository homeRepository;

	public Page<TodoDto> selectTodoAll(SearchDto searchDto, PageDto pageDto) {
		
		Pageable pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage());
		
		Specification<Todo> spec = (root, query, criteriaBuilder) -> null;
		if(searchDto.getSearch_text() != null) {
			spec = spec.and(TodoSpecification.TodoCotentContains(searchDto.getSearch_text()));			
		}
		
		Page<Todo> todoEntities = homeRepository.findAll(spec, pageable);
		Page<TodoDto> todoDtos = todoEntities.map(todo -> {
		    TodoDto todoDto = new TodoDto();
		    return todoDto.toDto(todo);
		});
		
		return todoDtos;
	}
	
	public TodoDto createTodo(TodoDto todoDto) {
		Todo todo = homeRepository.save(todoDto.toEntity());
		return new TodoDto().toDto(todo);
	}
	
	public int updateTodoFlagOne(Long id) {
		int result = 0;
		
		try {
			Todo todoEntity = homeRepository.findById(id).orElse(null);
			TodoDto todoDto = new TodoDto().toDto(todoEntity);
			
			if(todoEntity != null) {
				if("N".equals(todoDto.getFlag())) {
					todoDto.setFlag("Y");
				} else {
					todoDto.setFlag("N");
				}
			}
			
			todoEntity = todoDto.toEntity();
			
			homeRepository.save(todoEntity);
			
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

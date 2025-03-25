package com.gn.todo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gn.todo.entity.Todo;

public interface HomeRepository extends JpaRepository<Todo, Long> {

	Page<Todo> findAll(Specification<Todo> spec, Pageable pageable);
}

package com.gn.mvc.specification;

import org.springframework.data.jpa.domain.Specification;

import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;

public class AttachSpecification {

	public static Specification<Attach> boardEquals(Board board) {
		return (root, query, criteriaBuilder) ->
			//root.get("board")는 Attach에 있는 필드
			criteriaBuilder.equal(root.get("board"), board);
	}
}

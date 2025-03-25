package com.gn.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PageDto {

	private int numPerPage = 5;
	private int nowPage;
	
	private int pageBarSize = 5;
	private int pageBarStart;
	private int pageBarEnd;
	
	private boolean prev = true;
	private boolean next = true;
	
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}
	
	private int totalPage;
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
		calcPaging();
	}
	
	private void calcPaging() {
		pageBarStart = ((nowPage-1)/pageBarSize)*pageBarSize + 1;

		pageBarEnd = pageBarStart + pageBarSize -1;
		if(pageBarEnd > totalPage) pageBarEnd = totalPage;

		if(pageBarStart == 1) prev = false;
		if(pageBarEnd >= totalPage) next = false;
	}
	
}

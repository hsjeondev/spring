package com.gn.todo.dto;

import com.gn.todo.entity.Attach;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AttachDto {
	private Long attach_no;
	private String ori_name;
	private String new_name;
	private String attach_path;
	
	public AttachDto toDto(Attach attach) {
		return AttachDto.builder()
				.attach_no(attach.getAttachNo())
				.ori_name(attach.getOriName())
				.new_name(attach.getNewName())
				.attach_path(attach.getAttachPath())
				.build();
	}
	
	public Attach toEntity() {
		return Attach.builder()
				.attachNo(attach_no)
				.oriName(ori_name)
				.newName(new_name)
				.attachPath(attach_path)
				.build();
	}
}

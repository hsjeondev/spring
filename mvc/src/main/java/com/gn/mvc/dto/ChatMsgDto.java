package com.gn.mvc.dto;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ChatMsgDto {
	private Long room_no;
	private Long sender_no;
	private Long receiver_no;
	private String msg_content;
}

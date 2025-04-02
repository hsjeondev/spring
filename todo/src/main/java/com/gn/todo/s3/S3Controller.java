package com.gn.todo.s3;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;
import com.gn.todo.dto.AttachDto;
import com.gn.todo.entity.Attach;
import com.gn.todo.service.AttachService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {
	
	private final S3Service s3Service;
	private final AttachService attachService;
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Object> downloadFile(@PathVariable("id") Long id) {
		try {
			// 1. 파일 정보 조회
			Attach fileData = attachService.selectAttachOne(id);
			if(fileData == null) {
				return ResponseEntity.notFound().build();
			}
			
			// 2. S3 서비스와 연결하기
			S3Object s3Object = s3Service.getS3Object(fileData.getNewName());
			// 3. S3 서비스에서 컨텐츠 정보 가져오기
			InputStream inputStream = s3Object.getObjectContent();
			// 4. 파일 데이터를 바이트 배열로 변환
			byte[] fileBytes = inputStream.readAllBytes();
			// 5. 기존 파일명칭 셋팅
			String oriFileName = fileData.getOriName();
			String encodedName = URLEncoder.encode(oriFileName, StandardCharsets.UTF_8);
			
			// 6. 브라우저한테 보내주기
			HttpHeaders headers = new HttpHeaders();
			// 파일확장자
			headers.setContentType(MediaType.parseMediaType(s3Object.getObjectMetadata().getContentType()));
			// headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+encodedName);랑 같은 애
			headers.setContentDispositionFormData("attachment", encodedName);
			headers.setContentLength(fileBytes.length);
			
			// 7. ResponseEntity에 파일 데이터 반환
			return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("/upload")
	@ResponseBody
	public Map<String, String> uploadFile(@RequestParam("files") List<MultipartFile> files) {
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "파일 업로드중 오류가 발생하였습니다.");
		
		try {
			
			for(MultipartFile mf : files) {
				// 1. 업로드
				AttachDto dto = s3Service.uploadFile(mf);
				// 2. 메타 데이터 insert
				if(dto != null) attachService.createAttach(dto);
				
			}
			
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "파일이 업로드되었습니다.");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}
}

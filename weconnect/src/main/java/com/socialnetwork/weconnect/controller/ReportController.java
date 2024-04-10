package com.socialnetwork.weconnect.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/reports/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {

//	@GetMapping("/user/export/excel")
//	public ApiResponse<String> exportToExcel(HttpServletResponse response) {
//	
//		return ApiResponse.<String>builder()
//				.result()
//				.build();
//	}
}

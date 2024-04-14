package com.socialnetwork.weconnect.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.socialnetwork.weconnect.Service.ReportService;
import com.socialnetwork.weconnect.ServiceImpl.ReportServiceImpl;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import com.socialnetwork.weconnect.dto.response.ReportResponse;
import com.socialnetwork.weconnect.repository.PostRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/reports/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
	ReportService reportService;

	@GetMapping("/export-to-excel")
	public void exportIntoExcelFile(HttpServletResponse response) throws IOException {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		SimpleDateFormat weekFormat = new SimpleDateFormat("'Week'_w");
		String weekNumber = weekFormat.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=Statistical_" + weekNumber + ".xlsx";
		response.setHeader(headerKey, headerValue);
		reportService.getReportThisWeek(response);
	}
}

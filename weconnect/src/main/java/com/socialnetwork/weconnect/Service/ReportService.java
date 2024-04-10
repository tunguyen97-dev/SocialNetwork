package com.socialnetwork.weconnect.Service;

import org.springframework.stereotype.Service;

import com.socialnetwork.weconnect.dto.response.ReportResponse;

@Service
public interface ReportService {
	ReportResponse getReportThisWeek();
}

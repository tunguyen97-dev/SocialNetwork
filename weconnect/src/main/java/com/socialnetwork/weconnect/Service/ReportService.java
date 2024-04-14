package com.socialnetwork.weconnect.Service;

import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletResponse;

@Service
public interface ReportService {
	void getReportThisWeek(HttpServletResponse response) throws IOException;

	void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style);

	void writeHeader();

	void write();
}
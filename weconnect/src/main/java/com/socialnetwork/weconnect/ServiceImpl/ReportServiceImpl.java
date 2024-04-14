package com.socialnetwork.weconnect.ServiceImpl;

import java.io.IOException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.socialnetwork.weconnect.Service.ReportService;
import com.socialnetwork.weconnect.dto.response.ReportResponse;
import com.socialnetwork.weconnect.entity.User;
import com.socialnetwork.weconnect.repository.PostRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportServiceImpl implements ReportService {

	final PostRepository postRepository;

	XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet sheet;

	@Override
	public void writeHeader() {
		sheet = workbook.createSheet("Statistical");
		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);
		setBorder(style);
		int columnCount = 0;
		createCell(row, columnCount++, "No", style);
		createCell(row, columnCount++, "Total Post", style);
		createCell(row, columnCount++, "Total Like", style);
		createCell(row, columnCount++, "Total Comment", style);
		createCell(row, columnCount++, "Total New Friend", style);
	}

	@Override
	public void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (valueOfCell instanceof Integer) {
			cell.setCellValue((Integer) valueOfCell);
		} else if (valueOfCell instanceof Long) {
			cell.setCellValue((Long) valueOfCell);
		} else if (valueOfCell instanceof String) {
			cell.setCellValue((String) valueOfCell);
		} else {
			cell.setCellValue((Boolean) valueOfCell);
		}
		cell.setCellStyle(style);
	}

	@Override
	public void write() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int rowCount = 1;
		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
		ReportResponse reportResponse = postRepository.findStatistical(user.getId());
		if (reportResponse != null) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			createCell(row, columnCount++, columnCount, style);
			createCell(row, columnCount++, reportResponse.getPostCount(), style);
			createCell(row, columnCount++, reportResponse.getLikeCount(), style);
			createCell(row, columnCount++, reportResponse.getCommentCount(), style);
			createCell(row, columnCount++, reportResponse.getNewFriendCount(), style);
		}
	}

	@Override
	public void getReportThisWeek(HttpServletResponse response) throws IOException {
		writeHeader();
		write();
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	public void setBorder(CellStyle style) {
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
	}
}
package com.socialnetwork.weconnect.controller;

import com.socialnetwork.weconnect.Service.FilesStorageService;
import com.socialnetwork.weconnect.dto.request.FileInfo;
import com.socialnetwork.weconnect.dto.response.ApiResponse;
import lombok.AllArgsConstructor;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/file")
public class FilesController {
	private final FilesStorageService storageService;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiResponse<List<String>> uploadFiles(@RequestPart("content") String content,
			@RequestPart("files") MultipartFile[] files, Principal connectedUser) {
		List<String> fileNames = new ArrayList<>();
		Arrays.asList(files).stream().forEach(file -> {
			storageService.save(file, connectedUser);
			fileNames.add(file.getOriginalFilename());
		});
		return ApiResponse.<List<String>>builder().message("Uploaded the files successfully: ").result(fileNames)
				.build();
	}

	@GetMapping("/files")
	public ApiResponse<List<FileInfo>> getListImageByUser(Principal connectedUser) {
		List<FileInfo> fileInfos = storageService.loadAllByUserName(connectedUser).map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(FilesController.class, "getFile", filename, connectedUser).build().toString();
			return new FileInfo(filename, url);
		}).collect(Collectors.toList());
		return ApiResponse.<List<FileInfo>>builder().result(fileInfos).build();
	}

	@GetMapping("/files/{filename:.+}")
	public ApiResponse<String> getFile(@PathVariable String filename, Principal connectedUser) {
		return ApiResponse.<String>builder().result(storageService.load(filename, connectedUser)).build();
	}
}

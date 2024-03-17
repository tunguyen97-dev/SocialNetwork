package com.socialnetwork.weconnect.ServiceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.socialnetwork.weconnect.Service.FilesStorageService;
import com.socialnetwork.weconnect.exception.AppException;
import com.socialnetwork.weconnect.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilesStorageServiceImpl implements FilesStorageService {

	Path root = Paths.get("C:\\upload");

	@Override
	public void init() {
		try {
			Files.createDirectory(root);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	@Override
	public void save(MultipartFile file) {
		try {
			// Tạo filename độc quyền
			String newFileName = getUniqueFilename(file.getOriginalFilename());
			// Xử lý lưu vào server img
			Files.copy(file.getInputStream(), this.root.resolve(newFileName));

			// Xử lý lưu vào db

		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	@Override
	public String load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource.toString();
			} else {
				throw new AppException(ErrorCode.IMAGE_NOT_EXISTED);
			}
		} catch (MalformedURLException e) {
			throw new AppException(ErrorCode.URL_INVALID);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

	@Override
	public String getUniqueFilename(String filename) {
		// Định dạng tên file mới, có thể sử dụng timestamp hoặc số ngẫu nhiên để đảm
		// bảo duy nhất
		return FilenameUtils.getBaseName(filename) + "." + FilenameUtils.getExtension(filename);
	}
}

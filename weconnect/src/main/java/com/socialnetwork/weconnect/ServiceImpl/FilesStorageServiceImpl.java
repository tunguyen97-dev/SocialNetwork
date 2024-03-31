package com.socialnetwork.weconnect.ServiceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.socialnetwork.weconnect.Service.FilesStorageService;
import com.socialnetwork.weconnect.entity.User;
import com.socialnetwork.weconnect.exception.AppException;
import com.socialnetwork.weconnect.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilesStorageServiceImpl implements FilesStorageService {

	Path root = Paths.get("upload");

	@Override
	public void init() {
		try {
			Files.createDirectory(root);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	@Override
	public String saveTosServer(MultipartFile file, String userName) {

		Path userDirectory = this.root.resolve(userName);
		try {
			if (!Files.exists(userDirectory)) {
				Files.createDirectory(userDirectory);
			}

			// neu ton tai roi thi tra ve luon path
//			if (checkExistFileName(file.getOriginalFilename(), userDirectory)) {
//				return userDirectory.resolve(file.getOriginalFilename()).toString();
//			}

			// Tạo filename độc quyền
			String newFileName = getUniqueFilename(file.getOriginalFilename());
			// Xử lý lưu vào server img
			Files.copy(file.getInputStream(), userDirectory.resolve(newFileName));
			return userDirectory.resolve(newFileName).toString();

		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	@Override
	public boolean delTosServer(String fileName, String userName) {
		Path path = Paths.get(fileName);
        String fileNameCut = path.getFileName().toString();
		Path filePath = this.root.resolve(userName).resolve(fileNameCut);

		try {
			// Kiểm tra xem tệp tin tồn tại trước khi xóa
			if (Files.exists(filePath)) {
				Files.delete(filePath);
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			throw new AppException(ErrorCode.URL_INVALID);
		}
	}

	@Override
	public String load(String filename) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			Path userDirectory = this.root.resolve(user.getName()).resolve(filename);
			Resource resource = new UrlResource(userDirectory.toUri());

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
	public Stream<Path> loadAllByUserName() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Path userDirectory = this.root.resolve(user.getName());
		try {
			if (!Files.exists(userDirectory)) {
				Files.createDirectory(userDirectory);
			}
			return Files.walk(userDirectory, 1).filter(path -> !path.equals(userDirectory))
					.map(userDirectory::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

	@Override
	public String getUniqueFilename(String filename) {
		// Định dạng tên file mới, có thể sử dụng timestamp hoặc số ngẫu nhiên để đảm
		// bảo duy nhất
		return FilenameUtils.getBaseName(filename) + "_" + UUID.randomUUID().toString().substring(0, 10) + "."
				+ FilenameUtils.getExtension(filename);
	}

	@Override
	public Path getRoot() {
		return root;
	}

	@Override
	public boolean checkExistFileName(String fileName, Path userDirectory) {
		Path path = Paths.get(fileName);
        String fileNameCut = path.getFileName().toString();
		Path filePath = userDirectory.resolve(fileNameCut);
		
		if (Files.exists(filePath)) {
			return true;
		}
		return false;
	}

	@Override
	public String saveToFileServer(String filePath, String userName) {
		Path userDirectory = this.root.resolve(userName);
		try {
			if (!Files.exists(userDirectory)) {
				Files.createDirectory(userDirectory);
			}

			// Kiểm tra xem tệp tin đã tồn tại chưa
			if (checkExistFileName(filePath, userDirectory)) {
				return userDirectory.resolve(filePath).toString();
			}

			// Tạo filename độc quyền
			String originalFileName = Paths.get(filePath).getFileName().toString();
			String newFileName = getUniqueFilename(originalFileName);

			// Xử lý lưu vào server img
			Path destinationPath = userDirectory.resolve(newFileName);
			Files.copy(Paths.get(filePath), destinationPath);

			return destinationPath.toString();
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}
}

package com.socialnetwork.weconnect.Service;

import java.nio.file.Path;
import java.security.Principal;
import java.util.stream.Stream;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
	public void init();

	public String saveTosServer(MultipartFile file, String userName);

	public boolean delTosServer(String file, String userName);

	public String load(String filename);

	public void deleteAll();

	public Stream<Path> loadAllByUserName();

	public String getUniqueFilename(String fileName);

	public Path getRoot();

	public boolean checkExistFileName(String fileName, Path userDirectory);

	public String saveToFileServer(String filePath, String userName);
}

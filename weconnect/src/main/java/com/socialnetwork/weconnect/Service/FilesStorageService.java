package com.socialnetwork.weconnect.Service;

import java.nio.file.Path;
import java.security.Principal;
import java.util.stream.Stream;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
	public void init();

	public void save(MultipartFile file, Principal connectedUser);

	public String load(String filename, Principal connectedUser);

	public void deleteAll();

	public Stream<Path> loadAllByUserName(Principal connectedUser);

	public String getUniqueFilename(String filename);
}

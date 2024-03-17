package com.socialnetwork.weconnect.Service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
	  public void init();

	  public void save(MultipartFile file);

	  public String load(String filename);

	  public void deleteAll();

	  public Stream<Path> loadAll();
	  
	  public String getUniqueFilename(String filename);
}

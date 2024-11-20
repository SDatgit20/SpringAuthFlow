package com.example.demo.file;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class fileStorageService {
	private Path fileStorageLocation;
	@Autowired
	public fileStorageService() throws Exception {
	     this.fileStorageLocation=Paths.get("C:\\Users\\SRASHTI\\Java\\jpademo\\login\\target");
	     this.fileStorageLocation=this.fileStorageLocation.toAbsolutePath();
	     try {
	    	 Files.createDirectories(this.fileStorageLocation);
	     }
	     catch(Exception e) {
	    	 throw new Exception("Unable to create Directory");
	     }
	}
	public String storeFile(MultipartFile file){
		String filename=StringUtils.cleanPath(file.getOriginalFilename());
		try {
		if(filename.contains(".."))
            System.out.println("Sorry! Filename contains invalid path sequence " + filename);
		Path target=this.fileStorageLocation.resolve(filename);
		Files.copy(file.getInputStream(),target,StandardCopyOption.REPLACE_EXISTING);
		return filename;
		}
		catch(Exception e) {
			throw new FileStorageException("Could not store file");
		}
	}
	
	public Resource loadFileAsResource(String fileName){
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}

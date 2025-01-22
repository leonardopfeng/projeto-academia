package br.com.lelis.services;

import br.com.lelis.config.FileStorageConfig;
import br.com.lelis.exceptions.FileStorageException;
import br.com.lelis.exceptions.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {

    // Variable to save the complete location where the files will be saved
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {

        // Normalises the path
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize();

        // Create the path
        try{
            Files.createDirectories(this.fileStorageLocation);
        }
        catch (Exception e){
            throw new FileStorageException("Couldn't create directory to upload files", e);
        }
    }

    public String storeFile(MultipartFile file){
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try{
            if (filename.contains("..")){
                throw new FileStorageException("File name is invalid. Try changing it" + filename);
            }
            // These 2 lines can be changed to store the file in a different place, like a Cloud storage
            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        }
        catch (Exception e){
            throw new FileStorageException("Couldn't store file" + file + ". Please try again", e);
        }
    }

    public Resource loadFileAsResource(String filename){
        try{
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) return resource;
            else throw new MyFileNotFoundException("File not found");
        }
        catch (Exception e){
            throw new MyFileNotFoundException("File not found" + filename, e);
        }
    }


}

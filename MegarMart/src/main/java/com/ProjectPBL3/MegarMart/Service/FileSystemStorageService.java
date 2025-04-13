package com.ProjectPBL3.MegarMart.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService {

    private final Path rootLocation;

    public FileSystemStorageService() {
        rootLocation = Paths.get("C:\\Users\\Admin\\Desktop\\OneDrive - The University of Technology\\HistoryPBL3\\MegamartXONGLOGINFINAL\\MegarMart\\src\\main\\resources\\static\\img");
//        rootLocation = Paths.get("C:\\Users\\minhc\\OneDrive - The University of Technology\\HistoryPBL3\\MegamartXONGLOGINFINAL\\MegarMart\\src\\main\\resources\\static\\img");
    }

    public void store(MultipartFile file) {
        try{
            Path destinationFile = rootLocation.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        try{
            Files.createDirectories(rootLocation);
        }catch (IOException e){e.printStackTrace();}
    }
}


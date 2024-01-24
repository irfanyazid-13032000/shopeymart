package com.enigma.shopeymart.service.impl;


import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.UIResource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;


    public FileStorageService(@Value("${app.path.file}") String fileStorageLocation){
        this.fileStorageLocation = Paths.get(fileStorageLocation);
        try {
            Files.createDirectories(this.fileStorageLocation);
        }catch (Exception e){
            throw new RuntimeException("error tidak bisa menemukan path nya");

        }
    }




    public String uploadFile(MultipartFile file){
        String mimeType = file.getContentType();
//        check file type just image
        if (mimeType == null || (!mimeType.startsWith("image/"))){
            throw new RuntimeException("invalid upload, only image");
        }


        try{
            String fileName = file.getOriginalFilename();
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return file.getOriginalFilename();
        }catch (IOException e){
            throw new RuntimeException("could not store" + file.getOriginalFilename() + "please check again" + e);
        }
    }



    public Resource downloadFile(String nameFile)throws FileNotFoundException{
        try {
            Path targetLocation = this.fileStorageLocation.resolve(nameFile).normalize();
            Resource resource = new UrlResource(targetLocation.toUri());
            if (resource.exists()){
                return resource;
            }else {
                throw new FileNotFoundException("File not found " + nameFile);
            }

        }catch (MalformedURLException e){
            throw new FileNotFoundException("file not found " + nameFile + e);
        }
    }



}

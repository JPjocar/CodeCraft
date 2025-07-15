/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Config.StaticRoutes;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    
    //Retornar Imagenes
    public Resource createResource(String filename){
        Path file = StaticRoutes.pathUploads().resolve(filename).normalize();
        try{
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }else{
                return null;
            }
        }catch(MalformedURLException e){
            System.out.println("Resource doesn`t exist");
            return null;
        }
    }

    
    public String create(MultipartFile image_file) throws IOException {
        String originalFileName = image_file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String filename = UUID.randomUUID() + extension;
       
        if(!Files.exists(StaticRoutes.pathUploads()))
            Files.createDirectories(StaticRoutes.pathUploads());
           
        String type = image_file.getContentType();
        if(!type.startsWith("image/")){
            throw new IOException("Only images");
        }
        
        Path destination = StaticRoutes.pathUploads().resolve(filename).normalize();
        Files.copy(image_file.getInputStream(), destination);
        
        return "http://localhost:8080/uploads/"+filename;
    }
}

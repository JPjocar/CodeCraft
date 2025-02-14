/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Config.StaticRoutes;
import java.net.MalformedURLException;
import java.nio.file.Path;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

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
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;


public class StaticRoutes {
    
    private static Path uploads;
    
    @Value("${spring.directory.uploads}")
    private static void setPathUploads(String uploads) {
        StaticRoutes.uploads = Paths.get(uploads);
    }
   
    public static Path pathUploads(){
        return Paths.get("C:/Users/Cosmos/Desktop/uploads");
    }
    
}

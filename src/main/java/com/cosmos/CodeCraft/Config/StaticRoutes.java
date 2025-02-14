/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Config;

import java.nio.file.Path;
import java.nio.file.Paths;


public class StaticRoutes {
    public static final Path root = Paths.get(System.getProperty("user.dir"));
    public static final Path uploads = Paths.get("src/main/resources/static/uploads");
    
    public static Path pathUploads() {
        return root.resolve(uploads);
    }
   
    
}

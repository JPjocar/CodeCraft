/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StaticRoutes {

    private final Path uploads;

    public StaticRoutes(@Value("${spring.directory.uploads}") String uploadsDirectory) {
        this.uploads = Paths.get(uploadsDirectory).toAbsolutePath().normalize();
    }

    public Path pathUploads() {
        return this.uploads;
    }
}

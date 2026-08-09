/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Rutas de almacenamiento en disco.
 *
 * Antes era una clase estatica con @Value sobre un setter privado estatico, cosa
 * que Spring nunca inyecta, y devolvia una ruta fija de la maquina del autor
 * (C:/Users/Cosmos/Desktop/uploads). Ahora es un bean normal que lee de verdad
 * la propiedad spring.directory.uploads.
 */
@Component
public class StaticRoutes {

    private final Path uploads;

    public StaticRoutes(@Value("${spring.directory.uploads}") String uploadsDirectory) {
        // Absoluta y normalizada una sola vez: es la raiz contra la que se
        // comparan todas las rutas resueltas para detectar path traversal.
        this.uploads = Paths.get(uploadsDirectory).toAbsolutePath().normalize();
    }

    public Path pathUploads() {
        return this.uploads;
    }
}

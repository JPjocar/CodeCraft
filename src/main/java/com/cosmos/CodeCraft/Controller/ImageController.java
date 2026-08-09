/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Controller;

import com.cosmos.CodeCraft.Service.ImageService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "uploads")
public class ImageController {

    @Autowired
    private ImageService imageService;

    //Retornar recurso de imagen
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> get(@PathVariable("filename") String filename){
        // resolveSafely lanza InvalidFilenameException (-> 400) si el nombre
        // intenta salir del directorio de uploads.
        Path file = this.imageService.resolveSafely(filename);
        try {
            Resource resource = new UrlResource(file.toUri());

            // Antes era '&&': un archivo existente pero no legible pasaba el filtro.
            if(!resource.exists() || !resource.isReadable()){
                return ResponseEntity.notFound().build();
            }

            // El Content-Type se deduce del archivo en disco, no de la conexion.
            String contentType = Files.probeContentType(file);
            MediaType mediaType = contentType != null
                    ? MediaType.parseMediaType(contentType)
                    : MediaType.APPLICATION_OCTET_STREAM;

            return ResponseEntity.ok()
                        .contentType(mediaType)
                        .header("Content-Disposition", "inline; filename=\""+filename+"\"")
                        .header("Cache-Control", "public, max-age=31536000")
                        .body(resource);
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestParam("image_file") MultipartFile image_file) throws IOException{
        String imageUrl = this.imageService.create(image_file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(imageUrl);
    }

}

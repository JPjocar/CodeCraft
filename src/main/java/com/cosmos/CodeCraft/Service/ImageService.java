/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Config.StaticRoutes;
import com.cosmos.CodeCraft.Exception.InvalidFilenameException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp");

    @Autowired
    private StaticRoutes staticRoutes;

    @Value("${codecraft.uploads.public-url}")
    private String publicUrl;

    public Path resolveSafely(String filename) {
        if (filename == null || filename.isBlank()) {
            throw new InvalidFilenameException("El nombre de archivo es obligatorio");
        }

        Path root = this.staticRoutes.pathUploads();
        Path resolved;
        try {
            resolved = root.resolve(filename).normalize().toAbsolutePath();
        } catch (InvalidPathException ex) {
            throw new InvalidFilenameException("Nombre de archivo no valido: " + filename);
        }

        if (!resolved.startsWith(root)) {
            throw new InvalidFilenameException("Nombre de archivo no valido: " + filename);
        }
        return resolved;
    }

    public String create(MultipartFile image_file) throws IOException {
        String type = image_file.getContentType();
        if (type == null || !type.startsWith("image/")) {
            throw new InvalidFilenameException("Solo se permiten imagenes");
        }

        String extension = extractExtension(image_file.getOriginalFilename());
        String filename = UUID.randomUUID() + extension;

        Path root = this.staticRoutes.pathUploads();
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        Path destination = resolveSafely(filename);
        Files.copy(image_file.getInputStream(), destination);

        return this.publicUrl + "/" + filename;
    }

    private String extractExtension(String originalFileName) {
        if (originalFileName == null) {
            throw new InvalidFilenameException("El archivo no tiene nombre");
        }
        int dot = originalFileName.lastIndexOf('.');
        if (dot < 0) {
            throw new InvalidFilenameException("El archivo no tiene extension");
        }
        String extension = originalFileName.substring(dot).toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new InvalidFilenameException("Extension no permitida: " + extension);
        }
        return extension;
    }
}

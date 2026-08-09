package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Config.StaticRoutes;
import com.cosmos.CodeCraft.Exception.InvalidFilenameException;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica la defensa contra path traversal de ImageService.
 *
 * Se prueba a nivel de unidad a proposito: por HTTP, Tomcat ya rechaza por su
 * cuenta las barras codificadas (%2f, %5c), asi que un test de integracion
 * devolveria 400 sin llegar a ejecutar nuestra comprobacion. Aqui se ejercita
 * el metodo directamente.
 */
class ImageServiceTest {

    private ImageService imageService;
    private Path root;

    @BeforeEach
    void setUp() {
        StaticRoutes staticRoutes = new StaticRoutes("./target/test-uploads");
        this.root = staticRoutes.pathUploads();
        this.imageService = new ImageService();
        ReflectionTestUtils.setField(imageService, "staticRoutes", staticRoutes);
    }

    @ParameterizedTest
    @DisplayName("Rechaza nombres que intentan salir del directorio de uploads")
    @ValueSource(strings = {
        "../secret.txt",
        "../../pom.xml",
        "../../../../Windows/win.ini",
        "..\\..\\Windows\\win.ini",
        "subdir/../../escape.txt",
        "..",
        "/etc/passwd",
        "C:/Windows/win.ini"
    })
    void rejectsTraversalAttempts(String filename) {
        assertThrows(InvalidFilenameException.class,
                () -> imageService.resolveSafely(filename),
                () -> "Deberia haberse rechazado: " + filename);
    }

    @Test
    @DisplayName("Rechaza nombres vacios o nulos")
    void rejectsBlankNames() {
        assertThrows(InvalidFilenameException.class, () -> imageService.resolveSafely(null));
        assertThrows(InvalidFilenameException.class, () -> imageService.resolveSafely("  "));
    }

    @Test
    @DisplayName("Acepta un nombre normal y lo resuelve dentro del directorio")
    void acceptsPlainFilename() {
        Path resolved = imageService.resolveSafely("f47ac10b-58cc-4372-a567-0e02b2c3d479.png");

        assertTrue(resolved.startsWith(root), "La ruta resuelta debe quedar dentro de uploads");
        assertEquals("f47ac10b-58cc-4372-a567-0e02b2c3d479.png",
                resolved.getFileName().toString());
    }
}

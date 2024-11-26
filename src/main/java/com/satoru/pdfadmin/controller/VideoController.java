package com.satoru.pdfadmin.controller;

import com.satoru.pdfadmin.dto.VideoInfo;
import com.satoru.pdfadmin.entity.Video;
import com.satoru.pdfadmin.service.VideoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.satoru.pdfadmin.config.WebConfig.getResourceResponseVideoEntity;

@RestController
@RequestMapping("/api/video")
@CrossOrigin(origins = "http://localhost:3000")
public class VideoController {

    @Value("${file.upload.root-dir}")
    private String rootDir;

    private final VideoService fileService;

    @Autowired
    public VideoController(VideoService fileService) {
        this.fileService = fileService;
    }

//    http://localhost:8080/api/video/thumbnail?fileName=E:%5CDowns%5Cbase%5Cvideos%5Cthumbnails%5Ccuck%5Cvideo_2024-10-26_09-13-23_thumbnail.png
    @GetMapping("/thumbnail")
    public ResponseEntity<Resource> getThumbnail(@RequestParam String fileName) {
        try {
            // Decode the file path
            String decodedPath = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            Path filePath = Paths.get(decodedPath);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Serve the file
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000")
                        .header("Cross-Origin-Resource-Policy", "same-site") // Add this for COEP
                        .header("Cross-Origin-Embedder-Policy", "require-corp")
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName().toString() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

//    http://localhost:8080/api/video/stream?fileName=E%3A%5C%5C%5C%5CDowns%5C%5C%5C%5Cbase%5C%5C%5C%5Ccontent%5C%5Cvideo%5C%5Ccuck%5C%5Cvideo_2024-10-26_09-13-23.mp4
    @GetMapping("/stream")
    public ResponseEntity<Resource> serveVideo(@RequestParam String fileName) {
        try {
            // Decode the file path
            String decodedPath = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            Path fullPath = Paths.get(decodedPath);

            // Ensure the requested file is within the allowed directory
            Path baseDir = Paths.get(rootDir).normalize();
            if (!fullPath.startsWith(baseDir)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Create a Resource for the file
            Resource resource = new UrlResource(fullPath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return getResourceResponseVideoEntity(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




    @GetMapping
    public ResponseEntity<List<Video>> getFiles () {
        return ResponseEntity.ok(fileService.getFiles());
    }

    @GetMapping("/list/**")
    public ResponseEntity<List<VideoInfo>> listFiles(HttpServletRequest request) {
        String path = extractPathFromRequest(request, "/api/files/list");
        return ResponseEntity.ok(fileService.listFiles(path));
    }

    @PostMapping("/upload/**")
//    @PreAuthorize("hasRole('UPLOAD')")
    public ResponseEntity<VideoInfo> uploadFile(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) throws IOException {
        String path = extractPathFromRequest(request, "/api/files/upload");
        return ResponseEntity.ok(fileService.saveFile(file, path));
    }

    @DeleteMapping("/**")
//    @PreAuthorize("hasRole('DELETE')")
    public ResponseEntity<Void> deleteFile(HttpServletRequest request) {
        String path = extractPathFromRequest(request, "/api/files");
        fileService.deleteFile(path);
        return ResponseEntity.ok().build();
    }

    private String extractPathFromRequest(HttpServletRequest request, String prefix) {
        String requestURI = request.getRequestURI();
        return requestURI.substring(prefix.length());
    }
}

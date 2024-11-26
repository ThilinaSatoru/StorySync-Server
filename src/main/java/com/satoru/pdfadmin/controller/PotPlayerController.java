package com.satoru.pdfadmin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/video")
public class PotPlayerController {

    @Value("${file.upload.root-dir}")
    private String rootDir;

    @Value("${player.path}")
    private String potPlayerPath;


    // http://localhost:8080/play?filePath=video/les/1az9cjx.mp4&arguments=/fullscreen
    // cmd - PotPlayerMini64.exe "E:/Downs/base/video/les/1aaq6f5.mp4"
    @GetMapping("/play")
    public String playMedia(  // E:\Downs\base\video\les\1aaq6f5.mp4
            @RequestParam String filePath,
            @RequestParam(required = false, defaultValue = "") String arguments
    ) {
        // Decode the file path
        String decodedPath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
        Path fullPath = Paths.get(decodedPath);

        String command = potPlayerPath + " " + arguments + " \"" + fullPath + "\"";
        System.out.println("CMD : " + command);

        try {
            // Kill any running PotPlayer processes to make sure no stale processes are blocking
            ProcessBuilder killProcess = new ProcessBuilder("taskkill", "/F", "/IM", "PotPlayerMini64.exe");
            Process killProcessInstance = killProcess.start();
            killProcessInstance.waitFor(); // Wait for the kill process to complete
            Thread.sleep(1000);  // Add a short delay to allow system cleanup (1 second)

            // Start a new PotPlayer process
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            processBuilder.redirectErrorStream(true); // Merge error output with standard output
            Process process = processBuilder.start();

            // Capture and log the output and error streams for debugging
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Output: " + line);
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("Error: " + errorLine);
            }

            // Wait for the process to finish (optional, if you need to block here)
            process.waitFor();


            return "PotPlayer launched successfully for file: " + command;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(command);
            return "Failed to execute command: " + e.getMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
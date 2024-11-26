package com.satoru.pdfadmin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoPreviewService {

    @Value("${video.preview.path}")
    private String previewPath;

    @Value("${file.upload.root-dir}")
    private String rootDir;

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private String generatePreviewFileName(String originalFileName) {
        return originalFileName.replaceFirst("[.][^.]+$", "") + "_preview.mp4";
    }
}
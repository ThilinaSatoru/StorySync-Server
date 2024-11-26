package com.satoru.pdfadmin.service;

import com.satoru.pdfadmin.dto.VideoInfo;
import com.satoru.pdfadmin.entity.Video;
import com.satoru.pdfadmin.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VideoService {
    @Value("${file.upload.root-dir}")
    private String rootDir;

    private final VideoRepository videoRepository;

    Logger log = LoggerFactory.getLogger(VideoService.class);

    @Autowired
    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public List<Video> getFiles(){
        return videoRepository.findAll();
    }

    public List<VideoInfo> listFiles(String relativePath) {
        File directory = new File(rootDir + relativePath);
        List<VideoInfo> files = new ArrayList<>();

        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                files.add(new VideoInfo(
                        file.getName(),
                        file.isDirectory(),
                        file.length(),
                        new Date(file.lastModified())
                ));
            }
        }
        return files;
    }

    public VideoInfo saveFile(MultipartFile file, String relativePath) throws IOException {
        String fullPath = rootDir + relativePath;
        File directory = new File(fullPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File destFile = new File(fullPath + "/" + file.getOriginalFilename());
        file.transferTo(destFile);

        return new VideoInfo(
                destFile.getName(),
                false,
                destFile.length(),
                new Date(destFile.lastModified())
        );
    }

    public void deleteFile(String relativePath) {
        File file = new File(rootDir + relativePath);
        if (file.exists()) {
            file.delete();
        }
    }
}

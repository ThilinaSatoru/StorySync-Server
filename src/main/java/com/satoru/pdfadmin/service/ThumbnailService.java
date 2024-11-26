package com.satoru.pdfadmin.service;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ThumbnailService {
    @Value("${app.thumbnail.directory}")
    private String thumbnailDirectory;

    @Value("${app.thumbnail.base-url}")
    private String thumbnailBaseUrl;

    public String generateThumbnail(String videoPath) throws Exception {
        // Create thumbnails directory if it doesn't exist
        Path thumbnailDir = Paths.get(thumbnailDirectory);
        if (!Files.exists(thumbnailDir)) {
            Files.createDirectories(thumbnailDir);
        }

        // Generate thumbnail filename based on video checksum or path
        String thumbnailFilename = generateThumbnailFilename(videoPath);
        Path thumbnailPath = thumbnailDir.resolve(thumbnailFilename);

        // Check if thumbnail already exists
        if (Files.exists(thumbnailPath)) {
            return thumbnailFilename;
        }

        // Open video file
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
        grabber.start();

        try {
            // Calculate frame position (25% of video duration)
            int framePosition = (int) (grabber.getLengthInFrames() * 0.50);
            grabber.setFrameNumber(framePosition);

            // Grab frame and convert to image
            Frame frame = grabber.grab();
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage bufferedImage = converter.convert(frame);

            // Resize thumbnail if needed
            BufferedImage resized = resizeImage(bufferedImage, 320, 180); // 16:9 aspect ratio

            // Save thumbnail
            ImageIO.write(resized, "jpg", thumbnailPath.toFile());

            return thumbnailFilename;
        } finally {
            grabber.stop();
            grabber.release();
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private String generateThumbnailFilename(String videoPath) {
        return UUID.randomUUID().toString() + ".jpg";
    }

    public String getThumbnailUrl(String thumbnailFilename) {
        if (thumbnailFilename == null) return null;
        return thumbnailBaseUrl + "/thumbnails/" + thumbnailFilename;
    }
}

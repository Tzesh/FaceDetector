package edu.estu;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FaceDetector {
    List<String> imageNames = null;
    File tempDir;

    public void detectAndDisplay(String pathName) throws IOException {
        imageNames = new LinkedList<>();
        nu.pattern.OpenCV.loadLocally();
        Mat src = Imgcodecs.imread(pathName);

        String absolutePath = getFile("/models/haarcascade_frontalface_alt2.xml").getAbsolutePath();
        CascadeClassifier cc = new CascadeClassifier(absolutePath);

        MatOfRect faceDetection = new MatOfRect();
        cc.detectMultiScale(src, faceDetection);

        Path tempDir = Files.createTempDirectory("faceDetectionImages");
        tempDir.toFile().deleteOnExit();
        this.tempDir = tempDir.toFile().getAbsoluteFile();

        int detectedFaces = faceDetection.toArray().length;
        System.out.println(String.format("Detected faces: %d", detectedFaces));

        Rect rect_Crop = null;
        File faceImage = null;
        for (Rect rect : faceDetection.toArray()) {
            Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x
                    + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
            rect_Crop = new Rect(rect.x, rect.y, rect.width, rect.height);

            Mat image_roi = new Mat(src, rect_Crop);
            faceImage = File.createTempFile("detectedFace", ".png", tempDir.toFile());
            faceImage.deleteOnExit();
            Imgcodecs.imwrite(faceImage.getAbsolutePath(), image_roi);
            imageNames.add(faceImage.getAbsolutePath());
            detectedFaces--;
        }
        File imageFile = File.createTempFile("image", ".png", tempDir.toFile());
        imageFile.deleteOnExit();
        Imgcodecs.imwrite(imageFile.getAbsolutePath(), src);
        System.out.println("Face detection process has done.\n"
                + "All the faces and image are below, you can traversal between faces and browse all the images by pressing 'Browse' button below.");
    }

    public List<String> getImageNames() {
        return imageNames;
    }

    private File getFile(String fileName) throws IOException {
        File file = null;
        String resource = fileName;
        URL res = getClass().getResource(resource);
        if (res.getProtocol().equals("jar")) {
            InputStream input = getClass().getResourceAsStream(resource);
            file = File.createTempFile("tempfile", ".tmp");
            OutputStream out = new FileOutputStream(file);
            int read;
            byte[] bytes = new byte[1024];

            while ((read = input.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.close();
            file.deleteOnExit();
        } else {
            file = new File(res.getFile());
        }

        if (file != null && !file.exists()) {
            throw new RuntimeException("Error: File " + file + " not found!");
        }
        return file;
    }

    public File getTempDir() {
        return tempDir;
    }
}

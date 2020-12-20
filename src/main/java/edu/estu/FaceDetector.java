package edu.estu;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FaceDetector {
    List<String> imageNames = null;

    public void detectAndDisplay(String pathName) throws URISyntaxException {
        imageNames = new LinkedList<>();
        nu.pattern.OpenCV.loadLocally();
        Mat src = Imgcodecs.imread(pathName);

        URL res = getClass().getClassLoader().getResource("models/haarcascade_frontalface_alt2.xml");
        File file = Paths.get(res.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();
        CascadeClassifier cc = new CascadeClassifier(absolutePath);

        MatOfRect faceDetection = new MatOfRect();
        cc.detectMultiScale(src, faceDetection);

        int detectedFaces = faceDetection.toArray().length;
        System.out.println(String.format("Detected faces: %d", detectedFaces));

        Rect rect_Crop = null;
        for (Rect rect : faceDetection.toArray()) {
            Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x
                    + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
            rect_Crop = new Rect(rect.x, rect.y, rect.width, rect.height);

            Mat image_roi = new Mat(src, rect_Crop);
            StringBuilder sb = new StringBuilder("detected_face_");
            sb.append(detectedFaces).append(".jpg");
            Imgcodecs.imwrite(sb.toString(), image_roi);
            imageNames.add(sb.toString());
            detectedFaces--;
        }
        Imgcodecs.imwrite("image_out.png", src);
        System.out.println("Face detection process has done.\n"
                + "All the faces are framed and saved the image as: 'image_out.png' in the same directory of the program.\n"
                + "Also, faces detected in the image are all saved in 'faces' folder.");
    }

    public List<String> getImageNames() {
        return imageNames;
    }


}

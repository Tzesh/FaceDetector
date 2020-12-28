package edu.eskisehir;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;

public class FaceDetector {
    List<String> imageNames = null; // name (path) of the images to show them
    File tempDir; // temporary direction to save, show and browse images
    String resultImage = null; // result image means, the original image with faces framed

    public void detectAndSave(String pathName) throws IOException {
        imageNames = new LinkedList<>(); // imageNames have to be saved as LinkedList
        nu.pattern.OpenCV.loadLocally(); // before using OpenCV make sure that it's loaded
        Mat src = Imgcodecs.imread(pathName); // getting the image according to the given path from Interface

        String absolutePath = getFile("/models/haarcascade_frontalface_alt2.xml").getAbsolutePath(); // write an extra method is mandatory to get absolute path of required trained model since CascadeClassifier wants
        CascadeClassifier cc = new CascadeClassifier(absolutePath); // creating our CascadeClassifier to detect faces in the given image

        MatOfRect faceDetection = new MatOfRect(); // creating math of rectangle means, after selecting faces we'll locate them as rectangles
        cc.detectMultiScale(src, faceDetection); // selecting and locating the faces as rectangles

        Path tempDir = Files.createTempDirectory("faceDetectionImages"); // creating a temporary directory to save each faces and result image
        tempDir.toFile().deleteOnExit(); // making sure that the temporary directory will not exist after the program closed
        this.tempDir = tempDir.toFile().getAbsoluteFile(); // to access temporary directory from Interface in order to browse all the images that mentioned before

        int detectedFaces = faceDetection.toArray().length; // detected faces can be observed easily
        System.out.println(String.format("Detected faces: %d", detectedFaces)); // printing how many faces are detected

        for (Rect rect : faceDetection.toArray()) { // getting each faces location and creating a rectangle which colored as (0, 255, 0) and also cutting the faces and saving them as images
            Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x
                    + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
            Rect rect_Crop = new Rect(rect.x, rect.y, rect.width, rect.height);

            Mat image_roi = new Mat(src, rect_Crop);
            File faceImage = File.createTempFile("detectedFace", ".png", tempDir.toFile());
            faceImage.deleteOnExit(); // faces are not required after program is done
            Imgcodecs.imwrite(faceImage.getAbsolutePath(), image_roi);
            imageNames.add(faceImage.getAbsolutePath());
            detectedFaces--;
        }
        File imageFile = File.createTempFile("image", ".png", tempDir.toFile()); // saving the result image to temporary directory
        imageFile.deleteOnExit(); // result image is not required after program is done
        this.resultImage = imageFile.getAbsolutePath(); // getting the path of result image to show it in Interface
        Imgcodecs.imwrite(imageFile.getAbsolutePath(), src); // saving it as all the faces are framed
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

    public String getResultImage() {
        return resultImage;
    }
}

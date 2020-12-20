# FaceDetector
## How it all begun?
It all begun by just an enthusiasm, we've got excited when we saw "Face detection application with OpenCV library" title on suggested projects section of instruction of project of Principles of Software Design and Development course.
And, we have to mention it's just a project of Principles of Software Design and Development course, but you are free to use it as well.
## Usage of the program
You have 2 options in here, either you download the source code and build .JAR file to use the program wherever you want or just simply download the final release of the program.
The rest is just the same as below, usage of the program is too easy.

![GUI](https://i.imgur.com/z6uIpR5.gif)

## Background of the program
FaceDetector uses OpenCV library and loads it locally of course in the first place, then it uses CascadeClassifier, MatOfRect classes and also trained face detection model which is named as "haarcascade_frontalface_alt2.xml" to detect faces and process them.
FaceDetector only selects image files, creates temporary files just in case if you want to browse or save them also deletes them on exit. You can also preview images without browsing or selecting each image or face especially.


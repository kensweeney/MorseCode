package com.ken.camel.opencv;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.SimpleBlobDetector;
import org.opencv.features2d.SimpleBlobDetector_Params;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.BarcodeDetector;
import org.opencv.objdetect.DetectorParameters;
import org.opencv.objdetect.GraphicalCodeDetector;

import nu.pattern.OpenCV;

public class OpenCVProcessor implements Processor {
    
    public OpenCVProcessor() {
        OpenCV.loadLocally();
    }

    public void process(Exchange exchange) throws Exception {
        Message m = exchange.getIn();
        
        Mat image = Imgcodecs.imread(m.getHeader("CamelFileAbsolutePath", String.class), Imgcodecs.IMREAD_GRAYSCALE);
        // Check if the image was loaded successfully
        if (image.empty()) {
            exchange.setRouteStop(true);
            System.out.println("Error: Could not load image from " + m.getHeader("ImagePath", String.class));
            return;
        }
        //GraphicalCodeDetector detector = new GraphicalCodeDetector
        
        SimpleBlobDetector_Params params = new SimpleBlobDetector_Params();
        //params.set_minRepeatability(6);
        params.set_collectContours(true);
        //params.
        // Create a SimpleBlobDetector object
        SimpleBlobDetector detector = SimpleBlobDetector.create(params);

        // Lists to store decoded information and barcode types
        List<String> decodedInfo = new ArrayList<>();
        List<String> decodedType = new ArrayList<>();

        // Mat to store the corners of detected barcodes
        MatOfKeyPoint points = new MatOfKeyPoint();

        // Detect and decode barcodes
        detector.detect(image, points);

        ArrayList<MatOfPoint> list = new ArrayList<>(detector.getBlobContours());
        for (MatOfPoint matOfPoint : list) {
            System.out.println(matOfPoint.dump());
        }
            System.out.println("Barcode detection and decoding successful.");

            // Iterate through the detected barcodes
            for (int i = 0; i < decodedInfo.size(); i++) {
                String info = decodedInfo.get(i);
                String type = decodedType.get(i);

                System.out.println("Barcode " + (i + 1) + ":");
                System.out.println("  Info: " + info);
                System.out.println("  Type: " + type);

                // Draw bounding box around the detected barcode
                if (!points.empty() && points.rows() > 0) {
                    // Each barcode has 4 points (corners)
                    int pIdx = i * 4;
                    Point p1 = new Point(points.get(pIdx, 0)[0], points.get(pIdx, 0)[1]);
                    Point p2 = new Point(points.get(pIdx + 1, 0)[0], points.get(pIdx + 1, 0)[1]);
                    Point p3 = new Point(points.get(pIdx + 2, 0)[0], points.get(pIdx + 2, 0)[1]);
                    Point p4 = new Point(points.get(pIdx + 3, 0)[0], points.get(pIdx + 3, 0)[1]);

                    Imgproc.line(image, p1, p2, new Scalar(0, 255, 0), 2); // Green color
                    Imgproc.line(image, p2, p3, new Scalar(0, 255, 0), 2);
                    Imgproc.line(image, p3, p4, new Scalar(0, 255, 0), 2);
                    Imgproc.line(image, p4, p1, new Scalar(0, 255, 0), 2);
                }
            }

            // Save the image with drawn bounding boxes (optional)
            String fileOut = "e:/morse/outFile/output_image_with_barcodes.jpg";
            Imgcodecs.imwrite(fileOut, image);
            System.out.println("Output image saved as " + fileOut);

//        } else {
//            System.out.println("No barcodes detected or decoding failed.");
//        }

        // Release resources
        image.release();
        points.release();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}

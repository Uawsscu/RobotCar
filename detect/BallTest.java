
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.*;

public class BallTest {

    public static void imshow(Mat src) {

        BufferedImage bufImage = null;
        try {
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".jpg", src, matOfByte);
            byte[] byteArray = matOfByte.toArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);

            JFrame frame = new JFrame("Image");
            frame.getContentPane().setLayout(new FlowLayout());
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        String color = sc.next();
        // Load the library

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Consider the image for processing
        Mat image = Imgcodecs.imread("E:\\Robot\\image.png", Imgproc.COLOR_BGR2GRAY);
        Mat imageHSV = new Mat(image.size(), CvType.CV_8UC4);
        Mat imageBlurr = new Mat(image.size(), CvType.CV_8UC4);
        Mat imageA = new Mat(image.size(), CvType.CV_32F);
        Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5, 5), 0);
        Imgproc.adaptiveThreshold(imageBlurr, imageA, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 7, 5);
        imshow(image);
        Imgcodecs.imwrite("E:\\Robot\\ballOut1.png", imageBlurr);

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        //Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
        //Imgproc.drawContours(imageBlurr, contours, 1, new Scalar(0,0,255));
        Mat hierarchy = new Mat();

        // color filtering
        Mat redMap = new Mat(image.size(), CvType.CV_32F);
        if (color.equals("Red")) {
            Core.inRange(image, new Scalar(0, 0, 70), new Scalar(0, 0, 255), redMap);
        } else if (color.equals("Blue")) {
            Core.inRange(image, new Scalar(70, 0, 0), new Scalar(255, 0, 0), redMap);
        } else if (color.equals("Green")) {
            Core.inRange(image, new Scalar(0, 70, 0), new Scalar(0, 255, 0), redMap);
       
        } else if (color.equals("Yellow")) {
            Core.inRange(image, new Scalar(0, 70, 70), new Scalar(0, 255, 255), redMap);
        } 
        Imgproc.findContours(redMap, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

        if (hierarchy.size().height > 0 && hierarchy.size().width > 0) {
            for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0]) {

                Imgproc.drawContours(image, contours, idx, new Scalar(0, 255, 0), 2);
                Rect rect = Imgproc.boundingRect(contours.get(idx));

                int x = rect.x + rect.width / 2;
                int y = rect.y + rect.height / 2;
                System.out.println("Ball position:" + x + "," + y);

            }
        }

        imshow(image);
        Imgcodecs.imwrite("E:\\Robot\\ballOut2.png", image);
    }
}

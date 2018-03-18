import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import jdk.nashorn.internal.parser.TokenType;
import com.jcraft.jsch.*;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Color1 {
    static Socket socket = null;
    static  String host = "192.168.1.3"; // echoServer address
    
    public static void main(String[] args) throws IOException, InterruptedException {
/*
//SendandReceive        
    Scanner input = new Scanner(System.in);
    String cmd = input.nextLine();
        
    try {
    int port = 25000; // communication port
    InetAddress address = InetAddress.getByName(host);
             socket = new Socket(address, port);

    // Send the message to the server -->>
    OutputStream os = socket.getOutputStream();
    OutputStreamWriter osw = new OutputStreamWriter(os);
    BufferedWriter bw = new BufferedWriter(osw);

    String sendMessage = cmd + "\n";
    bw.write(sendMessage);
    bw.flush();
    // <<-- send
    System.out.println("Sent:" + cmd);
    if(cmd.equals("capture")){
        TimeUnit.MILLISECONDS.sleep(2000);
    }
    
    //Get the return message from the server
    InputStream is = socket.getInputStream();
    InputStreamReader isr = new InputStreamReader(is);
    BufferedReader br = new BufferedReader(isr);
    String message = br.readLine();
    System.out.println("Received: " + message);

    os.close();
    osw.close();
    bw.close();
    is.close();
    isr.close();
    br.close();
} catch (IOException e) {
} finally {
    }
    
    
//GetFile    
    JSch jsch = new JSch();
    Session session = null;
try {
    // config the remote server ----->>
    session = jsch.getSession("test", "192.168.1.3", 22); // user, ip, port
    session.setConfig("StrictHostKeyChecking", "no");
    session.setPassword("1q2w3e4r5T");
    session.connect();
    // <<----- config the remote server
    
    // Get a file from server ----->>
    String src = "/home/test/cameraImg.png"; // from linux server
    String dst = "C:\\Users\\MoJi\\Downloads\\Robot\\image1.jpg"; // to windows (your pc)
    Channel channel = session.openChannel("sftp");
    channel.connect();
    ChannelSftp sftpChannel = (ChannelSftp) channel;
    sftpChannel.get(src, dst);
    sftpChannel.exit();
    // <<----- get a file from server
    
    session.disconnect();
} catch (JSchException | SftpException e) {

}

*/
//redball
 System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    // Consider the image for processing
    Mat image = Imgcodecs.imread("E:\\Robot\\image.png", Imgproc.COLOR_BGR2GRAY);
    Mat imageHSV = new Mat(image.size(), CvType.CV_8UC4);
    Mat imageBlurr = new Mat(image.size(), CvType.CV_8UC4);
    Mat imageA = new Mat(image.size(), CvType.CV_32F);
    Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
    Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
    Imgproc.adaptiveThreshold(imageBlurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);
    
    Imgcodecs.imwrite("E:\\Robot\\imageOut.png",imageBlurr);
    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();    
    //Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
    //Imgproc.drawContours(imageBlurr, contours, 1, new Scalar(0,0,255));
    Mat hierarchy = new Mat();
  // Imgproc.findContours(imageBlurr, contours, hierarchy, Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
  
   // color filtering
    Mat redMap   = new Mat(image.size(), CvType.CV_32F);

    Core.inRange(image, new Scalar(10,10 , 100), new Scalar(70,49 , 190), redMap); 
   // source, minBGR, maxGBR, destination  ตัดสีที่ไม่ต้องการ 
   
   // color filtering
    Mat greenMap   = new Mat(image.size(), CvType.CV_32F);
    Core.inRange(image, new Scalar(10, 100, 10), new Scalar(25, 150, 88), greenMap); // source, minBGR, maxGBR, destination  ตัดสีที่ไม่ต้องการ
 
   // color filtering
    Mat blueMap   = new Mat(image.size(), CvType.CV_32F);
    Core.inRange(image, new Scalar(70, 15, 15), new Scalar(165, 99, 18), blueMap); // source, minBGR, maxGBR, destination  ตัดสีที่ไม่ต้องการ
  
    
    Imgproc.findContours(redMap, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
   
    Imgproc.findContours(greenMap, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
   
    Imgproc.findContours(blueMap, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
   
   if(hierarchy.size().height>0&&hierarchy.size().width>0){
    for(int idx = 0 ;idx>=0;idx=(int) hierarchy.get(0,idx)[0]){
        //System.out.println(Imgproc.contourArea(contours.get(i)));
        if (Imgproc.contourArea(contours.get(idx)) > 50 ){
            Imgproc.drawContours(image, contours, idx, new Scalar(0,255,0),2);
            Rect rect = Imgproc.boundingRect(contours.get(idx));
           // System.out.println(rect.height);
                int x = rect.x + rect.width/2;
                int y = rect.y + rect.height/2;
            System.out.println("Ball position:" + x + "," + y);
            //Imgproc.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255));
        //}
    }
     
    Imgcodecs.imwrite("E:\\Robot\\image2.png",image);
   }
    }
    }
}
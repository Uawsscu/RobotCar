
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

public class client {

    private static Socket socket;

    public static void main(String args[]) throws IOException, JSchException {

        try {
            String host = "192.168.1.3"; // echoServer address
            int port = 25000; // communication portA

            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);

            // Send the message to the server -->>
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            String cmd = "capture";
            String sendMessage = cmd + "\n";
            bw.write(sendMessage);
            bw.flush();
            // <<-- send

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
            String dst = "E:/image.png"; // to windows (your pc)
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.get(src, dst);
            sftpChannel.exit();
            // <<----- get a file from server

            session.disconnect();
        } catch (JSchException | SftpException e) {

        }
    }
}
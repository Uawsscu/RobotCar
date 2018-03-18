import com.jcraft.jsch. *;
public class getFile {

    public static void main(String args[]) {
 
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
            String src = "/home/test/Robotis.jpg"; // from linux server
            String dst = "E:/image.jpg"; // to windows (your pc)
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

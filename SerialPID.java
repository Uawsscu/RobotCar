
/**
 *
 * @author tong_Uawsscu
 */
import java.util.*;
import jssc.*;
import java.util.concurrent.TimeUnit;

public class SerialPID {

    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        PID t = new PID(1.0, 0.1, 0.5, 50.0);
        int maxSpeed = 100;

        String[] portNames = SerialPortList.getPortNames();
        if (portNames.length > 0) {
            for (String portName : portNames) {
                System.out.print(portName);
            }

            SerialPort serialPort = new SerialPort(portNames[0]); // COM7
            System.out.println("..ready");
            System.out.println("   pv   err   P   I   D   PID    LM  RM ");
            try {
                serialPort.openPort();
                serialPort.setParams(9600, 8, 1, 0);
                TimeUnit.MILLISECONDS.sleep(3000); // 3000 is ok

                for (int i = 0; i < 20; i++) {
                    System.out.print(i + " ");
                    String data = "ir";
                    //sending...ok
                    serialPort.writeString(data);
                    TimeUnit.MILLISECONDS.sleep(1000);

                    //reading...ok
                    try {
                        int len1 = Integer.parseInt(serialPort.readString(1, 150));
                        int sensor1 = Integer.parseInt(serialPort.readString(len1));

                        // System.out.println("len1 "+len1+" sensor "+sensor1);
                        int len2 = Integer.parseInt(serialPort.readString(1, 150));
                        int sensor2 = Integer.parseInt(serialPort.readString(len2));
                        //  System.out.println("len2 "+len2+" sensor "+sensor2);
                        if (sensor1 > 500) {
                            sensor1 = 1;
                        }
                        if (sensor2 > 500) {
                            sensor2 = 1;
                        }
                        if (sensor1 <= 500) {
                            sensor1 = 0;
                        }
                        if (sensor2 <= 500) {
                            sensor2 = 0;
                        }
                        int pid = t.process(sensor1, sensor2);

                        int LSpeed = 0;
                        int RSpeed = 0;
                        if (pid < 0) {
                            LSpeed = maxSpeed;
                            RSpeed = (int) (maxSpeed + pid);
                        } else {
                            LSpeed = (int) (maxSpeed - pid);
                            RSpeed = maxSpeed;

                        }

                        System.out.format("  [%3d,%3d]\n", LSpeed, RSpeed);
                        serialPort.writeString(LSpeed + "");
                        TimeUnit.MILLISECONDS.sleep(1500);
                        serialPort.writeString(RSpeed + "");
                        TimeUnit.MILLISECONDS.sleep(1500);
                    } catch (SerialPortTimeoutException sTo) {
                        System.out.println(i + " timeout!");
                    }

                }

                TimeUnit.MILLISECONDS.sleep(1500); // 1500 is ok
                serialPort.closePort();//Close serial port

            } catch (SerialPortException sEx) {
                System.out.println(sEx);
            }

        }

    }
}
/*
String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;  // whether the string is complete

void setup() {
  Serial.begin(9600);

}

void loop() {

  // Reading data from interrupt..ok
  if (stringComplete) {

    String str = "", str2 = "";
    String len = "", len2 = "";

    if (inputString.equals("ir")) {
      str = (String)analogRead(A0);
      len = String(str.length(), DEC);
      str2 = (String)analogRead(A1);
      len2 = String(str2.length(), DEC);
    }
    if (len.length() > 0) {
      Serial.print(len);
      Serial.print(str);
      Serial.print(len2);
      Serial.print(str2);
    }
   

    stringComplete = false;
  }
}

void serialEvent() {
  while (Serial.available()) {
    inputString = Serial.readString();
    if (inputString.length() > 0) {
      stringComplete = true;
    }
  }
}*/

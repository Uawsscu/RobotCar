
import java.util.*;
import jssc.*;
import java.util.concurrent.TimeUnit;

public class R03{

    static SerialPortEvent event;

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length > 0) {

            for (String portName : portNames) {
                System.out.print(portName);
            }

            // Writing data..ok
            SerialPort serialPort = new SerialPort(portNames[0]); // COM9
            System.out.println("..ready");

            try {
                serialPort.openPort();// this will reset the arduino's comm
                //Set params. Also you can set params by this string: 
                serialPort.setParams(9600, 8, 1, 0);
                TimeUnit.MILLISECONDS.sleep(3000); // 3000 is ok

                for (int i = 0; i < 10; i++) {
                    //String data = sc.nextLine();
                    String data = "ir";
                    //sending...ok
                    serialPort.writeString(data);
                    TimeUnit.MILLISECONDS.sleep(1000); // 1500 is ok

                    //reading...ok
                    try {
                        int len = Integer.parseInt(serialPort.readString(1, 150));

                        // sensor value (integer)
                        if (len <= 4) { // this length may be 0 - 4 for the data of 0-1023
                            int sensor = Integer.parseInt(serialPort.readString(len));
                            System.out.println(i + " " + sensor);
                        } else { // message (string)
                            String msg = serialPort.readString(len);
                            System.out.println(i + " " + msg);
                        }

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
// Arduino-code *****

byte led = 13;
boolean state = LOW;
String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;  // whether the string is complete

void setup() {
  Serial.begin(9600);
  pinMode(led, OUTPUT);
  pinMode(13, OUTPUT);
  digitalWrite(13, LOW);
}

void loop() {

  //to control an led
  digitalWrite(led, state);

  // Reading data from interrupt..ok
  if (stringComplete) {

    String str = "";
    String len = "";

    if (inputString.equals("ir0")) {
      state = HIGH;
      str = "LED - ON";
      len = String(str.length(), DEC);
    }

    else if (inputString.equals("ir5")) {
      state = LOW;
      str = "LED - OFF"; // the length can't be more than 9
      len = String(str.length(), DEC);
    }

    else if (inputString.equals("ir1")) {
      int val = 0; // will be reading from analogRead(a0);
      str = String(val, DEC);
      len = String(str.length(), DEC);
    }

    else if (inputString.equals("ir2")) {
      int val = 511; // will be reading from analogRead(a1)
      str = String(val, DEC);
      len = String(str.length(), DEC);
    }

    else if (inputString.equals("ir3")) {
      int val = 1023; // will be reading from analogRead(a2)
      str = String(val, DEC);
      len = String(str.length());
    }

    if (len.length() > 0) {
      Serial.print(len);
      Serial.print(str);
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
}


*/

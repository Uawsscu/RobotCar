
import java.util.*;
import jssc.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerialComm {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] portNames = SerialPortList.getPortNames();
        for (String portName : portNames) {
            System.out.println(portName);
        }

        // Writing data..ok
        SerialPort serialPort = new SerialPort(portNames[0]); // COM9

        try {
            serialPort.openPort();// this will reset the arduino's comm
            //Set params. Also you can set params by this string: 
            //serialPort.setParams(9600, 8, 1, 0);
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE); //ไม่ใช้parity

            //serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN
            //        | SerialPort.FLOWCONTROL_RTSCTS_OUT);
            for (int i = 0; i < 1; i++) {
                String data = sc.next();
                serialPort.writeString(data);

                //serialPort.writeBytes("on".getBytes());//Write data to port                        
            }

            serialPort.closePort();//Close serial port

        } catch (SerialPortException ex) {
            System.out.println(ex);
        }

        //  Reading data .. ok with Serial.write() from the paired-arduino
        try {
            serialPort.openPort();//Open serial port
            serialPort.setParams(9600, 8, 1, 0);//Set params.

            for (int i = 0; i <10; i++) {
                //byte[] buffer = serialPort.readBytes();//Read 10 bytes from serial port                        
                TimeUnit.MILLISECONDS.sleep(1500);
                System.out.println(serialPort.readString(5));
               
               
            }
            serialPort.closePort();//Close serial port

        } catch (SerialPortException ex) {
            System.out.println(ex);
        } catch (InterruptedException ex) {
           ex.printStackTrace();
        }

    }
}

/*
// Arduino-code *****
byte led = 2;
byte state = LOW;
String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;  // whether the string is complete

void setup() {  
  Serial.begin(9600);
  
  // reserve 200 bytes for the inputString:
  inputString.reserve(200);
  pinMode(13, OUTPUT);
  pinMode(led, OUTPUT);  
  digitalWrite(13, LOW);
}

void loop() {
  digitalWrite(led, state);
  
  // Reading data from interrupt..ok
  if (stringComplete) {
    if(inputString.equals("on")){
      state = HIGH;
      Serial.write("LED is ON");
    }

    if(inputString.equals("off")){
      state = LOW;
      Serial.write("LED is OFF");
    }
    
    stringComplete = false;
  }
  
  
  // Writing data...ok to send data a string to a jSSC
  //Serial.write("Hello");
  
}

void serialEvent() {
  while (Serial.available()) {    
    inputString = Serial.readString();
    if (inputString.length()>0) {
      stringComplete = true;
    }
  }
}
 */

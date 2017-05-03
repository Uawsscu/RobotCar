
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
char inL1 = 1 ; //IN1
char inL2 = 2 ; //IN2
char EnaL = 3 ; //A

char inR1 = 8 ; //IN3
char inR2 = 9 ; //IN4
char EnaR = 10 ; //B

int speedTime = 45; //มากกว่า60ถึงได้
//LOW HIGH หมุนตรง
void setup() {

  Serial.begin(9600);
  pinMode(inL1, OUTPUT);
  pinMode(inL2, OUTPUT);
  pinMode(inR1, OUTPUT);
  pinMode(inR2, OUTPUT);
  pinMode(EnaL, OUTPUT);
  pinMode(EnaR, OUTPUT);
}

void loop() {
  String str = "", str2 = "";
  String len = "", len2 = "";
  ///////>>>><<<<<<//////
  if (Serial.available() > 0) {
    String inputString = Serial.readString();
    if (inputString.equals("ir")) {
      str = (String)analogRead(A0);//ตัวขวาRight
      len = String(str.length(), DEC); // print as an ASCII-encoded decimal
      str2 = (String)analogRead(A1);//ตัวซ้ายLeft
      len2 = String(str2.length(), DEC);
    }
    if (len.length() > 0) {
      Serial.print(len);
      Serial.print(str);
      Serial.print(len2);
      Serial.print(str2);
    }
    int inputSpeedL = Serial.readString().toInt();
    int inputSpeedR = Serial.readString().toInt();
    //ดำ1 ขาว 0
    //ดำเยอะขาวน้อย


    analogWrite(EnaL,  inputSpeedL);
    analogWrite(EnaR,  inputSpeedR);
    int sensorValueL = str.toInt();
    int sensorValueR = str2.toInt();
    //เดินตามเส้นสีดำ
    if (sensorValueL == 0 && sensorValueR <= 0) { //ขาวและขาว ตรง
      walk();
    }
    else if (sensorValueL == 0 && sensorValueR == 1) { //ขาวซ้ายและดำขวา เรียวขวา
      turnRight();
    }
    else if (sensorValueL == 1 && sensorValueR == 0) { //ดำด้านซ้ายและ ขาวขวา เรียวซ้าย
      turnLeft();
    }
    else if (sensorValueL == 1 && sensorValueR == 1) { //ดำและดำ หยุด
      stops();
    }
  }

  ///////>>>><<<<<<//////
}


void walk() {
  digitalWrite(inL1, LOW);
  digitalWrite(inL2, HIGH);

  digitalWrite(inR1, LOW);
  digitalWrite(inR2, HIGH);

}
void turnRight() {
  //ซ้ายหมุนปรกติ
  digitalWrite(inL1, LOW);
  digitalWrite(inL2, HIGH);
  //ขวาหมุนกลับ(HIGH LOW) หรือหยุด (LOW LOW หรือ HIGH HIGH)

  digitalWrite(inR1, HIGH);
  digitalWrite(inR2, LOW);

}
void turnLeft() {
  digitalWrite(inL1, HIGH);
  digitalWrite(inL2, LOW);

  digitalWrite(inR1, LOW);
  digitalWrite(inR2, HIGH);


}

void stops() {
  digitalWrite(inL1, LOW);
  digitalWrite(inL2, LOW);

  digitalWrite(inR1, LOW);
  digitalWrite(inR2, LOW);

}



}*/

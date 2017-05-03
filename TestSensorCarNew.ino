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




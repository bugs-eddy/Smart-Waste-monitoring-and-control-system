
#include<SoftwareSerial.h>
#if defined(ESP32)
#include <WiFi.h>
#include <FirebaseESP32.h>
#elif defined(ESP8266)
#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>
#endif

#define TRIG_PIN 2 // ESP32 pin D2 connected to Ultrasonic Sensor's TRIG pin
#define ECHO_PIN 5// ESP32 pin D5 connected to Ultrasonic Sensor's ECHO pin
#define TRIG_PIN2 15 // ESP32 pin D15 connected to Ultrasonic Sensor's TRIG pin
#define ECHO_PIN2 35// ESP32 pin D35 connected to Ultrasonic Sensor's ECHO pin

String Zblock,Rblock;


//Provide the token generation process info.
#include <addons/TokenHelper.h>

//Provide the RTDB payload printing info and other helper functions.
#include <addons/RTDBHelper.h>

/* 1. Define the WiFi credentials */
#define WIFI_SSID "TECNO POP 5 Pro"
#define WIFI_PASSWORD "eddy1234"

//For the following credentials, see examples/Authentications/SignInAsUser/EmailPassword/EmailPassword.ino

/* 2. Define the API Key */
#define API_KEY "GFAu9fhLFonsILCriaP5ob6sSmzKvwio99E3cRSL"

/* 3. Define the RTDB URL */
#define DATABASE_URL "https://application-91394-default-rtdb.firebaseio.com/" //<databaseName>.firebaseio.com or <databaseName>.<region>.firebasedatabase.app

//Define Firebase Data object
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

SoftwareSerial GSMSerial(23, 21);
float duration1, distance1,duration2, distance2;


void setup()
{

  Serial.begin(9600);
  GSMSerial.begin(9600);
  GSMSerial.println("AT");
  updateSerial();
  GSMSerial.println("AT+CSQ");
  updateSerial();
  GSMSerial.println("AT+CCID");
  updateSerial();

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  // configure the trigger pin to output mode
  pinMode(TRIG_PIN, OUTPUT);
   pinMode(ECHO_PIN, INPUT);
   
  // configure the echo pin to input mode
 pinMode(TRIG_PIN2, OUTPUT);
  pinMode(ECHO_PIN2, INPUT);

  
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();
  Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION);


  /* Assign the api key (required) */
  config.api_key = API_KEY;
  config.database_url = DATABASE_URL;
  Firebase.begin(DATABASE_URL, API_KEY);
  Firebase.setDoubleDigits(5);
}



void loop()
{
 
 // generate 10-microsecond pulse to TRIG pin
  digitalWrite(TRIG_PIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN, LOW);
  duration1 = pulseIn(ECHO_PIN, HIGH);
  distance1 = 0.017 * duration1;  // calculate the distance

  
  digitalWrite(TRIG_PIN2, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN2, LOW);
  duration2 = pulseIn(ECHO_PIN2, HIGH); // measure duration of pulse from ECHO pin
  distance2 = 0.0171 * duration2;

  
  if (Firebase.ready()) 
  {
    
    //Firebase.setInt(fbdo, main, 5);
    Firebase.setInt(fbdo, "/sensorData/Z Block /wasteLevel", distance1);
    Firebase.setInt(fbdo, "/sensorData/B Block/wasteLevel", distance2);
    delay(200);

  }

  if (distance1 < 8){
     sendSMS();
     delay(10000);
    }

     if (distance2 < 8){
     sendSMS();
     delay(10000);
    }

    
    delay(500);
}


  void sendSMS()
{     delay(50);
      Serial.println("SENDING MESSAGE...");
      GSMSerial.println("AT+CMGF=1"); 
      updateSerial();
      GSMSerial.println("AT+CMGS=\"add your mobile numer\" ");// put mobile number here 
      updateSerial();
      GSMSerial.println("BIN IS FULL,NEEDS TO BE EMPTIED");
      updateSerial();
      GSMSerial.write(26);
      delay(2500);// reduce this delay to receive sms quickly 
      Serial.println("MESSAGE SENT !");
      delay(2500);// reduce this delay to receive sms quickly 
     
   
 }

 void updateSerial(){
    
    delay(500);
   while (Serial.available()){
     GSMSerial.write(Serial.read());
     
    }
     while(GSMSerial.available()){
      Serial.write(GSMSerial.read());
     }
  }

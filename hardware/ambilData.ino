//lib untuk wifi
#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266HTTPClient.h>

//lib untuk MRFC522
#include <SPI.h>
#include <MFRC522.h>

// Gunakan serial sebagai monitor
#define USE_SERIAL Serial

// Buat object Wifi
ESP8266WiFiMulti WiFiMulti;

// Buat object http
HTTPClient http;

String url = "http://172.30.56.173/nodemcu/ambilData.php?nim=";
String payload;

//pin untuk MRFC522
constexpr uint8_t RST_PIN = 5;
constexpr uint8_t SS_PIN = 4;
MFRC522 mfrc522(SS_PIN, RST_PIN);

const char* WIFI_SSID = "WiFi-UB";
const char* NIM = "";
const char* PASS = "";

//var untuk servo (Aktuator Kunci)
Servo myservo;
int pos = 5;

void setup() {

  USE_SERIAL.begin(115200);
  USE_SERIAL.setDebugOutput(false);

  //in untuk servo
  myservo.attach(3);

  SPI.begin();
  mfrc522.PCD_Init();

  WiFi.mode(WIFI_STA);
  WiFiMulti.addAP(WIFI_SSID, NULL);

  while (WiFiMulti.run() != WL_CONNECTED) {
    delay(200);
    Serial.println("Connecting...");
  }

  http.begin("http://nas.ub.ac.id/ac_portal/login.php");
  http.addHeader("Content-Type", "application/x-www-form-urlencoded");
  String kirim = "opr=pwdLogin&userName=";
  kirim += NIM;
  kirim += "&pwd=";
  kirim += PASS;
  kirim += "&rememberPwd=1";
  int httpCode = http.POST(kirim);
  if (httpCode > 0) {
    Serial.println(http.getString());
  }
}

//===============================
// LOOP
//===============================

void loop() {
  // Look for new cards
  if ( ! mfrc522.PICC_IsNewCardPresent()) {
    return;
  }
  // Select one of the cards
  if ( ! mfrc522.PICC_ReadCardSerial()) {
    return;
  }

  //pembacaan UID
  Serial.print("UID tag :");
  String content = "";
  String kirim = "";
  byte letter;
  for (byte i = 0; i < mfrc522.uid.size; i++)
  {
    Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
    Serial.print(mfrc522.uid.uidByte[i], HEX);
    content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
    content.concat(String(mfrc522.uid.uidByte[i], HEX));
  }
  Serial.println();
  Serial.print("Message : ");
  content.toUpperCase();
  kirim = content.substring(1);
  kirim.replace(" ", "-");
  Serial.println(kirim);

  // Cek apakah statusnya sudah terhubung
  if ((WiFiMulti.run() == WL_CONNECTED)) {

    // Tambahkan nilai suhu pada URL yang sudah kita buat
    USE_SERIAL.print("[HTTP] Memulai...\n");
    http.begin( url + kirim );

    // Mulai koneksi dengan metode GET
    USE_SERIAL.print("[HTTP] Melakukan GET ke server...\n");
    int httpCode = http.GET();

    payload = http.getString();
    Serial.println(payload);
    if (payload == "tidakAdaBerow ") {
      Serial.println("tidak ada");
    } else {
      Serial.println("ada");
      
      //servo gerak
      for (pos = 5; pos <= 130; pos += 1) {
        myservo.write(pos);
      }
      delay(3000);
      for (pos = 130; pos >= 5; pos -= 1) {
        myservo.write(pos);
      };
    }

    http.end();
  }

  delay(5000);
}

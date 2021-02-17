#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>
#include <EEPROM.h>
#define WiFiBtn  D7
#define Name "nodeMCU_"
char WiFiID[20];
char Key[20];
int countForConn=0;
bool late_connection=false;
const String serviceName="http"; 
ESP8266WebServer httpServer(80);

void setup() {
  Serial.begin(115200);
  EEPROM.begin(512);
  int WIFIlen =EEPROM.read(0);//Lenghth of SSID stored Here
  int PASSlen =EEPROM.read(1);//Lenghth of PASSWORD stored Here
  int wr=2;//SSID Max length 2nd byte to 24th byte - You can Change this as per your need
  for(int c=0; c<WIFIlen; c++){
    WiFiID[c]=EEPROM.read(wr);
    wr++;
  }
  wr=25;//PASSWORD Max Length 25th byte to 
  for(int c=0; c<PASSlen; c++){
    Key[c]=EEPROM.read(wr);
    wr++;
  }
  Serial.println(WiFiID);
  Serial.println(Key);
  pinMode(WiFiBtn ,INPUT_PULLUP);
  pinMode(D0, OUTPUT);
  pinMode(D1, OUTPUT);
  pinMode(D2, OUTPUT);
  pinMode(D3, OUTPUT);
  pinMode(D4, OUTPUT);
  pinMode(D5, OUTPUT);
  pinMode(D8, OUTPUT);
  
  WiFi.mode(WIFI_STA);
  WiFi.begin(WiFiID,Key);
  int blnk=0;
  while (WiFi.status() != WL_CONNECTED){
    countForConn++;
    delay(500);
    digitalWrite(LED_BUILTIN,blnk);
    blnk=!blnk;
   if(countForConn == 60){//30seconds-waits for connecting
      countForConn=0;
      late_connection=true;
      break;
    }
  }
  if(!late_connection){
    MDNS.begin(Name+String(ESP.getChipId(),HEX));
    httpServer.begin();
    MDNS.addService(serviceName, "tcp", 80);
    Serial.println(WiFi.localIP());
    }else{
      WiFi.disconnect();//Disconnect to previously connected WiFi
      //delay(1000);
      WiFi.softAP(Name+String(ESP.getChipId(),HEX));
      delay(100);
      MDNS.begin(Name+String(ESP.getChipId(),HEX));  
      httpServer.begin();
      MDNS.addService(serviceName, "tcp", 80);
      Serial.println(WiFi.localIP());
      }
  digitalWrite(LED_BUILTIN,LOW);

  //Commands and replay
  httpServer.on("/d0H", []() {httpServer.send(200, "text/plain", "DO-SET-HIGH"); digitalWrite(D0,HIGH);});
  httpServer.on("/d1H", []() {httpServer.send(200, "text/plain", "D1-SET-HIGH"); digitalWrite(D1,HIGH);});
  httpServer.on("/d2H", []() {httpServer.send(200, "text/plain", "D2-SET-HIGH"); digitalWrite(D2,HIGH);});
  httpServer.on("/d3H", []() {httpServer.send(200, "text/plain", "D3-SET-HIGH"); digitalWrite(D3,HIGH);});
  httpServer.on("/d4H", []() {httpServer.send(200, "text/plain", "D4-SET-HIGH"); digitalWrite(D4,HIGH);});
  httpServer.on("/d5H", []() {httpServer.send(200, "text/plain", "D5-SET-HIGH"); digitalWrite(D5,HIGH);});
  httpServer.on("/d6H", []() {httpServer.send(200, "text/plain", "D6-SET-HIGH"); digitalWrite(D6,HIGH);});
  //httpServer.on("/d7H", []() {httpServer.send(200, "text/plain", "D7-SET-HIGH"); digitalWrite(D7,HIGH);}); //D7 used for WiFi Reset Button.
  httpServer.on("/d8H", []() {httpServer.send(200, "text/plain", "D8-SET-HIGH"); digitalWrite(D8,HIGH);});

  httpServer.on("/d0L", []() {httpServer.send(200, "text/plain", "D0-SET-LOW"); digitalWrite(D0,LOW);});
  httpServer.on("/d1L", []() {httpServer.send(200, "text/plain", "D1-SET-LOW"); digitalWrite(D1,LOW);});
  httpServer.on("/d2L", []() {httpServer.send(200, "text/plain", "D2-SET-LOW"); digitalWrite(D2,LOW);});
  httpServer.on("/d3L", []() {httpServer.send(200, "text/plain", "D3-SET-LOW"); digitalWrite(D3,LOW);});
  httpServer.on("/d4L", []() {httpServer.send(200, "text/plain", "D4-SET-LOW"); digitalWrite(D4,LOW);});
  httpServer.on("/d5L", []() {httpServer.send(200, "text/plain", "D5-SET-LOW"); digitalWrite(D5,LOW);});
  httpServer.on("/d6L", []() {httpServer.send(200, "text/plain", "D6-SET-LOW"); digitalWrite(D6,LOW);});
  //httpServer.on("/d7L", []() {httpServer.send(200, "text/plain", "D7-SET-LOW"); digitalWrite(D7,LOW);});
  httpServer.on("/d8L", []() {httpServer.send(200, "text/plain", "D8-SET-LOW"); digitalWrite(D8,LOW);});
  
  //handle aything Else requested including WiFi Credentials
  httpServer.onNotFound(handleElse); 
}
void handleElse() {
  String dat = httpServer.uri();
  Serial.println(dat);
  if(dat.substring(1,3)=="WC"){
    Serial.println(dat.substring(3));
    setWiFiCredentials(dat.substring(3));
    
    }else{
      httpServer.send(200, "text/plain", dat+"-Not Found");
      }
}
void setWiFiCredentials(String creds){
  httpServer.send(200, "text/plain", "done");
  delay(1000);
  String Credentials =creds;
  String WIFI="",PASS ="", Remaining="";
  for (int i = 0; i < Credentials.length(); i++) {
    if (Credentials.substring(i, i+1) == ",") {
      WIFI = Credentials.substring(0, i);
      Remaining = Credentials.substring(i+1);
      break;
      }
    }
  for (int i = 0; i < Remaining.length(); i++) {
    if (Remaining.substring(i, i+1) == ",") {
      PASS = Remaining.substring(0, i);
      String remi = Credentials.substring(i+1);
      break;
      }
    }
    EEPROM.write(0,WIFI.length()); // Write WiFi ID length to EEPROM 
    EEPROM.write(1,PASS.length()); // Write WiFi Password length to EEPROM
    int wr=2;
    for(int c=0;c<WIFI.length();c++){
      EEPROM.write(wr,WIFI[c]);   //Writing WIFI SSID to EEPROM
      wr++;
    }
    wr=25;
    for(int c=0;c<PASS.length();c++){
      EEPROM.write(wr,PASS[c]);  //Writing WIFI PASSWORD to EEPROM
      wr++;
    }
    EEPROM.commit();
    delay(10);
    ESP.restart(); // restarts ESP - if Credentials Wrong - It starts AP again..
  }
void loop() {
  MDNS.update();
  httpServer.handleClient();
  delay(1);
  if(!digitalRead(WiFiBtn)){
    delay(1000);
    if(!digitalRead(WiFiBtn)){  
    MDNS.end();
    WiFi.disconnect();
    Serial.println("WIFI RST BTN PRESSED");
    digitalWrite(LED_BUILTIN,LOW);
    delay(50);
    digitalWrite(LED_BUILTIN,HIGH);
    delay(100);
    digitalWrite(LED_BUILTIN,LOW);
    delay(50);
    digitalWrite(LED_BUILTIN,HIGH);
    delay(100);
    digitalWrite(LED_BUILTIN,LOW);
    WiFi.softAP(Name+String(ESP.getChipId(),HEX));
    delay(100);
    MDNS.begin("STCT_"+String(ESP.getChipId(),HEX));
    httpServer.begin();
    MDNS.addService(serviceName, "tcp", 80);
    }
  } 
}
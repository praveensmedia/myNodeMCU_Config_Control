#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>
#include <EEPROM.h>
#include <Ticker.h> // Ticker Function is very useful trigger functions timely, like blink without delay
#define WiFiBtn D7 //You can start softAP by making D7 GND (1sec)untill IN_BUILTLED Blink twice.
#define Name "nodeMCU_"
char WiFiID[20];
char Key[20];
int countForConn = 0;
int firstTimeCheck = 243;   //This is to store at address 100
int ConnectionTimeOut = 60; //increse this If You have a Lazy WiFi Router. 60 means 30seconds
bool late_connection = false;
const String serviceName = "http";
ESP8266WebServer httpServer(80);
Ticker blinker;
bool blk =false;
void setup()
{
  Serial.begin(115200);
  EEPROM.begin(512);

  int firstTimeInt = EEPROM.read(100); // At address 100
  if (firstTimeCheck != firstTimeInt)
  {              //Below Code runs only once
    int bb1 = 0; //This is very important if you never used your EEPROM,
    while (bb1 < 512)
    { //Otherwise your device will restart--may ran into Errors
      EEPROM.write(bb1, 0);
      bb1++;
    }
    EEPROM.write(100, firstTimeCheck);
    Serial.println("FIRST-TIME RUN");
  }
  EEPROM.commit();
  int WIFIlen = EEPROM.read(0); //Lenghth of SSID stored Here
  int PASSlen = EEPROM.read(1); //Lenghth of PASSWORD stored Here
  int wr = 2;                   //SSID Max length 2nd byte to 24th byte - You can Change this as per your need
  for (int c = 0; c < WIFIlen; c++)
  {
    WiFiID[c] = EEPROM.read(wr);
    wr++;
  }
  wr = 25; //PASSWORD Max Length 25th byte to
  for (int c = 0; c < PASSlen; c++)
  {
    Key[c] = EEPROM.read(wr);
    wr++;
  }
  Serial.println(WiFiID); // These may print Empty or Garbage For First Time
  Serial.println(Key);
  pinMode(WiFiBtn, INPUT_PULLUP);
  pinMode(D0, OUTPUT); //In nodeMCU D0 has InBuilt LED near Programmer IC(CP2102..)
  pinMode(D1, OUTPUT);
  pinMode(D2, OUTPUT);
  pinMode(D3, OUTPUT);
  pinMode(LED_BUILTIN, OUTPUT); //D4 is LED_BUILTIN
  pinMode(D5, OUTPUT);
  pinMode(D6, OUTPUT);
  pinMode(D8, OUTPUT);

  WiFi.mode(WIFI_STA);
  WiFi.begin(WiFiID, Key);
  int blnk = 0;
  while (WiFi.status() != WL_CONNECTED)
  {
    countForConn++;
    delay(500);
    digitalWrite(LED_BUILTIN, blnk);
    blnk = !blnk;
    if (countForConn == ConnectionTimeOut)
    {
      countForConn = 0;
      late_connection = true; // you can handle late connection for Lazy Routers- code will be Added if anyone Interested
      break;
    }
  }
  if (!late_connection)
  { // if connected to WiFi Do this
    MDNS.begin(Name + String(ESP.getChipId(), HEX));
    httpServer.begin();
    MDNS.addService(serviceName, "tcp", 80);
    Serial.println(WiFi.localIP());
  }
  else
  { // if not connected to WiFi Do this
    startSoftAP();
  }
  digitalWrite(LED_BUILTIN, LOW);

  //Commands and replay-'I recommend you give response to every command you hit to NodeMCU'
  httpServer.on("/d0H", /* <-You can make your own commands*/ []() {
    httpServer.send(200, "text/plain", "DO-SET-HIGH"); 
    /* <-You can do whatever you want*/ // I don't recommed you to run 'delay()' in here..
    digitalWrite(D0,HIGH); });

  httpServer.on("/d1H", []() {httpServer.send(200, "text/plain", "D1-SET-HIGH"); digitalWrite(D1,HIGH); });
  httpServer.on("/d2H", []() {httpServer.send(200, "text/plain", "D2-SET-HIGH"); digitalWrite(D2,HIGH); });
  httpServer.on("/d3H", []() {httpServer.send(200, "text/plain", "D3-SET-HIGH"); digitalWrite(D3,HIGH); });
  httpServer.on("/d4H", []() {httpServer.send(200, "text/plain", "D4-SET-HIGH"); digitalWrite(LED_BUILTIN, HIGH);}); //Used for Blinking with out delay
  httpServer.on("/d5H", []() {httpServer.send(200, "text/plain", "D5-SET-HIGH"); digitalWrite(D5,HIGH); });
  httpServer.on("/d6H", []() {httpServer.send(200, "text/plain", "D6-SET-HIGH"); digitalWrite(D6,HIGH); });
  httpServer.on("/d7H", []() {httpServer.send(200, "text/plain", "D7-SET-HIGH");}); //D7 used for WiFi Reset Button.
  httpServer.on("/d8H", []() {httpServer.send(200, "text/plain", "D8-SET-HIGH"); digitalWrite(D8,HIGH); });

  httpServer.on("/d0L", []() {httpServer.send(200, "text/plain", "D0-SET-LOW"); digitalWrite(D0,LOW); });
  httpServer.on("/d1L", []() {httpServer.send(200, "text/plain", "D1-SET-LOW"); digitalWrite(D1,LOW); });
  httpServer.on("/d2L", []() {httpServer.send(200, "text/plain", "D2-SET-LOW"); digitalWrite(D2,LOW); });
  httpServer.on("/d3L", []() {httpServer.send(200, "text/plain", "D3-SET-LOW"); digitalWrite(D3,LOW); });
  httpServer.on("/d4L", []() {httpServer.send(200, "text/plain", "D4-SET-LOW"); digitalWrite(LED_BUILTIN, LOW);});
  httpServer.on("/d5L", []() {httpServer.send(200, "text/plain", "D5-SET-LOW"); digitalWrite(D5,LOW); });
  httpServer.on("/d6L", []() {httpServer.send(200, "text/plain", "D6-SET-LOW"); digitalWrite(D6,LOW); });
  httpServer.on("/d7L", []() {httpServer.send(200, "text/plain", "D7-SET-LOW");});
  httpServer.on("/d8L", []() {httpServer.send(200, "text/plain", "D8-SET-LOW"); digitalWrite(D8,LOW); });

  //handle aything Else requested including WiFi Credentials
  httpServer.onNotFound(handleElse);
}

void blinkFun()
{
  digitalWrite(LED_BUILTIN, blk);
  blk = !blk;
  }

void handleElse()
{
  String dat = httpServer.uri();
  Serial.println(dat);
  if (dat.substring(1, 3) == "WC")
  {
    Serial.println(dat.substring(3));
    setWiFiCredentials("WiFi Cred-Set: "+dat.substring(3));
  }else if(dat.substring(1, 3) == "A0")
  {
    Serial.println("A0-Set: "+dat.substring(3));
    httpServer.send(200, "text/plain", "A0-Set: " +dat.substring(3));
    int num= (dat.substring(3)).toInt();
    float dly =num/1000.0; 
    blinker.attach(dly,blinkFun);
    
  }else if(dat.substring(1, 3) == "A1")
  {
    Serial.println("A1-Set: "+dat.substring(3));
    httpServer.send(200, "text/plain", "A1-Set: " +dat.substring(3));
  }else if(dat.substring(1, 3) == "A2")
  {
    Serial.println("A2-Set: "+dat.substring(3));
    httpServer.send(200, "text/plain", "A2-Set: " +dat.substring(3));
  }
}
void startSoftAP()
{
  WiFi.disconnect(); //Disconnect to previously connected WiFi
  //delay(1000);
  WiFi.softAP(Name + String(ESP.getChipId(), HEX));
  delay(100);
  MDNS.begin(Name + String(ESP.getChipId(), HEX));
  httpServer.begin();
  MDNS.addService(serviceName, "tcp", 80);
  Serial.println(WiFi.localIP());
}
void setWiFiCredentials(String creds)
{
  httpServer.send(200, "text/plain", "done");
  delay(1000);
  String Credentials = creds;
  String WIFI = "", PASS = "", Remaining = "";
  for (int i = 0; i < Credentials.length(); i++)
  {
    if (Credentials.substring(i, i + 1) == ",")
    {
      WIFI = Credentials.substring(0, i);
      Remaining = Credentials.substring(i + 1);
      break;
    }
  }
  for (int i = 0; i < Remaining.length(); i++)
  {
    if (Remaining.substring(i, i + 1) == ",")
    {
      PASS = Remaining.substring(0, i);
      String remi = Credentials.substring(i + 1);
      break;
    }
  }
  EEPROM.write(0, WIFI.length()); // Write WiFi ID length to EEPROM
  EEPROM.write(1, PASS.length()); // Write WiFi Password length to EEPROM
  int wr = 2;
  for (int c = 0; c < WIFI.length(); c++)
  {
    EEPROM.write(wr, WIFI[c]); //Writing WIFI SSID to EEPROM
    wr++;
  }
  wr = 25;
  for (int c = 0; c < PASS.length(); c++)
  {
    EEPROM.write(wr, PASS[c]); //Writing WIFI PASSWORD to EEPROM
    wr++;
  }
  EEPROM.commit();
  delay(10);
  ESP.restart(); // restarts ESP - if Credentials Wrong - It starts AP again..
}
void loop()
{
  MDNS.update();
  httpServer.handleClient();
  delay(1);
  if (!digitalRead(WiFiBtn))
  {
    delay(1000);
    if (!digitalRead(WiFiBtn))
    {
      Serial.println("WIFI RST BTN PRESSED");
      MDNS.end();
      startSoftAP();
      digitalWrite(LED_BUILTIN, LOW);
      delay(50);
      digitalWrite(LED_BUILTIN, HIGH);
      delay(100);
      digitalWrite(LED_BUILTIN, LOW);
      delay(50);
      digitalWrite(LED_BUILTIN, HIGH);
      delay(100);
      digitalWrite(LED_BUILTIN, LOW);
    }
  }
  //Do whatever you want Here...
}

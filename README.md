# myNodeMCU_Config_Control
Anyone can use these files. This is the Help page for myNodeMCU_Config_Control App on Android PlayStore.
1. You Can Download the App Now from playStore [ClickHere](https://play.google.com/store/apps/details?id=com.praveensmedia.mynodemcuconfig_control)
2. You can Get IP,PORT,ServiceName and ServiceType of any Service available In your Network.
3. Please clone [myNodeMCU_Config_Control_Example.ino](https://github.com/praveensmedia/myNodeMCU_Config_Control/blob/main/myNodeMCU_Config_Control_Example.ino)
4. Support Libraries:
5. Compile & Upload to Your NodeMCU(ESP8266) as it is. do not change anything in the code for first time to test communication b/w App & NodeMCU
6. After Uploading nodeMCU will blinks for 30 seconds(waits for the connection). There is no wifi credentials preloaded.
7. Then it Starts a softAP named myNodeMCU-(chipID), if you don't See softAP you can turn it ON by Making D7 GND Untill INBUIL_IN_LED blink twise. 
8. Connect your phone to above softAP, so that App Can find Its IP, further instrustions below..

## Service_Discovery-App_Home_Screen
1. By default '_http._tcp.' Services Only Discoverd on Your loacal network.
2. By Switching AllServices, All type of services will be discovered.
3. By Clicking On Discovered Services you can get its IP, PORT, ServiceName and ServiceType.
4. When you click on Discovered service that IP will be selected to Use in Control and Config screens of the myNodeMCU_Config_Control App
5. You can Enter Your own IP Address when you dont have services/you dont want to choose one of them, to go to Control/Config screens of myNodeMCU_Config_Control App
6. For Updating Credentials to nodeMCU first time, You must Connect to Its softAP to get its IP then only goto App_Config_Screen.
 
## Test_NodeMCU-App_Controls_Screen
1. You must selected a service to get IP in Home_Screeen. then only proceed to this screen.
2. By Default Buttons assigned to Trigger HIGH OR LOW to Digital pins of nodeMCU.
3. You can get Response from your device(if you write repose for it in your code)

## Update_Credentials-App_Config_Screen
1. You must selected a service to get IP in Home_Screeen. then only proceed to this screen.
2. just Enter SSID and Password in their fields. press UPDATE CREDENTIALS button. thats it. if Your Credentials Wrong Or That Wifi IS not Available, again it starts softAP.
3. it will make url: http://ipadress/WCwifissid,password,  'WC' is to recognise WifiCredentials Are Coming From APP. 
4. Once you pressed 'UPDATE CREDENTIALS' button, nodeMCU will be Restarted.

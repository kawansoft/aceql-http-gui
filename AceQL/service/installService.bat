@echo off
start /min AceQLHTTPService.exe //IS//AceQLHTTPService  ^
 --DisplayName="AceQL HTTP Server" ^
 --Install="%CD%"\AceQLHTTPService.exe ^
 --Description="AceQL HTTP Server - https://www.aceql.com" ^
 --Jvm=auto ^
 --JvmOptions=-Xrs;-Xms128m;-Xmx256m;-Duser.dir="%CD%"\..\..\ ^
 --Classpath="%CD%\..\..\aceql-http-gui-1.0-beta-1.jar";"%CD%\..\..\dependency/*";"%CD%\..\..\AceQL\lib-jdbc/*";"%CLASSPATH%" ^
 --StartMode=jvm ^
 --StartClass=com.kawansoft.aceql.gui.service.AceQLServiceControler ^
 ++StartParams=%1 ^
 --StartMethod=start ^
 --StopMode=jvm ^
 --StopClass=com.kawansoft.aceql.gui.service.AceQLServiceControler ^
 --StopMethod=stop ^
 --LogPath=%2 ^
 --StdOutput=auto ^
 --StdError=auto ^
 --Startup=auto ^
 --ServiceUser=System ^ 
 exit
@echo off
set USER_DIR=%1
start /min AceQLHTTPService.exe //IS//AceQLHTTPService  ^
 --DisplayName="AceQL HTTP Server" ^
 --Install="%CD%"\AceQLHTTPService.exe ^
 --Description="AceQL HTTP Server - https://www.aceql.com" ^
 --Jvm=auto ^
 --JvmOptions=-Xrs;-Xms128m;-Xmx256m;-Duser.dir="%USER_DIR%" ^
 --Classpath="%USER_DIR%\aceql-http-gui-1.0-beta-1.jar";"%USER_DIR%\dependency/*";"%USER_DIR%\AceQL\lib-jdbc/*";"%CLASSPATH%" ^
 --StartMode=jvm ^
 --StartClass=com.kawansoft.aceql.gui.service.AceQLServiceControler ^
 ++StartParams=%2 ^
 --StartMethod=start ^
 --StopMode=jvm ^
 --StopClass=com.kawansoft.aceql.gui.service.AceQLServiceControler ^
 --StopMethod=stop ^
 --LogPath=%3 ^
 --StdOutput=auto ^
 --StdError=auto ^
 --Startup=auto ^
 --ServiceUser=System ^ 
 exit
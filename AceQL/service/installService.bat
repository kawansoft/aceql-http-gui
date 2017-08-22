@echo off
REM SET SERVICE_USER_DIR=%1
start /min AceQLHTTPService.exe //IS//AceQLHTTPService  ^
 --DisplayName="AceQL HTTP Server" ^
 --Install="%CD%"\AceQLHTTPService.exe ^
 --Description="AceQL HTTP Server - SQL Over HTTP - https://www.aceql.com" ^
 --Jvm=auto ^
 --JvmOptions=-Xrs;-Xms128m;-Xmx256m;-Duser.dir=%1;-Duser.home=%2 ^
 --Classpath=%1\AceQL\lib-server\*;%1\AceQL\lib-jdbc\*;%CLASSPATH% ^
 --StartMode=jvm ^
 --StartClass=com.kawansoft.aceql.gui.service.AceQLServiceControler ^
 --StartMethod=start ^
 --StopMode=jvm ^
 --StopClass=com.kawansoft.aceql.gui.service.AceQLServiceControler ^
 --StopMethod=stop ^
 --LogPath=%2\windows-service-logs ^
 --StdOutput=auto ^
 --StdError=auto ^
 --Startup=auto ^
 --ServiceUser=System ^ 
 exit
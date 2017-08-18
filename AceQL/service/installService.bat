@echo off
start /min AceQLHTTPService.exe //IS//AceQLHTTPService  ^
 --DisplayName="AceQL HTTP Server" ^
 --Install="%CD%"\AceQLHTTPService.exe ^
--Description="AceQL HTTP Server - SQL Over HTTP - https://www.aceql.com" ^
 --Jvm=auto ^
 --JvmOptions=-Xrs;-Xms128m;-Xmx256m;-Duser.dir="%CD%"\..\..\ ^
 --Classpath="%CD%\..\lib-server/*";"%CD%\..\lib-jdbc/*";"%CLASSPATH%" ^
 --StartMode=jvm ^
 --StartClass=com.kawansoft.aceql.gui.service.AceQLServiceControler ^
 --StartMethod=start ^
 --StopMode=jvm ^
 --StopClass=com.kawansoft.aceql.gui.service.AceQLServiceControler ^
 --StopMethod=stop ^
 --LogPath=%1 ^
 --StdOutput=auto ^
 --StdError=auto ^
 --Startup=auto ^
 --ServiceUser=System ^ 
 exit
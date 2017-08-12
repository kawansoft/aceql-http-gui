@echo off
start /min AceQLHTTPService.exe //IS//AceQLHTTPService  ^
 --DisplayName="AceQL HTTP Server" ^
 --Description="AceQL HTTP Server - https://www.aceql.com" ^
 --Install="%CD%"\AceQLHTTPService.exe ^
 --Jvm=auto ^
 --JvmOptions=-Xrs;-Xms128m;-Xmx256m;-Duser.dir=%1 ^
 --Classpath=%2 ^
 --StartMode=jvm ^
 --StartClass=com.kawansoft.aceql.gui.service.AceQLServiceControler ^
 ++StartParams=%3 ^
 --StartMethod=start ^
 --StopMode=jvm ^
 --StopClass=com.kawansoft.aceql.gui.service.AceQLServiceControler ^
 --StopMethod=stop ^
 --LogPath=%4 ^
 --StdOutput=auto ^
 --StdError=auto ^
 --Startup=auto ^
 --ServiceUser=System ^ 
 exit
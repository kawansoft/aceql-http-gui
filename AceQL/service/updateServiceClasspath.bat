@echo off
start /min AceQLHTTPService.exe //US//AceQLHTTPService  ^
 --Description="AceQL HTTP Server - SQL Over HTTP - https://www.aceql.com" ^
 --Classpath=%1\AceQL\lib-server\*;%1\AceQL\lib-jdbc\*;%CLASSPATH% ^
exit



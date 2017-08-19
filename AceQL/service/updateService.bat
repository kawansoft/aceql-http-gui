@echo off
start /min AceQLHTTPService.exe //US//AceQLHTTPService  ^
 --Description="AceQL HTTP Server - SQL Over HTTP - https://www.aceql.com" ^
 --Classpath="%CD%\..\lib-server/*";"%CD%\..\lib-jdbc/*";%CLASSPATH% ^
exit



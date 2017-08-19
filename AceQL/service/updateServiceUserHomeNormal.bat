@echo off
start /min AceQLHTTPService.exe //US//AceQLHTTPService  ^
 --JvmOptions=-Xrs;-Xms128m;-Xmx256m;-Duser.dir=%1
exit



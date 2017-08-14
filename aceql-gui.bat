set ACEQL_HOME="I:\_dev_awake\aceql-http-main\aceql-http-gui\target"
java -Xms128m -Xmx256m -classpath %ACEQL_HOME%\aceql-http-gui-1.0-beta-1.jar;"%ACEQL_HOME%\dependency/*";"%ACEQL_HOME%\AceQL\lib-jdbc/*";%CLASSPATH% com.kawansoft.aceql.gui.AppTray

REM set ACEQL_HOME=I:\_dev_awake\aceql-http-main\aceql-http-gui\install
REM java -Xms128m -Xmx256m -classpath "%ACEQL_HOME%\lib-server/*";"%ACEQL_HOME%\lib-jdbc/*" com.kawansoft.aceql.gui.AppTray

java -Xms128m -Xmx256m -classpath "%CD%\AceQL\lib-server\*";"%CD%\AceQL\lib-jdbc\*";"%CLASSPATH%" com.kawansoft.aceql.gui.AppTray

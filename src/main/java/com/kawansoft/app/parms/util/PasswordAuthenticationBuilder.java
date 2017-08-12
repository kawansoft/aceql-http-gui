/**
 * 
 */
package com.kawansoft.app.parms.util;

import com.kawansoft.app.util.preference.AppPreferencesManager;
import com.kawansoft.app.util.proxy.DialogProxyAuth;
import java.awt.Cursor;
import java.awt.Window;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;


/**
 * 
 * Class to build a PasswordAuthentication instance for a Proxy if
 * it require authentication
 * 
 * @author Nicolas de Pomereu
 *
 */
public class PasswordAuthenticationBuilder {

    private Proxy proxy = null;
    private Window parentWindow = null;

    private PasswordAuthentication passwordAuthentication = null;
    private int responseCode;
    private String responseMessage;
    
    
    private final AppPreferencesManager appUserPreference;
    

    /**
     * Constructor
     * 
     * @param proxy
     * @param appUserPreference the value of appUserPreference
     * @param parentWindow
     */
    public PasswordAuthenticationBuilder(Proxy proxy, AppPreferencesManager appUserPreference, Window parentWindow) {
	super();
	this.proxy = proxy;
	this.parentWindow = parentWindow;
        this.appUserPreference = appUserPreference;
    }

    /**
     * Build the passwordAuthentication. Will return null if no authentication is required or if
     * url tested returns an Http response code <> HttpURLConnection.HTTP_OK (200).
     * <br>
     * So, test mut be done if returned PasswordAuthentication is null on HTTP response message with getResponseCode() 
     * that must return HttpURLConnection.HTTP_OK
     * @return passwordAuthentication s
     * @throws IOException 
     */
    public PasswordAuthentication build() throws IOException {

	responseCode = HttpURLConnection.HTTP_OK;

        // No authentication required if proxy is null
        if (proxy == null) {
            responseCode = HttpURLConnection.HTTP_OK;
            return passwordAuthentication;
        }
        
	while (true) {
	    parentWindow.setCursor(
		    Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	    
	   testProxy("http://www.google.com", proxy,
		    passwordAuthentication);

	    //debug("responseCode   : " + responseCode);
            //debug("responseMessage: " + responseMessage);
	   
	    if (responseCode == HttpURLConnection.HTTP_OK) {
                parentWindow.setCursor(Cursor.getDefaultCursor());
		return passwordAuthentication;
	    } else if (responseCode == HttpURLConnection.HTTP_PROXY_AUTH) {
                parentWindow.setCursor(Cursor.getDefaultCursor());
		DialogProxyAuth dialogProxyAuth = new DialogProxyAuth(
			parentWindow, appUserPreference);
		dialogProxyAuth.setVisible(true);
		if (dialogProxyAuth.isCancelled()) {
		    parentWindow.setCursor(
			    Cursor.getDefaultCursor());
		    return null;
		}
		passwordAuthentication = new PasswordAuthentication(
			(dialogProxyAuth.getProxyUsername()), dialogProxyAuth
				.getProxyPassword().toCharArray());
		continue; // We loop until responseCode == HttpURLConnection.HTTP_OK or user hits cancel in DialogProxyAuth
	    } else {
		parentWindow.setCursor(Cursor.getDefaultCursor());
		return passwordAuthentication;
	    }
	}

    }

    
  
    /**
     * @return the responseCode
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * @return the responseMessage
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * Test the proxy on an URL.
     *
     * @param url
     * @param proxy
     * @param passwordAuthentication
     */
    private void testProxy(String url, Proxy proxy,
	    PasswordAuthentication passwordAuthentication) throws IOException {

	setProxyCredentials(proxy, passwordAuthentication);

	URL theUrl = new URL(url);
	HttpURLConnection conn = null;

	if (proxy == null) {
	    conn = (HttpURLConnection) theUrl.openConnection();
	} else {
	    conn = (HttpURLConnection) theUrl.openConnection(proxy);
	}

	responseCode = conn.getResponseCode();
	responseMessage = conn.getResponseMessage();

    }

    public static void setProxyCredentials(Proxy proxy,
	    PasswordAuthentication passwordAuthentication) {

	if (proxy == null) {
	    Authenticator.setDefault(null);
	    return;
	}

	// Sets the credential for authentication
	if (passwordAuthentication != null) {
	    final String proxyAuthUsername = passwordAuthentication
		    .getUserName();
	    final char[] proxyPassword = passwordAuthentication.getPassword();

	    Authenticator authenticator = new Authenticator() {

		public PasswordAuthentication getPasswordAuthentication() {
		    return new PasswordAuthentication(proxyAuthUsername,
			    proxyPassword);
		}
	    };

	    Authenticator.setDefault(authenticator);
	} else {
	    Authenticator.setDefault(null);
	}
    }

}

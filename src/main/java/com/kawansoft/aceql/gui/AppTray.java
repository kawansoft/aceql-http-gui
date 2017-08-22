
package com.kawansoft.aceql.gui;

/*
 * Copyright (C) 2004 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */
import static com.kawansoft.aceql.gui.AceQLManager.setLookAndFeel;
import com.kawansoft.aceql.gui.util.UserPreferencesManager;
import com.kawansoft.app.parms.MessagesManager;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.SystemUtils;

import com.kawansoft.app.parms.Parms;
import com.kawansoft.app.parms.util.ImageParmsUtil;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Https Uploader Tray Launcher	
 * <br>
 * Laucnhes the JFrame UploaderMain
 */
public class AppTray { 

    /**
     * The debug flag
     */
    protected static boolean DEBUG = false;

    /**
     * The System Tray
     */
    private SystemTray tray = SystemTray.getSystemTray();
    /**
     * The Tray Icon
     */
    public TrayIcon trayIcon;
    /**
     * The Main Window
     */
    private AceQLManager aceQLManager = null;

    AboutFrame aboutFrame = null;
                    
    /**
     * Constructor.
     *
     * Will launch Https AceQL HTTP Server Manager Main Window and the Tray
     */
    public AppTray() {
    }

    private void setVisibleAndOnTopOneSecond() throws SecurityException {
        aceQLManager.setState(Frame.NORMAL);
        aceQLManager.setVisible(true);

        if (SystemUtils.IS_OS_MAC_OSX) {
            aceQLManager.setVisible(false);
            aceQLManager.setAlwaysOnTop(false);
            aceQLManager.setAlwaysOnTop(true);
            aceQLManager.setVisible(true);

            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        aceQLManager.setAlwaysOnTop(false);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AppTray.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            t.start();
        }
    }
  
    /**
     * Start Https Uploader as Tray
     *
     * @param args args passed to jar
     */
    public void startAsTray(String[] args) {

        PopupMenu menu;
        MenuItem menuItem;

        if (Integer.parseInt(System.getProperty("java.version").substring(2, 3)) >= 5) {
            System.setProperty("javax.swing.adjustPopupLocationToFit", "false");
        }

        /*
         Main Window
         Exit
         ____________
         About
         */
        menu = new PopupMenu(Parms.APP_NAME);

        // JMenuItems
        menuItem = new MenuItem("Show " + Parms.APP_NAME );

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               setVisibleAndOnTopOneSecond();
            }
        });

        menu.add(menuItem);

        //Font font = menuItem.getFont().deriveFont(Font.BOLD);
        //menuItem.setFont(font);

        // "Exit" menu item
        menuItem = new MenuItem("Quit");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(menuItem);
        
        menu.addSeparator();
                
        menuItem = new MenuItem("About");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (aboutFrame != null) {
                    aboutFrame.dispose();
                }

                aboutFrame = new AboutFrame(null);
            }
        });
        menu.add(menuItem);
               
        ImageIcon i = new ImageIcon(ImageParmsUtil.getTrayIcon());

        trayIcon = new TrayIcon(i.getImage(), Parms.APP_NAME, menu);
        trayIcon.setImageAutoSize(true);

        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleAndOnTopOneSecond();
            }

        });
        
        try {
            tray.add(trayIcon);
        } catch (AWTException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(aceQLManager, "Impossible to start " + Parms.APP_NAME + " : " + ex.toString());
        }

        UserPreferencesManager userPreferencesManager = new UserPreferencesManager();
        if (! userPreferencesManager.getBooleanPreference("DISPLAY_TRAY_MESSAGE_DONE")) {
            trayIcon.displayMessage(null, MessagesManager.get("click_the_icon_to_access") + " " + Parms.APP_NAME + " " + MessagesManager.get("when_the_window_is_closed"), TrayIcon.MessageType.INFO);
            userPreferencesManager.setPreference("DISPLAY_TRAY_MESSAGE_DONE", true);
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                aceQLManager = new AceQLManager();
                aceQLManager.setVisible(true);
            }
        });
        

    }


    /**
     * MAIN
     *
     * @param args
     */
    public static void main(String[] args) {

        try {
                            
            setLookAndFeel();
            
            if (SystemTray.isSupported()) {
                AppTray appTray = new AppTray();
                appTray.startAsTray(args);
            }
            else {
                AceQLManager aceQLManager = new AceQLManager();
                aceQLManager.setVisible(true);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
           JOptionPane.showMessageDialog(null, "Start Exception: " + ex.toString(), Parms.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * For Advanced Installer
     *
     * @param args
     */
    static void secondaryMain(String args[]) {

    }

    /**
     * debug tool
     */
    private static void debug(String s) {
        if (DEBUG) {
            System.out.println(s);
            // System.out.println(this.getClass().getName() + " " + new Date() +
            // " " + s);
        }
    }
}

/*
 * This file is part of AceQL HTTP.
 * AceQL HTTP: SQL Over HTTP                                     
 * Copyright (C) 2017,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.                                
 *                                                                               
 * AceQL HTTP is free software; you can redistribute it and/or                 
 * modify it under the terms of the GNU Lesser General Public                    
 * License as published by the Free Software Foundation; either                  
 * version 2.1 of the License, or (at your option) any later version.            
 *                                                                               
 * AceQL HTTP is distributed in the hope that it will be useful,               
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU             
 * Lesser General Public License for more details.                               
 *                                                                               
 * You should have received a copy of the GNU Lesser General Public              
 * License along with this library; if not, write to the Free Software           
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  
 * 02110-1301  USA
 * 
 * Any modifications to this file must keep this entire header
 * intact.
 */
package com.kawansoft.aceql.gui;

import com.kawansoft.aceql.gui.service.ServiceInstaller;
import com.kawansoft.aceql.gui.service.ServiceUtil;
import com.kawansoft.aceql.gui.task.AceQLTask;
import com.kawansoft.aceql.gui.util.AceQLManagerUtil;
import com.kawansoft.aceql.gui.util.ConfigurationUtil;
import com.kawansoft.aceql.gui.util.PropertiesFileFilter;
import com.kawansoft.app.parms.MessagesManager;
import com.kawansoft.app.parms.ParmsConstants;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import com.kawansoft.app.parms.util.ImageParmsUtil;
import com.kawansoft.app.parms.util.ParmsUtil;
import com.kawansoft.app.util.ButtonResizer;
import com.kawansoft.app.util.ClipboardManager;
import com.kawansoft.app.util.FileDialogMemory;
import com.kawansoft.app.util.Help;
import com.kawansoft.app.util.JFileChooserMemory;
import com.kawansoft.app.util.SystemPropDisplayer;
import com.kawansoft.app.util.WindowSettingMgr;
import com.kawansoft.app.util.table.FileDrop;
import com.swing.util.SwingUtil;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.jdesktop.swingx.JXTitledSeparator;
import org.kawanfw.sql.api.server.web.WebServerApi;
import org.kawanfw.sql.tomcat.TomcatStarterUtil;

/**
 *
 * @author Nicolas de Pomereu
 */
public class AceQLManager extends javax.swing.JFrame {

    public static final String CR_LF = System.getProperty("line.separator");

    public static final int SLEEP_SCAN = 330;

    /**
     * Says if we continue to udate Windows Service Status
     */
    private static boolean UPDATE_SERVICE_STATUS_RUNING = false;

    public static final int STANDARD_STOPPED = 1;
    public static final int STANDARD_STARTING = 2;
    public static final int STANDARD_STOPPING = 3;
    public static final int STANDARD_RUNNING = 4;

    /**
     * Standard Status
     */
    public static int STANDARD_STATUS = STANDARD_STOPPED;

    /**
     * Windows Service Status
     */
    private int serviceStatus = ServiceUtil.NOT_INSTALLED;

    private JFrame thisOne = this;

    private Help help = null;

    private AceQLConsole aceQLConsole = null;

    /**
     * Add a clipboard manager for help text
     */
    private ClipboardManager clipboard = null;

    private AboutFrame aboutFrame = null;
    private SystemPropDisplayer systemPropDisplayer = null;
    private AceQLManagerInstall aceQLManagerInstall = null;

    private static boolean WINDOWS_OK_WITH_AWT = false;
    private ClasspathDisplayer classpathDisplayer;

    /**
     * Creates new form Preferences
     */
    public AceQLManager() {
        initComponents();
        initializeIt();
    }

    /**
     * This is the method to include in *our* constructor(s)
     */
    public void initializeIt() {

        Dimension dim = new Dimension(604, 604);
        this.setPreferredSize(dim);
        this.setSize(dim);

        String appName = ParmsConstants.APP_NAME;

        if (ParmsUtil.isAceQLPro()) {
            appName += " Pro";
        }

        this.jLabelLogo.setText(appName);

        try {
            this.setIconImage(ImageParmsUtil.getAppIcon());
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }

        // Add a Clipboard Manager
        clipboard = new ClipboardManager(jPanelMain);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                installService();
            }
        });

        loadConfiguration();
        updateStandardStatusThreadStart();

        if (SystemUtils.IS_OS_WINDOWS) {
            updateServiceStatusThreadStart();
            jMenuItemServiceInstall.setVisible(false); // Futur usage if any problem
        } else {
            jLabelServiceStatusValue.setText("Not installed");
            jLabelServiceStatusValue
                    .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_GREY_PNG));

            jButtonStartService.setEnabled(false);
            jButtonStopService.setEnabled(false);
            jButtonDisplayLogs.setEnabled(false);
            jButtonServicesConsole.setEnabled(false);

            jMenuItemServiceInstall.setVisible(false);
        }

        ((AbstractDocument) this.jTextFieldPort.getDocument()).setDocumentFilter(new AceQLManager.MyDocumentFilter());

        jTextFieldPropertiesFile.requestFocusInWindow();

        if (SystemUtils.IS_OS_MAC_OSX) {
            jMenuItemQuit.setVisible(false); // Quit is already in default left menu
            jMenuItemClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        } else {
            jMenuItemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        }

        SwingUtil.resizeJComponentsForNimbusAndMacOsX(rootPane);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                saveSettings();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                saveSettings();
            }
        });

        // Our window listener for all events
        // If window is closed ==> call close()
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeOnExit();
            }
        });

        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                jButtonApply.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                jButtonApply.setEnabled(true);
            }

            public void insertUpdate(DocumentEvent e) {
                jButtonApply.setEnabled(true);
            }
        };

        // Listen for changes in the text
        jTextFieldPropertiesFile.getDocument().addDocumentListener(documentListener);
        jTextFieldHost.getDocument().addDocumentListener(documentListener);
        jTextFieldPort.getDocument().addDocumentListener(documentListener);
        //defaultListModel.addListDataListener(new MyListDataListener());

        this.keyListenerAdder();

        this.setTitle(jLabelLogo.getText());

        // Button Apply is not enabled
        jButtonApply.setEnabled(false);

        ButtonResizer buttonResizer = new ButtonResizer(jPanelButtons);
        buttonResizer.setWidthToMax();

        ButtonResizer buttonResizer2 = new ButtonResizer(jPanelProperties);
        buttonResizer2.setWidthToMax();

        ButtonResizer buttonResizer3 = new ButtonResizer(jPanelButtonStartStop);
        buttonResizer3.setWidthToMax();

        new FileDrop(this, new FileDrop.Listener() {
            @Override
            public void filesDropped(File[] files) {
                // handle file drop  
                addDropedFiles(files);

            } // end filesDropped
        });

        // Load and activate previous windows settings
        WindowSettingMgr.load(this);

        pack();
        //update(getGraphics());
    }

    /*
    class MyListDataListener implements ListDataListener {

        @Override
        public void contentsChanged(ListDataEvent e) {
            jButtonApply.setEnabled(true);
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            jButtonApply.setEnabled(true);
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            jButtonApply.setEnabled(true);
        }
    }
     */
 /*
    public void addFilesToClasspath(List<File> files) {
        String classpath = System.getProperty("java.class.path");
        
        for (int i = 0; i < files.size(); i++) {
            if (!classpath.contains(files.get(i).toString())) {
                try {
                    ClassPathHacker.addFile(files.get(i));
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Logger.getLogger(AceQLManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }   
    }*/
    public void loadConfiguration() {

        File configurationProperties = ConfigurationUtil.getConfirurationPropertiesFile();

        String aceqlProperties = null;
        String host = null;
        int port = 0;

        if (configurationProperties.exists()) {
            ConfigurationUtil configurationUtil = new ConfigurationUtil();
            try {
                configurationUtil.load();
            } catch (Exception ex) {
                JOptionPane
                        .showMessageDialog(this,
                                "Unable to read properties file " + configurationProperties + ": " + ex.toString(),
                                ParmsConstants.APP_NAME, JOptionPane.ERROR_MESSAGE);
                return;
            }

            aceqlProperties = configurationUtil.getAceqlProperties();
            host = configurationUtil.getHost();
            port = configurationUtil.getPort();
        }

        if (host == null) {
            host = ConfigurationUtil.DEFAULT_HOST;
        }

        if (port == 0) {
            port = ConfigurationUtil.DEFAULT_PORT;
        }

        jTextFieldHost.setText(host);
        jTextFieldPort.setText("" + port);

        if (aceqlProperties == null || aceqlProperties.isEmpty()) {
            File fileIn = new File(ParmsUtil.getInstallAceQLDir() + File.separator + "conf" + File.separator + "aceql-server.properties");

            if (!fileIn.exists()) {
                JOptionPane
                        .showMessageDialog(this,
                                "Missing base configuration file. Please reinstall AceQL HTTP.",
                                ParmsConstants.APP_NAME, JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Copy the file to ParmsUtil.fileOut()/conf because it won't be possible to edit directly it in c:\Program (Windows Security)
            File confDir = new File(ParmsUtil.getBaseDir() + File.separator + "conf");
            File fileOut = new File(confDir.toString() + File.separator + "aceql-server.properties");

            if (!fileOut.exists()) {
                try {
                    FileUtils.copyFile(fileIn, fileOut);
                    jTextFieldPropertiesFile.setText(fileOut.toString());
                } catch (IOException ex) {
                    jTextFieldPropertiesFile.setText(null);
                    Logger.getLogger(AceQLManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                jTextFieldPropertiesFile.setText(fileOut.toString());
            }

            // Install Postgres Driver
        } else {
            jTextFieldPropertiesFile.setText(aceqlProperties);
        }

        try {
            setAceQLServerURL(host, port, new File(jTextFieldPropertiesFile.getText()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Can not load the Property File. Reason: " + ex.getMessage(), ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Store the properties if they did not exists
        if (!ConfigurationUtil.getConfirurationPropertiesFile().exists()) {
            storeConfiguration();
        }
    }

    private void startStandard() {

        if (aceQLConsole != null) {
            aceQLConsole.dispose();
        }

        try {
            if ((ServiceUtil.isStarting() || ServiceUtil.isRunning())) {
                JOptionPane
                        .showMessageDialog(this,
                                "Please stop Windows Service in order to run in Standard Mode.",
                                ParmsConstants.APP_NAME, JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                    "Unable to display Windows Service Status: "
                    + ioe.getMessage(), ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean verifyOk = verifyConfigValues();

        if (!verifyOk) {
            return;
        }

        int port = Integer.parseInt(jTextFieldPort.getText());

        aceQLConsole = new AceQLConsole();

        AceQLTask aceQLTask = new AceQLTask(AceQLTask.STANDARD_MODE, jTextFieldPropertiesFile.getText(), jTextFieldHost.getText(), port);
        aceQLTask.start();

    }

    /**
     * Start as a thread updateServiceStatusLabel
     */
    private void stopStandardThreadStart() {

        int port = 0;

        try {
            port = Integer.parseInt(jTextFieldPort.getText());
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(this,
                    "Port must be numeric.", ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        final int finalPort = port;

        Thread thread = new Thread() {
            @Override
            public void run() {
                stopStandard(finalPort);
            }
        };

        thread.start();
    }

    private void stopStandard(int port) {
        WebServerApi webServerApi = new WebServerApi();
        try {

            STANDARD_STATUS = STANDARD_STOPPING;
            webServerApi.stopServer(port);

            try {
                // Give One second to be sure release bound port
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(AceQLManager.class.getName()).log(Level.SEVERE, null, ex);
            }

            STANDARD_STATUS = STANDARD_STOPPED;

            System.out.println();
            System.out.println("SQL Web server running on port " + port
                    + " successfully stopped!");
            System.out.println();
        } catch (IOException e) {

            STANDARD_STATUS = STANDARD_RUNNING;
            if (e instanceof ConnectException) {
                System.err.println(e.getMessage());
            } else {
                System.err
                        .println("Impossible to stop the SQL Web server running on port "
                                + port);
                System.err.println(e.getMessage());

                if (e.getCause() != null) {
                    System.err.println("Java Exception Stack Trace:");
                    e.printStackTrace();
                }
            }

            System.err.println();
        }
    }

    /**
     * Start as a thread updateServiceStatusLabel
     */
    private void updateStandardStatusThreadStart() {
        thisOne = this;
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    updateStandardStatusLabel();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(thisOne,
                            "Unable to display Standard Status: "
                            + e.getMessage());
                    e.printStackTrace();
                }
            }

        };

        thread.start();
    }

    private void updateStandardStatusLabel() throws IOException {
        while (true) {
            try {
                Thread.sleep(SLEEP_SCAN);
            } catch (InterruptedException ex) {
                Logger.getLogger(AceQLManager.class.getName()).log(
                        Level.SEVERE, null, ex);
            }

            if (STANDARD_STATUS == STANDARD_STOPPED) {
                jButtonStart.setEnabled(true);
                jButtonStop.setEnabled(false);
                jLabeStandardStatusValue.setText("Stopped");
                jLabeStandardStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_RED_PNG));

            } else if (STANDARD_STATUS == STANDARD_STARTING) {
                jButtonStart.setEnabled(false);
                jButtonStop.setEnabled(false);
                jLabeStandardStatusValue.setText("Starting...");
                jLabeStandardStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_YELLOW_PNG));
            } else if (STANDARD_STATUS == STANDARD_STOPPING) {
                jButtonStart.setEnabled(false);
                jButtonStop.setEnabled(false);
                jLabeStandardStatusValue.setText("Stopping...");
                jLabeStandardStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_YELLOW_PNG));
            } else if (STANDARD_STATUS == STANDARD_RUNNING) {
                jButtonStart.setEnabled(false);
                jButtonStop.setEnabled(true);
                jLabeStandardStatusValue.setText("Started");
                jLabeStandardStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_GREEN_PNG));
            }
        }
    }

    /**
     * Start as a thread updateServiceStatusLabel
     */
    private void updateServiceStatusThreadStart() {
        thisOne = this;
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    updateServiceStatusLabel();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(thisOne,
                            "Unable to display Windows Service Status: "
                            + e.getMessage());
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    /**
     * Stops the Windows Service Status update thread
     */
    private void updateServiceStatusThreadStop() {
        UPDATE_SERVICE_STATUS_RUNING = false;
    }

    /**
     * Update Windows Service Status
     *
     * @throws IOException
     */
    private void updateServiceStatusLabel() throws IOException {

        UPDATE_SERVICE_STATUS_RUNING = true;

        while (UPDATE_SERVICE_STATUS_RUNING) {

            try {
                Thread.sleep(SLEEP_SCAN);
            } catch (InterruptedException ex) {
                Logger.getLogger(AceQLManager.class.getName()).log(
                        Level.SEVERE, null, ex);
            }

            serviceStatus = ServiceUtil
                    .getServiceStatus(ServiceUtil.ACEQL_HTTP_SERVICE);
            String startModeLabel = ServiceUtil
                    .getServiceStartupTypeLabel(ServiceUtil.ACEQL_HTTP_SERVICE);

            String startModeText = null;
            if (!startModeLabel.isEmpty()) {
                startModeText = " - Startup type: " + startModeLabel;
            }

            if (serviceStatus == ServiceUtil.NOT_INSTALLED) {
                jLabelServiceStartModeValue.setText(startModeText);
                jLabelServiceStatusValue.setText("Not Installed");

                jLabelServiceStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_GREY_PNG));

                jButtonStartService.setEnabled(false);
                jButtonStopService.setEnabled(false);

            } else if (serviceStatus == ServiceUtil.STOPPED) {
                jLabelServiceStartModeValue.setText(startModeText);
                jLabelServiceStatusValue.setText("Stopped");

                jLabelServiceStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_RED_PNG));

                jButtonStartService.setEnabled(true);
                jButtonStopService.setEnabled(false);
            } else if (serviceStatus == ServiceUtil.STARTING) {
                jLabelServiceStartModeValue.setText(startModeText);
                jLabelServiceStatusValue.setText("Starting...");

                jLabelServiceStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_YELLOW_PNG));

                jButtonStartService.setEnabled(false);
                jButtonStopService.setEnabled(false);
            } else if (serviceStatus == ServiceUtil.STOPPING) {
                jLabelServiceStartModeValue.setText(startModeText);
                jLabelServiceStatusValue.setText("Stopping...");

                jLabelServiceStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_YELLOW_PNG));

                jButtonStartService.setEnabled(false);
                jButtonStopService.setEnabled(false);
            } else if (serviceStatus == ServiceUtil.RUNNING) {
                jLabelServiceStartModeValue.setText(startModeText);
                jLabelServiceStatusValue.setText("Started");

                jLabelServiceStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_GREEN_PNG));

                jButtonStartService.setEnabled(false);
                jButtonStopService.setEnabled(true);
            }

        }

    }

    private void installService() {

        if (!SystemUtils.IS_OS_WINDOWS) {
            return;
        }

        boolean serviceInstalled = false;

        try {
            serviceInstalled = ServiceUtil.isInstalled();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                    "Unable to get if service is installed: "
                    + ioe.getMessage(), ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!serviceInstalled) {
            try {
                ServiceInstaller.installService();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(thisOne,
                        "Unable to install Windows Service: "
                        + e.getMessage());
                e.printStackTrace();
            }

            try {
                ServiceInstaller.updateServiceClasspath();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(thisOne,
                        "Unable to update Windows Service CLASSPATH: "
                        + e.getMessage());
                e.printStackTrace();
            }

        }

    }

    private void startService() {
        if (STANDARD_STATUS != STANDARD_STOPPED) {
            JOptionPane
                    .showMessageDialog(this,
                            "Please stop server in Standard Mode in order to run in Windows Service Mode.",
                            ParmsConstants.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ServiceInstaller.updateServiceClasspath();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Unable to update Windows Service: "
                    + e.getMessage());
            e.printStackTrace();
            return;
        }

        boolean isLocalSystem = true;

        try {
            isLocalSystem = ServiceUtil.isLocalSystem(ServiceUtil.ACEQL_HTTP_SERVICE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Unable to say if service is with LocalSystem: "
                    + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (isLocalSystem) {
            try {
                ServiceInstaller.updateServiceUserHomeSystem();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(thisOne,
                        "Unable to update Windows Service: "
                        + e.getMessage());
                e.printStackTrace();
            }
        } else {
            try {
                ServiceInstaller.updateServiceUserHomeNormal();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(thisOne,
                        "Unable to update Windows Service: "
                        + e.getMessage());
                e.printStackTrace();
            }

        }

        try {
            ServiceUtil.startService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(thisOne,
                    "Unable to start Windows Service: "
                    + e.getMessage());
            e.printStackTrace();
        }

    }

    private void stopService() {

        try {
            ServiceUtil.stopService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(thisOne,
                    "Unable to stop Windows Service: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayLogs() {

        try {
            File file = new File(ParmsUtil.getBaseDir() + File.separator + "windows-service-logs");
            Desktop.getDesktop().open(file);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Unable to display Windows Service log directory: " + CR_LF + ex.getMessage(),
                    ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
    private boolean existsOpenKeyForProperties() throws Exception {
        String subKey = "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\FileExts\\.properties\\UserChoice";

        RegistryReader registryReader = new RegistryReader();
        String useValue = registryReader.getCurrentUserKeyValue(subKey, "ProgId");

        if (useValue != null) {
            return true;
        } else {
            return false;
        }
    }
    */

    private void windowsServiceManagementConsole() {

        try {
            if (!SystemUtils.IS_OS_WINDOWS) {
                return;
            }

            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C",
                    "startMmc.bat");

            //JOptionPane.showMessageDialog(null, "serviceDirectory: " + serviceDirectory);
            pb.directory(new File(ServiceInstaller.SERVICE_DIRECTORY));
            Process p = pb.start();

            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Unable to display Microsoft Management Console: " + CR_LF + ex.getMessage(),
                    ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void serviceInstall() {
        if (aceQLManagerInstall != null) {
            aceQLManagerInstall.dispose();
        }

        aceQLManagerInstall = new AceQLManagerInstall(this);
        aceQLManagerInstall.setVisible(true);
    }

    private void displayClasspath() {
        if (classpathDisplayer != null) {
            classpathDisplayer.dispose();
        }

        classpathDisplayer = new ClasspathDisplayer(this);
        classpathDisplayer.setVisible(true);

    }

    // See http://stackoverflow.com/questions/14058505/jtextfield-accept-only-alphabet-and-white-space/14060047#14060047
    class MyDocumentFilter extends DocumentFilter {

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int i, int i1, String string, AttributeSet as) throws BadLocationException {
            for (int n = string.length(); n > 0; n--) {//an inserted string may be more than a single character i.e a copy and paste of 'aaa123d', also we iterate from the back as super.XX implementation will put last insterted string first and so on thus 'aa123d' would be 'daa', but because we iterate from the back its 'aad' like we want
                char c = string.charAt(n - 1);//get a single character of the string

                String s = "" + c;
                if (StringUtils.isNumeric(s)) {
                    super.replace(fb, i, i1, String.valueOf(c), as);//allow update to take place for the given character
                } else {
                    Toolkit.getDefaultToolkit().beep();
//                    JOptionPane.showMessageDialog(thisOne, 
//                            "Les caract�res doivent �tre num�rique.", Parms.APP_NAME, JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        @Override
        public void remove(DocumentFilter.FilterBypass fb, int i, int i1) throws BadLocationException {
            super.remove(fb, i, i1);
        }

        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int i, String string, AttributeSet as) throws BadLocationException {
            super.insertString(fb, i, string, as);

        }
    }

    /**
     * Universal key listener
     */
    private void keyListenerAdder() {
        List<Component> components = SwingUtil.getAllComponants(this);

        for (int i = 0; i < components.size(); i++) {
            Component comp = components.get(i);

            comp.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    thisKeyReleased(e);
                }
            });
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // KEYS PART
    ///////////////////////////////////////////////////////////////////////////    
    private void thisKeyReleased(KeyEvent e) {
        //System.out.println("thisKeyReleased(KeyEvent e) " + e.getComponent().getName()); 
        int id = e.getID();
        if (id == KeyEvent.KEY_RELEASED) {
            int keyCode = e.getKeyCode();

            if (keyCode == KeyEvent.VK_ENTER) {
                actionApply();
            }

            if (keyCode == KeyEvent.VK_ESCAPE) {
                closeOnExit();
            }

            if (keyCode == KeyEvent.VK_F1) {
                jButtonHelpActionPerformed(null);
            }

        }
    }

    public void saveSettings() {
        WindowSettingMgr.save(this);
    }

    
    private void doSystemExit() {
        updateServiceStatusThreadStop();
        AceQLManagerUtil.systemExitWrapper();
    }
    
    private void closeOnExit() {
        saveSettings();
        this.setVisible(false);
    }

    private boolean verifyConfigValues() {
        String propertiesFile = jTextFieldPropertiesFile.getText();

        if (propertiesFile == null || propertiesFile.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a Properties File", ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        File file = new File(propertiesFile);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this,
                    "The Properties File does not exist: " + CR_LF + file, ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String host = jTextFieldHost.getText();

        if (host == null || host.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a Host.", ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int port = 0;

        try {
            port = Integer.parseInt(jTextFieldPort.getText());
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(this,
                    "Port must be numeric.", ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        URL url = null;
        try {

            if (host.toLowerCase().startsWith("http://") || host.toLowerCase().startsWith("https://")) {
                host = StringUtils.substringAfter(host, "//");
            }

            url = new URL("http://" + host + ":" + port);
            boolean doDisplay = false;
            if (doDisplay) {
             System.out.println(url);               
            }

        } catch (MalformedURLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid URL without the scheme for Host. Examples: localhost, www.acme.com", ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void actionApply() {

        //this.getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //this.getRootPane().setCursor(Cursor.getDefaultCursor());  
        boolean verifyOk = verifyConfigValues();

        if (!verifyOk) {
            return;
        }

        try {
            setAceQLServerURL(jTextFieldHost.getText(), Integer.parseInt(jTextFieldPort.getText()), new File(jTextFieldPropertiesFile.getText()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Can not load the Property File. Reason: " + ex.getMessage(), ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        storeConfiguration();
        jButtonApply.setEnabled(false);

    }

    public void storeConfiguration() throws HeadlessException {
        try {
            int port = Integer.parseInt(jTextFieldPort.getText());

            ConfigurationUtil configurationUtil = new ConfigurationUtil();
            configurationUtil.store(jTextFieldPropertiesFile.getText(), jTextFieldHost.getText(), port);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Enable to store configuration on configuration file: " + ConfigurationUtil.getConfirurationPropertiesFile() + ". Reason: " + ex, ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static File[] getInstalledJdbcDrivers() {
        File libJdbcDir = new File(ParmsUtil.getInstallAceQLDir() + File.separator + "lib-jdbc");
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name != null && name.endsWith(".jar")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        File[] files = libJdbcDir.listFiles(filenameFilter);
        return files;
    }

    public void setAceQLServerURL(String host, int port, File propertiesFile) throws HeadlessException, Exception {

        if (propertiesFile == null || !propertiesFile.exists()) {
            return;
        }

        Properties properties = TomcatStarterUtil.getProperties(propertiesFile);
        String aceqlServer = "";
        aceqlServer = properties.getProperty("aceQLManagerServletCallName");
        String scheme = "http";

        String sslConnectorSSLEnabled = properties
                .getProperty("sslConnector.SSLEnabled");

        if (sslConnectorSSLEnabled != null && sslConnectorSSLEnabled.trim().equals("true")) {
            scheme = properties
                    .getProperty("sslConnector.scheme").trim();
        }

        this.jButtonURL.setText(scheme + "://" + host + ":" + port + "/" + aceqlServer);
    }

    private void actionOk() {

        if (jButtonApply.isEnabled()) {
            boolean verifyOk = verifyConfigValues();

            if (!verifyOk) {
                return;
            }

            actionApply();
        }

        closeOnExit();
    }

    private void actionResetWindows() {
        int response = JOptionPane.showConfirmDialog(this,
                MessagesManager.get("the_windows_will_be_reset"),
                ParmsConstants.APP_NAME,
                JOptionPane.OK_CANCEL_OPTION);

        if (response != JOptionPane.OK_OPTION) {
            return;
        }

        WindowSettingMgr.resetAll();
        JOptionPane.showMessageDialog(this, MessagesManager.get("the_windows_have_been_reset"), ParmsConstants.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
        AceQLManagerUtil.systemExitWrapper();
    }

    private void addPropertyFileWithAwt() {
        FileDialog fileDialog = new FileDialogMemory(this, MessagesManager.get("system_open"), FileDialog.LOAD);
        fileDialog.setIconImage(ImageParmsUtil.getAppIcon());
        fileDialog.setType(FileDialog.Type.NORMAL);
        fileDialog.setMultipleMode(false);

        fileDialog.setFile("*.properties");
        fileDialog.setFilenameFilter(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".properties");
            }
        });

        fileDialog.setVisible(true);
        File[] selectedFiles = fileDialog.getFiles();
        if (selectedFiles.length == 0) {
            return;
        }
        jTextFieldPropertiesFile.setText(selectedFiles[0].toString());

    }

    private void addPropertyFileWithSwing() throws HeadlessException {
        JFileChooser jfileChooser = new JFileChooserMemory();

        jfileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        jfileChooser.setMultiSelectionEnabled(false);

        jfileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        jfileChooser.setFileFilter(new PropertiesFileFilter());
        jfileChooser.setAcceptAllFileFilterUsed(false);

        int returnVal = jfileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jfileChooser.getSelectedFile();
            jTextFieldPropertiesFile.setText(file.toString());
        }
    }

    /**
     * Add files to the AbstractFileListManager
     * <br>
     * may be called by outside program
     *
     * @param files Files to addDropedFiles
     */
    @SuppressWarnings("unchecked")
    public void addDropedFiles(File[] files) {
        if (files == null || files.length == 0) {
            return;
        }

        List<File> drivers = new ArrayList<>();

        for (File file : files) {
            if (file.toString().endsWith(".jar")) {
                drivers.add(file);
            }
        }

        File[] fileArray = new File[drivers.size()];
        fileArray = drivers.toArray(fileArray);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelMain = new JPanel();
        jPanelLogo = new JPanel();
        jPanelSepBlank11 = new JPanel();
        jLabelLogo = new JLabel();
        jPanelSepLine2New2 = new JPanel();
        jPanel22 = new JPanel();
        jSeparator4 = new JSeparator();
        jPanel23 = new JPanel();
        jPanelSepBlanc8spaces1 = new JPanel();
        jPanelTitledSeparator6 = new JPanel();
        jPaneBlanklLeft1 = new JPanel();
        jXTitledSeparator20pixels2 = new JXTitledSeparator();
        jPanelSep3x6 = new JPanel();
        jXTitledSeparator6 = new JXTitledSeparator();
        jPanelBlankRight1 = new JPanel();
        jPanelSepBlanc8spaces3 = new JPanel();
        jPanelProperties = new JPanel();
        jPanelLeft16 = new JPanel();
        jLabelPropertiesFile = new JLabel();
        jPanelLeft13 = new JPanel();
        jTextFieldPropertiesFile = new JTextField();
        jPanelLeft14 = new JPanel();
        jButtonBrowse = new JButton();
        jPanelLeft15 = new JPanel();
        jButtonEdit = new JButton();
        jPanelEndField4 = new JPanel();
        jPanelHost = new JPanel();
        jPanelLeft22 = new JPanel();
        jLabelHost = new JLabel();
        jPanelLeft17 = new JPanel();
        jTextFieldHost = new JTextField();
        jPanelLeft18 = new JPanel();
        jLabelHost1 = new JLabel();
        jPanelLeft19 = new JPanel();
        jTextFieldPort = new JTextField();
        jPanelEndField6 = new JPanel();
        jPanelClasspath = new JPanel();
        jPanelLeft25 = new JPanel();
        jLabelClasspath = new JLabel();
        jPanelLeft21 = new JPanel();
        jButtonDisplayClasspath = new JButton();
        jPanelSepBlanc8spaces = new JPanel();
        jPanelTitledSeparator5 = new JPanel();
        jPaneBlanklLeft2 = new JPanel();
        jXTitledSeparator20pixels1 = new JXTitledSeparator();
        jPanelSep3x7 = new JPanel();
        jXTitledSeparator5 = new JXTitledSeparator();
        jPanelBlankRight2 = new JPanel();
        jPanelSepBlanc8spaces5 = new JPanel();
        jPanelURL = new JPanel();
        jPanelLeft24 = new JPanel();
        jLabelURL = new JLabel();
        jPanelLeft20 = new JPanel();
        jButtonURL = new JButton();
        jPanelRadioStandard = new JPanel();
        jPanelLeft23 = new JPanel();
        jLabelStandardMode = new JLabel();
        jLabelStandardStatus = new JLabel();
        jLabeStandardStatusValue = new JLabel();
        jPanelButtonStartStop = new JPanel();
        jPanelButtonsStartStandard = new JPanel();
        jPanelLeft31 = new JPanel();
        jButtonStart = new JButton();
        jButtonStop = new JButton();
        jPaneSepInstallAndStart1 = new JPanel();
        jButtonDisplayConsole = new JButton();
        jPanelSepBlanc8spaces4 = new JPanel();
        jPanelRadioService = new JPanel();
        jPanelLeft26 = new JPanel();
        jLabelWindowsServiceMode = new JLabel();
        jLabelServiceStartModeValue = new JLabel();
        jLabelServiceStatus = new JLabel();
        jLabelServiceStatusValue = new JLabel();
        jPanelButtonsStartService = new JPanel();
        jPanelLeft33 = new JPanel();
        jButtonStartService = new JButton();
        jButtonStopService = new JButton();
        jPaneSep2 = new JPanel();
        jButtonDisplayLogs = new JButton();
        jButtonServicesConsole = new JButton();
        jPanelSepBlanc8spaces8 = new JPanel();
        jPanelSepLine2New = new JPanel();
        jPanel28 = new JPanel();
        jSeparator2 = new JSeparator();
        jPanel29 = new JPanel();
        jPanelBottom = new JPanel();
        jPanel2 = new JPanel();
        jPanelButtons = new JPanel();
        jButtonOk = new JButton();
        jButtonApply = new JButton();
        jButtonHelp = new JButton();
        jPanel1 = new JPanel();
        jMenuBar1 = new JMenuBar();
        jMenuFile = new JMenu();
        jMenuItemServiceInstall = new JMenuItem();
        jMenuItemClose = new JMenuItem();
        jMenuItemQuit = new JMenuItem();
        jMenuOptions = new JMenu();
        jMenuCheckForUpdates = new JMenuItem();
        jMenuItemResetWindows = new JMenuItem();
        jMenuHelp = new JMenu();
        jMenuItemHelp = new JMenuItem();
        jMenuItemReleaseNotes = new JMenuItem();
        jSeparator3 = new JPopupMenu.Separator();
        jMenuItemSystemInfo = new JMenuItem();
        jMenuItemAbout = new JMenuItem();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        jPanelMain.setLayout(new BoxLayout(jPanelMain, BoxLayout.Y_AXIS));

        jPanelLogo.setMaximumSize(new Dimension(32767, 70));
        jPanelLogo.setMinimumSize(new Dimension(137, 70));
        jPanelLogo.setPreferredSize(new Dimension(431, 70));
        jPanelLogo.setLayout(new BoxLayout(jPanelLogo, BoxLayout.LINE_AXIS));

        jPanelSepBlank11.setMaximumSize(new Dimension(10, 10));
        jPanelSepBlank11.setPreferredSize(new Dimension(10, 11));

        GroupLayout jPanelSepBlank11Layout = new GroupLayout(jPanelSepBlank11);
        jPanelSepBlank11.setLayout(jPanelSepBlank11Layout);
        jPanelSepBlank11Layout.setHorizontalGroup(jPanelSepBlank11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanelSepBlank11Layout.setVerticalGroup(jPanelSepBlank11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelLogo.add(jPanelSepBlank11);

        jLabelLogo.setFont(new Font("Tahoma", 1, 13)); // NOI18N
        jLabelLogo.setIcon(new ImageIcon(getClass().getResource("/com/kawansoft/app/parms/images/logos/logo-AceQL_48.png"))); // NOI18N
        jLabelLogo.setText("AceQL HTTP");
        jLabelLogo.setToolTipText("");
        jPanelLogo.add(jLabelLogo);

        jPanelMain.add(jPanelLogo);

        jPanelSepLine2New2.setMaximumSize(new Dimension(32787, 10));
        jPanelSepLine2New2.setMinimumSize(new Dimension(0, 10));
        jPanelSepLine2New2.setPreferredSize(new Dimension(20, 10));
        jPanelSepLine2New2.setLayout(new BoxLayout(jPanelSepLine2New2, BoxLayout.LINE_AXIS));

        jPanel22.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanel22Layout = new GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(jPanel22Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(jPanel22Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelSepLine2New2.add(jPanel22);
        jPanelSepLine2New2.add(jSeparator4);

        jPanel23.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanel23Layout = new GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(jPanel23Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(jPanel23Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelSepLine2New2.add(jPanel23);

        jPanelMain.add(jPanelSepLine2New2);

        jPanelSepBlanc8spaces1.setMaximumSize(new Dimension(32767, 8));
        jPanelSepBlanc8spaces1.setMinimumSize(new Dimension(10, 8));
        jPanelSepBlanc8spaces1.setPreferredSize(new Dimension(1000, 8));
        jPanelMain.add(jPanelSepBlanc8spaces1);

        jPanelTitledSeparator6.setMinimumSize(new Dimension(184, 24));
        jPanelTitledSeparator6.setPreferredSize(new Dimension(518, 24));
        jPanelTitledSeparator6.setLayout(new BoxLayout(jPanelTitledSeparator6, BoxLayout.LINE_AXIS));

        jPaneBlanklLeft1.setMaximumSize(new Dimension(10, 10));
        jPaneBlanklLeft1.setPreferredSize(new Dimension(10, 11));

        GroupLayout jPaneBlanklLeft1Layout = new GroupLayout(jPaneBlanklLeft1);
        jPaneBlanklLeft1.setLayout(jPaneBlanklLeft1Layout);
        jPaneBlanklLeft1Layout.setHorizontalGroup(jPaneBlanklLeft1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPaneBlanklLeft1Layout.setVerticalGroup(jPaneBlanklLeft1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelTitledSeparator6.add(jPaneBlanklLeft1);

        jXTitledSeparator20pixels2.setMaximumSize(new Dimension(20, 16));
        jXTitledSeparator20pixels2.setMinimumSize(new Dimension(20, 16));
        jXTitledSeparator20pixels2.setPreferredSize(new Dimension(20, 16));
        jXTitledSeparator20pixels2.setTitle("");
        jPanelTitledSeparator6.add(jXTitledSeparator20pixels2);

        jPanelSep3x6.setMaximumSize(new Dimension(3, 5));
        jPanelSep3x6.setMinimumSize(new Dimension(3, 5));

        GroupLayout jPanelSep3x6Layout = new GroupLayout(jPanelSep3x6);
        jPanelSep3x6.setLayout(jPanelSep3x6Layout);
        jPanelSep3x6Layout.setHorizontalGroup(jPanelSep3x6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );
        jPanelSep3x6Layout.setVerticalGroup(jPanelSep3x6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelTitledSeparator6.add(jPanelSep3x6);

        jXTitledSeparator6.setIcon(new ImageIcon(getClass().getResource("/com/kawansoft/app/parms/images/window_gear_24.png"))); // NOI18N
        jXTitledSeparator6.setMaximumSize(new Dimension(2147483647, 24));
        jXTitledSeparator6.setTitle("Configuration");
        jPanelTitledSeparator6.add(jXTitledSeparator6);

        jPanelBlankRight1.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanelBlankRight1Layout = new GroupLayout(jPanelBlankRight1);
        jPanelBlankRight1.setLayout(jPanelBlankRight1Layout);
        jPanelBlankRight1Layout.setHorizontalGroup(jPanelBlankRight1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanelBlankRight1Layout.setVerticalGroup(jPanelBlankRight1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelTitledSeparator6.add(jPanelBlankRight1);

        jPanelMain.add(jPanelTitledSeparator6);

        jPanelSepBlanc8spaces3.setMaximumSize(new Dimension(32767, 12));
        jPanelSepBlanc8spaces3.setMinimumSize(new Dimension(10, 12));
        jPanelSepBlanc8spaces3.setName(""); // NOI18N
        jPanelSepBlanc8spaces3.setPreferredSize(new Dimension(1000, 12));
        jPanelMain.add(jPanelSepBlanc8spaces3);

        jPanelProperties.setMaximumSize(new Dimension(2147483647, 32));
        jPanelProperties.setMinimumSize(new Dimension(91, 32));
        jPanelProperties.setPreferredSize(new Dimension(191, 32));
        jPanelProperties.setLayout(new BoxLayout(jPanelProperties, BoxLayout.LINE_AXIS));

        jPanelLeft16.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanelLeft16Layout = new GroupLayout(jPanelLeft16);
        jPanelLeft16.setLayout(jPanelLeft16Layout);
        jPanelLeft16Layout.setHorizontalGroup(jPanelLeft16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanelLeft16Layout.setVerticalGroup(jPanelLeft16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelProperties.add(jPanelLeft16);

        jLabelPropertiesFile.setHorizontalAlignment(SwingConstants.TRAILING);
        jLabelPropertiesFile.setText("Properties File:");
        jLabelPropertiesFile.setMaximumSize(new Dimension(129, 16));
        jLabelPropertiesFile.setMinimumSize(new Dimension(129, 16));
        jLabelPropertiesFile.setName(""); // NOI18N
        jLabelPropertiesFile.setPreferredSize(new Dimension(129, 16));
        jPanelProperties.add(jLabelPropertiesFile);

        jPanelLeft13.setMaximumSize(new Dimension(5, 5));
        jPanelLeft13.setMinimumSize(new Dimension(5, 5));

        GroupLayout jPanelLeft13Layout = new GroupLayout(jPanelLeft13);
        jPanelLeft13.setLayout(jPanelLeft13Layout);
        jPanelLeft13Layout.setHorizontalGroup(jPanelLeft13Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanelLeft13Layout.setVerticalGroup(jPanelLeft13Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelProperties.add(jPanelLeft13);

        jTextFieldPropertiesFile.setMaximumSize(new Dimension(2147483647, 22));
        jTextFieldPropertiesFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jTextFieldPropertiesFileActionPerformed(evt);
            }
        });
        jPanelProperties.add(jTextFieldPropertiesFile);

        jPanelLeft14.setMaximumSize(new Dimension(5, 5));
        jPanelLeft14.setMinimumSize(new Dimension(5, 5));

        GroupLayout jPanelLeft14Layout = new GroupLayout(jPanelLeft14);
        jPanelLeft14.setLayout(jPanelLeft14Layout);
        jPanelLeft14Layout.setHorizontalGroup(jPanelLeft14Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanelLeft14Layout.setVerticalGroup(jPanelLeft14Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelProperties.add(jPanelLeft14);

        jButtonBrowse.setText("Browse");
        jButtonBrowse.setToolTipText("");
        jButtonBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonBrowseActionPerformed(evt);
            }
        });
        jPanelProperties.add(jButtonBrowse);

        jPanelLeft15.setMaximumSize(new Dimension(5, 5));
        jPanelLeft15.setMinimumSize(new Dimension(5, 5));

        GroupLayout jPanelLeft15Layout = new GroupLayout(jPanelLeft15);
        jPanelLeft15.setLayout(jPanelLeft15Layout);
        jPanelLeft15Layout.setHorizontalGroup(jPanelLeft15Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanelLeft15Layout.setVerticalGroup(jPanelLeft15Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelProperties.add(jPanelLeft15);

        jButtonEdit.setText("Edit");
        jButtonEdit.setToolTipText("");
        jButtonEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });
        jPanelProperties.add(jButtonEdit);

        jPanelEndField4.setMaximumSize(new Dimension(50, 10));
        jPanelEndField4.setMinimumSize(new Dimension(50, 10));

        GroupLayout jPanelEndField4Layout = new GroupLayout(jPanelEndField4);
        jPanelEndField4.setLayout(jPanelEndField4Layout);
        jPanelEndField4Layout.setHorizontalGroup(jPanelEndField4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanelEndField4Layout.setVerticalGroup(jPanelEndField4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelProperties.add(jPanelEndField4);

        jPanelMain.add(jPanelProperties);

        jPanelHost.setMaximumSize(new Dimension(2147483647, 32));
        jPanelHost.setMinimumSize(new Dimension(91, 32));
        jPanelHost.setPreferredSize(new Dimension(191, 32));
        jPanelHost.setLayout(new BoxLayout(jPanelHost, BoxLayout.LINE_AXIS));

        jPanelLeft22.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanelLeft22Layout = new GroupLayout(jPanelLeft22);
        jPanelLeft22.setLayout(jPanelLeft22Layout);
        jPanelLeft22Layout.setHorizontalGroup(jPanelLeft22Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanelLeft22Layout.setVerticalGroup(jPanelLeft22Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelHost.add(jPanelLeft22);

        jLabelHost.setHorizontalAlignment(SwingConstants.TRAILING);
        jLabelHost.setText("Host:");
        jLabelHost.setMaximumSize(new Dimension(129, 16));
        jLabelHost.setMinimumSize(new Dimension(129, 16));
        jLabelHost.setName(""); // NOI18N
        jLabelHost.setPreferredSize(new Dimension(129, 16));
        jPanelHost.add(jLabelHost);

        jPanelLeft17.setMaximumSize(new Dimension(5, 5));
        jPanelLeft17.setMinimumSize(new Dimension(5, 5));

        GroupLayout jPanelLeft17Layout = new GroupLayout(jPanelLeft17);
        jPanelLeft17.setLayout(jPanelLeft17Layout);
        jPanelLeft17Layout.setHorizontalGroup(jPanelLeft17Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanelLeft17Layout.setVerticalGroup(jPanelLeft17Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelHost.add(jPanelLeft17);

        jTextFieldHost.setMaximumSize(new Dimension(2147483647, 22));
        jTextFieldHost.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jTextFieldHostActionPerformed(evt);
            }
        });
        jPanelHost.add(jTextFieldHost);

        jPanelLeft18.setMaximumSize(new Dimension(5, 5));
        jPanelLeft18.setMinimumSize(new Dimension(5, 5));

        GroupLayout jPanelLeft18Layout = new GroupLayout(jPanelLeft18);
        jPanelLeft18.setLayout(jPanelLeft18Layout);
        jPanelLeft18Layout.setHorizontalGroup(jPanelLeft18Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanelLeft18Layout.setVerticalGroup(jPanelLeft18Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelHost.add(jPanelLeft18);

        jLabelHost1.setHorizontalAlignment(SwingConstants.TRAILING);
        jLabelHost1.setText("Port:");
        jLabelHost1.setName(""); // NOI18N
        jPanelHost.add(jLabelHost1);

        jPanelLeft19.setMaximumSize(new Dimension(5, 5));
        jPanelLeft19.setMinimumSize(new Dimension(5, 5));

        GroupLayout jPanelLeft19Layout = new GroupLayout(jPanelLeft19);
        jPanelLeft19.setLayout(jPanelLeft19Layout);
        jPanelLeft19Layout.setHorizontalGroup(jPanelLeft19Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanelLeft19Layout.setVerticalGroup(jPanelLeft19Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelHost.add(jPanelLeft19);

        jTextFieldPort.setMaximumSize(new Dimension(100, 22));
        jTextFieldPort.setMinimumSize(new Dimension(100, 22));
        jTextFieldPort.setPreferredSize(new Dimension(50, 22));
        jTextFieldPort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jTextFieldPortActionPerformed(evt);
            }
        });
        jPanelHost.add(jTextFieldPort);

        jPanelEndField6.setMaximumSize(new Dimension(50, 10));
        jPanelEndField6.setMinimumSize(new Dimension(50, 10));

        GroupLayout jPanelEndField6Layout = new GroupLayout(jPanelEndField6);
        jPanelEndField6.setLayout(jPanelEndField6Layout);
        jPanelEndField6Layout.setHorizontalGroup(jPanelEndField6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanelEndField6Layout.setVerticalGroup(jPanelEndField6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelHost.add(jPanelEndField6);

        jPanelMain.add(jPanelHost);

        jPanelClasspath.setMaximumSize(new Dimension(2147483647, 32));
        jPanelClasspath.setMinimumSize(new Dimension(91, 32));
        jPanelClasspath.setPreferredSize(new Dimension(191, 32));
        jPanelClasspath.setLayout(new BoxLayout(jPanelClasspath, BoxLayout.LINE_AXIS));

        jPanelLeft25.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanelLeft25Layout = new GroupLayout(jPanelLeft25);
        jPanelLeft25.setLayout(jPanelLeft25Layout);
        jPanelLeft25Layout.setHorizontalGroup(jPanelLeft25Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanelLeft25Layout.setVerticalGroup(jPanelLeft25Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelClasspath.add(jPanelLeft25);

        jLabelClasspath.setHorizontalAlignment(SwingConstants.TRAILING);
        jLabelClasspath.setMaximumSize(new Dimension(129, 16));
        jLabelClasspath.setMinimumSize(new Dimension(129, 16));
        jLabelClasspath.setName(""); // NOI18N
        jLabelClasspath.setPreferredSize(new Dimension(129, 16));
        jPanelClasspath.add(jLabelClasspath);

        jPanelLeft21.setMaximumSize(new Dimension(5, 5));
        jPanelLeft21.setMinimumSize(new Dimension(5, 5));

        GroupLayout jPanelLeft21Layout = new GroupLayout(jPanelLeft21);
        jPanelLeft21.setLayout(jPanelLeft21Layout);
        jPanelLeft21Layout.setHorizontalGroup(jPanelLeft21Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanelLeft21Layout.setVerticalGroup(jPanelLeft21Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelClasspath.add(jPanelLeft21);

        jButtonDisplayClasspath.setText("Display CLASSPATH");
        jButtonDisplayClasspath.setToolTipText("");
        jButtonDisplayClasspath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonDisplayClasspathActionPerformed(evt);
            }
        });
        jPanelClasspath.add(jButtonDisplayClasspath);

        jPanelMain.add(jPanelClasspath);

        jPanelSepBlanc8spaces.setMaximumSize(new Dimension(32767, 8));
        jPanelSepBlanc8spaces.setMinimumSize(new Dimension(10, 8));
        jPanelSepBlanc8spaces.setPreferredSize(new Dimension(1000, 8));
        jPanelMain.add(jPanelSepBlanc8spaces);

        jPanelTitledSeparator5.setMinimumSize(new Dimension(184, 24));
        jPanelTitledSeparator5.setPreferredSize(new Dimension(518, 24));
        jPanelTitledSeparator5.setLayout(new BoxLayout(jPanelTitledSeparator5, BoxLayout.LINE_AXIS));

        jPaneBlanklLeft2.setMaximumSize(new Dimension(10, 10));
        jPaneBlanklLeft2.setPreferredSize(new Dimension(10, 11));

        GroupLayout jPaneBlanklLeft2Layout = new GroupLayout(jPaneBlanklLeft2);
        jPaneBlanklLeft2.setLayout(jPaneBlanklLeft2Layout);
        jPaneBlanklLeft2Layout.setHorizontalGroup(jPaneBlanklLeft2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPaneBlanklLeft2Layout.setVerticalGroup(jPaneBlanklLeft2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelTitledSeparator5.add(jPaneBlanklLeft2);

        jXTitledSeparator20pixels1.setMaximumSize(new Dimension(20, 16));
        jXTitledSeparator20pixels1.setMinimumSize(new Dimension(20, 16));
        jXTitledSeparator20pixels1.setPreferredSize(new Dimension(20, 16));
        jXTitledSeparator20pixels1.setTitle("");
        jPanelTitledSeparator5.add(jXTitledSeparator20pixels1);

        jPanelSep3x7.setMaximumSize(new Dimension(3, 5));
        jPanelSep3x7.setMinimumSize(new Dimension(3, 5));

        GroupLayout jPanelSep3x7Layout = new GroupLayout(jPanelSep3x7);
        jPanelSep3x7.setLayout(jPanelSep3x7Layout);
        jPanelSep3x7Layout.setHorizontalGroup(jPanelSep3x7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 3, Short.MAX_VALUE)
        );
        jPanelSep3x7Layout.setVerticalGroup(jPanelSep3x7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelTitledSeparator5.add(jPanelSep3x7);

        jXTitledSeparator5.setIcon(new ImageIcon(getClass().getResource("/com/kawansoft/app/parms/images/bullet_triangle_green.png"))); // NOI18N
        jXTitledSeparator5.setMaximumSize(new Dimension(2147483647, 24));
        jXTitledSeparator5.setTitle("Server Start & Stop");
        jPanelTitledSeparator5.add(jXTitledSeparator5);

        jPanelBlankRight2.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanelBlankRight2Layout = new GroupLayout(jPanelBlankRight2);
        jPanelBlankRight2.setLayout(jPanelBlankRight2Layout);
        jPanelBlankRight2Layout.setHorizontalGroup(jPanelBlankRight2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanelBlankRight2Layout.setVerticalGroup(jPanelBlankRight2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelTitledSeparator5.add(jPanelBlankRight2);

        jPanelMain.add(jPanelTitledSeparator5);

        jPanelSepBlanc8spaces5.setMaximumSize(new Dimension(32767, 12));
        jPanelSepBlanc8spaces5.setMinimumSize(new Dimension(10, 12));
        jPanelSepBlanc8spaces5.setName(""); // NOI18N
        jPanelSepBlanc8spaces5.setPreferredSize(new Dimension(1000, 12));
        jPanelMain.add(jPanelSepBlanc8spaces5);

        jPanelURL.setMaximumSize(new Dimension(2147483647, 32));
        jPanelURL.setMinimumSize(new Dimension(91, 32));
        jPanelURL.setPreferredSize(new Dimension(191, 32));
        jPanelURL.setLayout(new BoxLayout(jPanelURL, BoxLayout.LINE_AXIS));

        jPanelLeft24.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanelLeft24Layout = new GroupLayout(jPanelLeft24);
        jPanelLeft24.setLayout(jPanelLeft24Layout);
        jPanelLeft24Layout.setHorizontalGroup(jPanelLeft24Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanelLeft24Layout.setVerticalGroup(jPanelLeft24Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelURL.add(jPanelLeft24);

        jLabelURL.setText("AceQL Server URL:");
        jLabelURL.setName(""); // NOI18N
        jPanelURL.add(jLabelURL);

        jPanelLeft20.setMaximumSize(new Dimension(5, 5));
        jPanelLeft20.setMinimumSize(new Dimension(5, 5));

        GroupLayout jPanelLeft20Layout = new GroupLayout(jPanelLeft20);
        jPanelLeft20.setLayout(jPanelLeft20Layout);
        jPanelLeft20Layout.setHorizontalGroup(jPanelLeft20Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanelLeft20Layout.setVerticalGroup(jPanelLeft20Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelURL.add(jPanelLeft20);

        jButtonURL.setForeground(new Color(0, 0, 255));
        jButtonURL.setText("http://localhost:9090/aceql");
        jButtonURL.setToolTipText("");
        jButtonURL.setBorder(null);
        jButtonURL.setBorderPainted(false);
        jButtonURL.setContentAreaFilled(false);
        jButtonURL.setFocusPainted(false);
        jButtonURL.setMargin(new Insets(2, 0, 2, 0));
        jButtonURL.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                jButtonURLMouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                jButtonURLMouseExited(evt);
            }
        });
        jButtonURL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonURLActionPerformed(evt);
            }
        });
        jPanelURL.add(jButtonURL);

        jPanelMain.add(jPanelURL);

        jPanelRadioStandard.setMaximumSize(new Dimension(2147483647, 32));
        jPanelRadioStandard.setMinimumSize(new Dimension(91, 32));
        jPanelRadioStandard.setPreferredSize(new Dimension(191, 32));
        jPanelRadioStandard.setLayout(new BoxLayout(jPanelRadioStandard, BoxLayout.LINE_AXIS));

        jPanelLeft23.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanelLeft23Layout = new GroupLayout(jPanelLeft23);
        jPanelLeft23.setLayout(jPanelLeft23Layout);
        jPanelLeft23Layout.setHorizontalGroup(jPanelLeft23Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelLeft23Layout.setVerticalGroup(jPanelLeft23Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelRadioStandard.add(jPanelLeft23);

        jLabelStandardMode.setText("Standard Mode");
        jPanelRadioStandard.add(jLabelStandardMode);

        jLabelStandardStatus.setText(" -  Status:");
        jPanelRadioStandard.add(jLabelStandardStatus);
        jPanelRadioStandard.add(jLabeStandardStatusValue);

        jPanelMain.add(jPanelRadioStandard);

        jPanelButtonStartStop.setLayout(new BoxLayout(jPanelButtonStartStop, BoxLayout.Y_AXIS));

        jPanelButtonsStartStandard.setMaximumSize(new Dimension(2147483647, 32));
        jPanelButtonsStartStandard.setMinimumSize(new Dimension(91, 32));
        jPanelButtonsStartStandard.setPreferredSize(new Dimension(191, 32));
        jPanelButtonsStartStandard.setLayout(new FlowLayout(FlowLayout.LEFT));

        jPanelLeft31.setMaximumSize(new Dimension(15, 10));
        jPanelLeft31.setMinimumSize(new Dimension(15, 10));
        jPanelLeft31.setPreferredSize(new Dimension(15, 10));

        GroupLayout jPanelLeft31Layout = new GroupLayout(jPanelLeft31);
        jPanelLeft31.setLayout(jPanelLeft31Layout);
        jPanelLeft31Layout.setHorizontalGroup(jPanelLeft31Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanelLeft31Layout.setVerticalGroup(jPanelLeft31Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelButtonsStartStandard.add(jPanelLeft31);

        jButtonStart.setText("Start Server");
        jButtonStart.setToolTipText("");
        jButtonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonStartActionPerformed(evt);
            }
        });
        jPanelButtonsStartStandard.add(jButtonStart);

        jButtonStop.setText("Stop Server");
        jButtonStop.setToolTipText("");
        jButtonStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonStopActionPerformed(evt);
            }
        });
        jPanelButtonsStartStandard.add(jButtonStop);

        jPaneSepInstallAndStart1.setMaximumSize(new Dimension(10, 10));
        jPaneSepInstallAndStart1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelButtonsStartStandard.add(jPaneSepInstallAndStart1);

        jButtonDisplayConsole.setText("Show Console");
        jButtonDisplayConsole.setToolTipText("");
        jButtonDisplayConsole.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonDisplayConsoleActionPerformed(evt);
            }
        });
        jPanelButtonsStartStandard.add(jButtonDisplayConsole);

        jPanelButtonStartStop.add(jPanelButtonsStartStandard);

        jPanelSepBlanc8spaces4.setMaximumSize(new Dimension(32767, 14));
        jPanelSepBlanc8spaces4.setMinimumSize(new Dimension(10, 14));
        jPanelSepBlanc8spaces4.setPreferredSize(new Dimension(1000, 14));
        jPanelButtonStartStop.add(jPanelSepBlanc8spaces4);

        jPanelRadioService.setMaximumSize(new Dimension(32767, 32));
        jPanelRadioService.setMinimumSize(new Dimension(91, 32));
        jPanelRadioService.setPreferredSize(new Dimension(191, 32));
        jPanelRadioService.setLayout(new BoxLayout(jPanelRadioService, BoxLayout.LINE_AXIS));

        jPanelLeft26.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanelLeft26Layout = new GroupLayout(jPanelLeft26);
        jPanelLeft26.setLayout(jPanelLeft26Layout);
        jPanelLeft26Layout.setHorizontalGroup(jPanelLeft26Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelLeft26Layout.setVerticalGroup(jPanelLeft26Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelRadioService.add(jPanelLeft26);

        jLabelWindowsServiceMode.setText("Service Mode - \"AceQL HTTP Server\" Service");
        jPanelRadioService.add(jLabelWindowsServiceMode);
        jPanelRadioService.add(jLabelServiceStartModeValue);

        jLabelServiceStatus.setText(" - Status:");
        jPanelRadioService.add(jLabelServiceStatus);
        jPanelRadioService.add(jLabelServiceStatusValue);

        jPanelButtonStartStop.add(jPanelRadioService);

        jPanelButtonsStartService.setMaximumSize(new Dimension(2147483647, 32));
        jPanelButtonsStartService.setMinimumSize(new Dimension(91, 32));
        jPanelButtonsStartService.setPreferredSize(new Dimension(191, 32));
        jPanelButtonsStartService.setLayout(new FlowLayout(FlowLayout.LEFT));

        jPanelLeft33.setMaximumSize(new Dimension(15, 10));
        jPanelLeft33.setMinimumSize(new Dimension(15, 10));
        jPanelLeft33.setPreferredSize(new Dimension(15, 10));

        GroupLayout jPanelLeft33Layout = new GroupLayout(jPanelLeft33);
        jPanelLeft33.setLayout(jPanelLeft33Layout);
        jPanelLeft33Layout.setHorizontalGroup(jPanelLeft33Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanelLeft33Layout.setVerticalGroup(jPanelLeft33Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanelButtonsStartService.add(jPanelLeft33);

        jButtonStartService.setText("Start Service");
        jButtonStartService.setToolTipText("");
        jButtonStartService.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonStartServiceActionPerformed(evt);
            }
        });
        jPanelButtonsStartService.add(jButtonStartService);

        jButtonStopService.setText("Stop Service");
        jButtonStopService.setToolTipText("");
        jButtonStopService.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonStopServiceActionPerformed(evt);
            }
        });
        jPanelButtonsStartService.add(jButtonStopService);

        jPaneSep2.setMaximumSize(new Dimension(10, 10));
        jPaneSep2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanelButtonsStartService.add(jPaneSep2);

        jButtonDisplayLogs.setText("Service Logs");
        jButtonDisplayLogs.setToolTipText("");
        jButtonDisplayLogs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonDisplayLogsActionPerformed(evt);
            }
        });
        jPanelButtonsStartService.add(jButtonDisplayLogs);

        jButtonServicesConsole.setText("Services Console");
        jButtonServicesConsole.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonServicesConsoleActionPerformed(evt);
            }
        });
        jPanelButtonsStartService.add(jButtonServicesConsole);

        jPanelButtonStartStop.add(jPanelButtonsStartService);

        jPanelMain.add(jPanelButtonStartStop);

        jPanelSepBlanc8spaces8.setMaximumSize(new Dimension(32767, 42));
        jPanelSepBlanc8spaces8.setMinimumSize(new Dimension(10, 42));
        jPanelSepBlanc8spaces8.setPreferredSize(new Dimension(1000, 42));
        jPanelMain.add(jPanelSepBlanc8spaces8);

        jPanelSepLine2New.setMaximumSize(new Dimension(32787, 10));
        jPanelSepLine2New.setMinimumSize(new Dimension(0, 10));
        jPanelSepLine2New.setPreferredSize(new Dimension(20, 10));
        jPanelSepLine2New.setLayout(new BoxLayout(jPanelSepLine2New, BoxLayout.LINE_AXIS));

        jPanel28.setMaximumSize(new Dimension(10, 5));
        jPanel28.setMinimumSize(new Dimension(10, 5));
        jPanel28.setPreferredSize(new Dimension(10, 5));

        GroupLayout jPanel28Layout = new GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(jPanel28Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(jPanel28Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelSepLine2New.add(jPanel28);
        jPanelSepLine2New.add(jSeparator2);

        jPanel29.setMaximumSize(new Dimension(10, 5));
        jPanel29.setMinimumSize(new Dimension(10, 5));

        GroupLayout jPanel29Layout = new GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(jPanel29Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(jPanel29Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelSepLine2New.add(jPanel29);

        jPanelMain.add(jPanelSepLine2New);

        jPanelBottom.setMaximumSize(new Dimension(65544, 45));
        jPanelBottom.setLayout(new BoxLayout(jPanelBottom, BoxLayout.LINE_AXIS));

        jPanel2.setMaximumSize(new Dimension(10, 10));
        jPanel2.setMinimumSize(new Dimension(10, 11));
        jPanel2.setPreferredSize(new Dimension(10, 11));
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.LINE_AXIS));
        jPanelBottom.add(jPanel2);

        jPanelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jButtonOk.setText("OK");
        jButtonOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonOk);

        jButtonApply.setText("Apply");
        jButtonApply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonApplyActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonApply);

        jButtonHelp.setText("Help");
        jButtonHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonHelpActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonHelp);

        jPanel1.setMaximumSize(new Dimension(1, 1));
        jPanel1.setMinimumSize(new Dimension(1, 1));
        jPanel1.setPreferredSize(new Dimension(0, 0));

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );

        jPanelButtons.add(jPanel1);

        jPanelBottom.add(jPanelButtons);

        jPanelMain.add(jPanelBottom);

        getContentPane().add(jPanelMain);

        jMenuFile.setText("File");

        jMenuItemServiceInstall.setText("Service Installation");
        jMenuItemServiceInstall.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItemServiceInstallActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemServiceInstall);

        jMenuItemClose.setText("Close");
        jMenuItemClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItemCloseActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemClose);

        jMenuItemQuit.setText("Quit");
        jMenuItemQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItemQuitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemQuit);

        jMenuBar1.add(jMenuFile);

        jMenuOptions.setText("Options");

        jMenuCheckForUpdates.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        jMenuCheckForUpdates.setText("Check for updates (new version)");
        jMenuCheckForUpdates.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuCheckForUpdatesActionPerformed(evt);
            }
        });
        jMenuOptions.add(jMenuCheckForUpdates);

        jMenuItemResetWindows.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        jMenuItemResetWindows.setText("Reset Windows");
        jMenuItemResetWindows.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItemResetWindowsActionPerformed(evt);
            }
        });
        jMenuOptions.add(jMenuItemResetWindows);

        jMenuBar1.add(jMenuOptions);

        jMenuHelp.setText("Help");

        jMenuItemHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        jMenuItemHelp.setIcon(new ImageIcon(getClass().getResource("/com/kawansoft/app/parms/images/symbol_questionmark_16.png"))); // NOI18N
        jMenuItemHelp.setText("Main Help");
        jMenuItemHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItemHelpActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemHelp);

        jMenuItemReleaseNotes.setText("Release Notes");
        jMenuItemReleaseNotes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItemReleaseNotesActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemReleaseNotes);
        jMenuHelp.add(jSeparator3);

        jMenuItemSystemInfo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
        jMenuItemSystemInfo.setIcon(new ImageIcon(getClass().getResource("/com/kawansoft/app/parms/images/about_16.png"))); // NOI18N
        jMenuItemSystemInfo.setText("System Info");
        jMenuItemSystemInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItemSystemInfoActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemSystemInfo);

        jMenuItemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
        jMenuItemAbout.setText("About");
        jMenuItemAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItemAboutActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemAbout);

        jMenuBar1.add(jMenuHelp);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed

        AceQLManagerUtil.printEvent(evt);
        actionOk();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonHelpActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonHelpActionPerformed
        AceQLManagerUtil.printEvent(evt);
        help();
    }//GEN-LAST:event_jButtonHelpActionPerformed

    public void help() {
        if (help != null) {
            help.dispose();
        }

        help = new Help(this, "help_aceql_manager");
    }

    private void jButtonApplyActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonApplyActionPerformed
        AceQLManagerUtil.printEvent(evt);
        actionApply();

    }//GEN-LAST:event_jButtonApplyActionPerformed

    private void jTextFieldPropertiesFileActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jTextFieldPropertiesFileActionPerformed
        AceQLManagerUtil.printEvent(evt);
    }//GEN-LAST:event_jTextFieldPropertiesFileActionPerformed

    private void jTextFieldHostActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jTextFieldHostActionPerformed
        AceQLManagerUtil.printEvent(evt);
    }//GEN-LAST:event_jTextFieldHostActionPerformed

    private void jTextFieldPortActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jTextFieldPortActionPerformed
        AceQLManagerUtil.printEvent(evt);
    }//GEN-LAST:event_jTextFieldPortActionPerformed

    private void jButtonURLMouseEntered(MouseEvent evt) {//GEN-FIRST:event_jButtonURLMouseEntered
        AceQLManagerUtil.printEvent(evt);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jButtonURLMouseEntered

    private void jButtonURLMouseExited(MouseEvent evt) {//GEN-FIRST:event_jButtonURLMouseExited
        AceQLManagerUtil.printEvent(evt);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_jButtonURLMouseExited

    private void jButtonURLActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonURLActionPerformed
        try {
            AceQLManagerUtil.printEvent(evt);
            Desktop.getDesktop().browse(new URI(jButtonURL.getText()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Unable to browse AceQL Server URL: " + CR_LF + ex.getMessage(),
                    ParmsConstants.APP_NAME,
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonURLActionPerformed

    private void jButtonBrowseActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseActionPerformed
        AceQLManagerUtil.printEvent(evt);
        jButtonApply.setEnabled(true);

        // AWT does not work (freeze) on Windows
        if (SystemUtils.IS_OS_WINDOWS) {
            if (WINDOWS_OK_WITH_AWT) {
                addPropertyFileWithAwt();
            } else {
                addPropertyFileWithSwing();
            }
        } else {
            addPropertyFileWithAwt();
        }
    }//GEN-LAST:event_jButtonBrowseActionPerformed

    private void jButtonEditActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonEditActionPerformed
        try {
            AceQLManagerUtil.printEvent(evt);
            jButtonApply.setEnabled(true);

            if (jTextFieldPropertiesFile.getText() == null || jTextFieldPropertiesFile.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid file",
                        ParmsConstants.APP_NAME,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            File file = new File(jTextFieldPropertiesFile.getText());

            if (!file.exists()) {
                JOptionPane.showMessageDialog(this,
                        "The file does not exists: " + CR_LF + file,
                        ParmsConstants.APP_NAME,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (SystemUtils.IS_OS_WINDOWS) {
                ProcessBuilder pb = new ProcessBuilder("Notepad.exe", file.toString());
                pb.start();

            } else {
                java.awt.Desktop dekstop = java.awt.Desktop.getDesktop();
                dekstop.edit(file);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Impossible to open the file: " + ex + "\n(" + ex.toString() + ")");
        }
    }//GEN-LAST:event_jButtonEditActionPerformed

    private void jButtonStartActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonStartActionPerformed
        AceQLManagerUtil.printEvent(evt);
        startStandard();
    }//GEN-LAST:event_jButtonStartActionPerformed

    private void jButtonStopActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonStopActionPerformed
        AceQLManagerUtil.printEvent(evt);
        stopStandardThreadStart();
    }//GEN-LAST:event_jButtonStopActionPerformed

    private void jButtonDisplayConsoleActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonDisplayConsoleActionPerformed
        AceQLManagerUtil.printEvent(evt);
        if (aceQLConsole == null) {
            aceQLConsole = new AceQLConsole();
        } else {
            aceQLConsole.setState(Frame.NORMAL);
            aceQLConsole.setVisible(true);
        }
    }//GEN-LAST:event_jButtonDisplayConsoleActionPerformed

    private void jMenuItemCloseActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseActionPerformed
        AceQLManagerUtil.printEvent(evt);
        closeOnExit();
    }//GEN-LAST:event_jMenuItemCloseActionPerformed

    private void jMenuItemQuitActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemQuitActionPerformed
        AceQLManagerUtil.printEvent(evt);
        doSystemExit();
    }//GEN-LAST:event_jMenuItemQuitActionPerformed

    private void jMenuItemHelpActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemHelpActionPerformed
        AceQLManagerUtil.printEvent(evt);
        help();
    }//GEN-LAST:event_jMenuItemHelpActionPerformed

    private void jMenuItemSystemInfoActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemSystemInfoActionPerformed
        AceQLManagerUtil.printEvent(evt);
        if (systemPropDisplayer != null) {
            systemPropDisplayer.dispose();
        }

        systemPropDisplayer = new SystemPropDisplayer(this);
    }//GEN-LAST:event_jMenuItemSystemInfoActionPerformed

    private void jMenuItemAboutActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutActionPerformed
        AceQLManagerUtil.printEvent(evt);
        if (aboutFrame != null) {
            aboutFrame.dispose();
        }

        aboutFrame = new AboutFrame(this);
    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    private void jMenuCheckForUpdatesActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuCheckForUpdatesActionPerformed

        try {
            String currentVersion = com.kawansoft.app.version.GuiVersionConstants.VERSION;

            String productType = org.kawanfw.sql.version.Version.PRODUCT.TYPE;
            productType = StringUtils.substringBefore(productType, " ");
            URL url = new URL("https://www.aceql.com/CheckForUpdates?version=" + currentVersion + "&edition=" + productType);

            Desktop desktop = Desktop.getDesktop();
            desktop.browse(url.toURI());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Impossible to display Check For Updates Page " + e.toString(), ParmsConstants.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuCheckForUpdatesActionPerformed

    private void jButtonStartServiceActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonStartServiceActionPerformed
        AceQLManagerUtil.printEvent(evt);
        startService();
    }//GEN-LAST:event_jButtonStartServiceActionPerformed

    private void jButtonStopServiceActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonStopServiceActionPerformed
        AceQLManagerUtil.printEvent(evt);
        stopService();
    }//GEN-LAST:event_jButtonStopServiceActionPerformed

    private void jButtonDisplayLogsActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonDisplayLogsActionPerformed
        AceQLManagerUtil.printEvent(evt);
        displayLogs();
    }//GEN-LAST:event_jButtonDisplayLogsActionPerformed

    private void jButtonServicesConsoleActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonServicesConsoleActionPerformed
        AceQLManagerUtil.printEvent(evt);
        windowsServiceManagementConsole();
    }//GEN-LAST:event_jButtonServicesConsoleActionPerformed

    private void jMenuItemServiceInstallActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemServiceInstallActionPerformed
        AceQLManagerUtil.printEvent(evt);
        serviceInstall();
    }//GEN-LAST:event_jMenuItemServiceInstallActionPerformed

    private void jMenuItemResetWindowsActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemResetWindowsActionPerformed
        AceQLManagerUtil.printEvent(evt);
        actionResetWindows();
    }//GEN-LAST:event_jMenuItemResetWindowsActionPerformed

    private void jMenuItemReleaseNotesActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jMenuItemReleaseNotesActionPerformed
        try {
            AceQLManagerUtil.printEvent(evt);
            String version = org.kawanfw.sql.version.VersionValues.VERSION;
            version = version.substring(1);
            URL url = new URL("https://www.aceql.com/rest/soft/" + version+ "/RELEASE-NOTES.txt");

            Desktop desktop = Desktop.getDesktop();
            desktop.browse(url.toURI());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Impossible to display Check For Updates Page " + e.toString(), ParmsConstants.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItemReleaseNotesActionPerformed

    private void jButtonDisplayClasspathActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonDisplayClasspathActionPerformed
        AceQLManagerUtil.printEvent(evt);
        displayClasspath();
    }//GEN-LAST:event_jButtonDisplayClasspathActionPerformed

    public static void setLookAndFeel() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        File lookAndFeelFile = new File(ParmsUtil.LOOK_AND_FEEL_TXT);
        if (lookAndFeelFile.exists()) {
            String lookAndFeel = FileUtils.readFileToString(lookAndFeelFile, Charset.defaultCharset());
            if (lookAndFeel != null && !lookAndFeel.isEmpty()) {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if (lookAndFeel.equalsIgnoreCase(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        return;
                    }
                }
            }
        } else // Case Windows
        if (SystemUtils.IS_OS_WINDOWS) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ex) {
                System.out.println("Failed loading L&F: ");
                System.out.println(ex);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            setLookAndFeel();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Start Error: " + ex.toString(), ParmsConstants.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AceQLManager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public JButton jButtonApply;
    public JButton jButtonBrowse;
    public JButton jButtonDisplayClasspath;
    public JButton jButtonDisplayConsole;
    public JButton jButtonDisplayLogs;
    public JButton jButtonEdit;
    public JButton jButtonHelp;
    public JButton jButtonOk;
    public JButton jButtonServicesConsole;
    public JButton jButtonStart;
    public JButton jButtonStartService;
    public JButton jButtonStop;
    public JButton jButtonStopService;
    public JButton jButtonURL;
    public JLabel jLabeStandardStatusValue;
    public JLabel jLabelClasspath;
    public JLabel jLabelHost;
    public JLabel jLabelHost1;
    public JLabel jLabelLogo;
    public JLabel jLabelPropertiesFile;
    public JLabel jLabelServiceStartModeValue;
    public JLabel jLabelServiceStatus;
    public JLabel jLabelServiceStatusValue;
    public JLabel jLabelStandardMode;
    public JLabel jLabelStandardStatus;
    public JLabel jLabelURL;
    public JLabel jLabelWindowsServiceMode;
    public JMenuBar jMenuBar1;
    public JMenuItem jMenuCheckForUpdates;
    public JMenu jMenuFile;
    public JMenu jMenuHelp;
    public JMenuItem jMenuItemAbout;
    public JMenuItem jMenuItemClose;
    public JMenuItem jMenuItemHelp;
    public JMenuItem jMenuItemQuit;
    public JMenuItem jMenuItemReleaseNotes;
    public JMenuItem jMenuItemResetWindows;
    public JMenuItem jMenuItemServiceInstall;
    public JMenuItem jMenuItemSystemInfo;
    public JMenu jMenuOptions;
    public JPanel jPaneBlanklLeft1;
    public JPanel jPaneBlanklLeft2;
    public JPanel jPaneSep2;
    public JPanel jPaneSepInstallAndStart1;
    public JPanel jPanel1;
    public JPanel jPanel2;
    public JPanel jPanel22;
    public JPanel jPanel23;
    public JPanel jPanel28;
    public JPanel jPanel29;
    public JPanel jPanelBlankRight1;
    public JPanel jPanelBlankRight2;
    public JPanel jPanelBottom;
    public JPanel jPanelButtonStartStop;
    public JPanel jPanelButtons;
    public JPanel jPanelButtonsStartService;
    public JPanel jPanelButtonsStartStandard;
    public JPanel jPanelClasspath;
    public JPanel jPanelEndField4;
    public JPanel jPanelEndField6;
    public JPanel jPanelHost;
    public JPanel jPanelLeft13;
    public JPanel jPanelLeft14;
    public JPanel jPanelLeft15;
    public JPanel jPanelLeft16;
    public JPanel jPanelLeft17;
    public JPanel jPanelLeft18;
    public JPanel jPanelLeft19;
    public JPanel jPanelLeft20;
    public JPanel jPanelLeft21;
    public JPanel jPanelLeft22;
    public JPanel jPanelLeft23;
    public JPanel jPanelLeft24;
    public JPanel jPanelLeft25;
    public JPanel jPanelLeft26;
    public JPanel jPanelLeft31;
    public JPanel jPanelLeft33;
    public JPanel jPanelLogo;
    public JPanel jPanelMain;
    public JPanel jPanelProperties;
    public JPanel jPanelRadioService;
    public JPanel jPanelRadioStandard;
    public JPanel jPanelSep3x6;
    public JPanel jPanelSep3x7;
    public JPanel jPanelSepBlanc8spaces;
    public JPanel jPanelSepBlanc8spaces1;
    public JPanel jPanelSepBlanc8spaces3;
    public JPanel jPanelSepBlanc8spaces4;
    public JPanel jPanelSepBlanc8spaces5;
    public JPanel jPanelSepBlanc8spaces8;
    public JPanel jPanelSepBlank11;
    public JPanel jPanelSepLine2New;
    public JPanel jPanelSepLine2New2;
    public JPanel jPanelTitledSeparator5;
    public JPanel jPanelTitledSeparator6;
    public JPanel jPanelURL;
    public JSeparator jSeparator2;
    public JPopupMenu.Separator jSeparator3;
    public JSeparator jSeparator4;
    public JTextField jTextFieldHost;
    public JTextField jTextFieldPort;
    public JTextField jTextFieldPropertiesFile;
    public JXTitledSeparator jXTitledSeparator20pixels1;
    public JXTitledSeparator jXTitledSeparator20pixels2;
    public JXTitledSeparator jXTitledSeparator5;
    public JXTitledSeparator jXTitledSeparator6;
    // End of variables declaration//GEN-END:variables

}

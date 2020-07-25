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

import com.kawansoft.aceql.gui.util.JdbcUtil;
import com.kawansoft.app.parms.util.ImageParmsUtil;
import com.kawansoft.app.util.ClipboardManager;
import com.kawansoft.app.util.WindowSettingMgr;
import com.kawansoft.app.util.classpath.ClasspathUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * Displays the orderd CLASSPATH with one element per line
 *
 * @author Nicolas de Pomereu
 */
public class ClasspathDisplayer extends javax.swing.JFrame {

    public static final String CR_LF = System.getProperty("line.separator");
    /**
     * Add a clipboard manager for content management
     */
    private ClipboardManager clipboard = null;
    private java.awt.Window parent = null;
    private Color light_red = new Color(255, 58, 58);

    public ClasspathDisplayer(Window parent) {
        super();
        this.parent = parent;
        initComponents();
        //
        // TODO: Add any constructor code after initializeComponent call
        //
        initializeIt();
        this.setVisible(true);
    }

    /**
     * This is the method to include in the constructor
     *
     */
    private JTextPane jtextPane2 = new JTextPane();

    public void initializeIt() {

        Dimension dim = new Dimension(933, 524);
        this.setPreferredSize(dim);
        this.setSize(dim);

        try {
            this.setIconImage(ImageParmsUtil.getAppIcon());
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }

        // Toolkit.getDefaultToolkit().setDynamicLayout(true);
        jCheckBoxShowPath.setSelected(true);

        buttonGroup1.add(jRadioButtonOriginalOrder);
        buttonGroup1.add(jRadioButtonSort);

        jRadioButtonOriginalOrder.setSelected(true);

        jPanelHelpMain.remove(jScrollPane1);

        /*
        jTextPane1 = new JTextPane() {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getUI().getPreferredSize(this).width
                        <= getParent().getSize().width;
            }
        };

        jScrollPane1.setViewportView(jTextPane1);
        jPanelHelpMain.add(jScrollPane1);
         */
        
        jtextPane2 = new JTextPane();
        JPanel noWrapPanel = new JPanel(new BorderLayout());
        noWrapPanel.add(jtextPane2);
        JScrollPane jScrollPane2 = new JScrollPane(noWrapPanel);
        jPanelHelpMain.add(jScrollPane2);

        jtextPane2.setContentType("text/html");
        jtextPane2.setEditable(false);
        jtextPane2.setFont(new java.awt.Font("Lucida Console", 0, 16)); // NOI18N
        jScrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Add a Clipboard Manager
        clipboard = new ClipboardManager(this.getContentPane());

        /*
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setClasspath(jCheckBoxShowPath.isSelected(), jRadioButtonSort.isSelected());
            }
        });
        */
        
        Thread thread = new Thread() {
            @Override
            public void run() {
                setClasspath(jCheckBoxShowPath.isSelected(), jRadioButtonSort.isSelected());
            }
        };

        thread.start();
        
       jCheckBoxShowPath.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                setClasspath(jCheckBoxShowPath.isSelected(), jRadioButtonSort.isSelected());
            }
        });
       
         jRadioButtonOriginalOrder.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                setClasspath(jCheckBoxShowPath.isSelected(), jRadioButtonSort.isSelected());
            }
        });
         
        jRadioButtonSort.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                setClasspath(jCheckBoxShowPath.isSelected(), jRadioButtonSort.isSelected());
            }
        });     
        
        //System.out.println(jEditorPane.getMargin());
        jtextPane2.setMargin(new Insets(3, 10, 3, 10));

        //Our key listeners
        jtextPane2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                keyReleasedActionPerformed(e);
            }
        });

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
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveSettings();
            }
        });

        // Load and activate previous windows settings
        // Defaults to upper left border
        WindowSettingMgr.load(this);

        this.jLabelTitle.setText("Display CLASSPATH");
        this.setTitle(this.jLabelTitle.getText());

        // These 2 stupid lines : only to Force to diplay top of file first
        jtextPane2.moveCaretPosition(0);
        jtextPane2.setSelectionEnd(0);

        this.pack();
        this.setVisible(true);

    }

    public void setClasspath(boolean displayDirectories, boolean sort) {
        
        boolean jdbcDriverFound = false;
        
        List<String> classpath = ClasspathUtil.getClasspath();

        List<String> finalClasspath = new ArrayList<>();

        for (String line : classpath) {
            if (displayDirectories) {
                    if (!new File(line).isDirectory()) {
                        
                        boolean isJdbcDriver = false;
                        
                        try {
                            isJdbcDriver = JdbcUtil.isJdbcDriver(line);
                        } catch (IOException ex) {
                            Logger.getLogger(ClasspathDisplayer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        if (isJdbcDriver) {
                            line = "<font color=blue>" + line + "</font>";
                            jdbcDriverFound = true;
                        }
                    }

                    finalClasspath.add("<font face=arial>" + line + "</font>");
            } else {

                    String element = StringUtils.substringAfterLast(line, File.separator);
                    
                    if (element == null || element.trim().isEmpty()) {
                        element = line;
                    }

                    if (!new File(line).isDirectory()) {
                        boolean isJdbcDriver = false;
                        
                        try {
                            isJdbcDriver = JdbcUtil.isJdbcDriver(line);
                        } catch (IOException ex) {
                            Logger.getLogger(ClasspathDisplayer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        if (isJdbcDriver) {
                            element = "<font color=blue>" + element + " </font>";
                            jdbcDriverFound = true;
                        }
                    }

                    finalClasspath.add("<font face=arial>" + element + "</font>");

            }
            
            if (jdbcDriverFound) {
                jLabelMessage.setText("<html>Found JDBC Drivers are displayed in <font color=blue>blue</font>.</html");
            }
            else {
                jLabelMessage.setText(null);
            }
            
        }

        // Security to be sure not to reuse old list
        classpath = null;

        if (sort) {
            Collections.sort(finalClasspath);
        }

        StringBuffer sb = new StringBuffer();

        for (String line : finalClasspath) {
            sb.append(line);
            sb.append("<br>");
            sb.append(CR_LF);
        }
        jtextPane2.setText(sb.toString());
    }

    public void saveSettings() {
        WindowSettingMgr.save(this);
    }

    ///////////////////////////////////////////////////////////////////////////
    // KEYS PART
    /////////////////////////////////////////////////////////////////////////// 
    private void keyReleasedActionPerformed(KeyEvent e) {
        //debug("this_keyReleased(KeyEvent e) " + e.getComponent().getName()); 

        int id = e.getID();
        if (id == KeyEvent.KEY_RELEASED) {
            int keyCode = e.getKeyCode();

            if (keyCode == KeyEvent.VK_ESCAPE) {
                this.dispose();
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanelBorderTop = new javax.swing.JPanel();
        jPanelOptions1 = new javax.swing.JPanel();
        jLabelTitle = new javax.swing.JLabel();
        jPanelOptionsMain = new javax.swing.JPanel();
        jPanelOptions = new javax.swing.JPanel();
        jPanelLeft22 = new javax.swing.JPanel();
        jCheckBoxShowPath = new javax.swing.JCheckBox();
        jPanelLeft23 = new javax.swing.JPanel();
        jLabelDisplay = new javax.swing.JLabel();
        jRadioButtonOriginalOrder = new javax.swing.JRadioButton();
        jRadioButtonSort = new javax.swing.JRadioButton();
        jPanelMessage = new javax.swing.JPanel();
        jLabelMessage = new javax.swing.JLabel();
        jPanelSep = new javax.swing.JPanel();
        jPanelHelp = new javax.swing.JPanel();
        jPanelHelpLeft = new javax.swing.JPanel();
        jPanelHelpMain = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanelHelpRight = new javax.swing.JPanel();
        jPanelBottom = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Aide");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanelBorderTop.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanelBorderTop.setPreferredSize(new java.awt.Dimension(20, 10));
        jPanelBorderTop.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));
        getContentPane().add(jPanelBorderTop);

        jPanelOptions1.setMaximumSize(new java.awt.Dimension(2147483647, 32));
        jPanelOptions1.setMinimumSize(new java.awt.Dimension(91, 32));
        jPanelOptions1.setPreferredSize(new java.awt.Dimension(191, 32));
        jPanelOptions1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));

        jLabelTitle.setText("Display CLASSPATH");
        jPanelOptions1.add(jLabelTitle);

        getContentPane().add(jPanelOptions1);

        jPanelOptionsMain.setMaximumSize(new java.awt.Dimension(2147483647, 32));
        jPanelOptionsMain.setLayout(new javax.swing.BoxLayout(jPanelOptionsMain, javax.swing.BoxLayout.LINE_AXIS));

        jPanelOptions.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanelLeft22.setMaximumSize(new java.awt.Dimension(0, 0));
        jPanelLeft22.setPreferredSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout jPanelLeft22Layout = new javax.swing.GroupLayout(jPanelLeft22);
        jPanelLeft22.setLayout(jPanelLeft22Layout);
        jPanelLeft22Layout.setHorizontalGroup(
            jPanelLeft22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelLeft22Layout.setVerticalGroup(
            jPanelLeft22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanelOptions.add(jPanelLeft22);

        jCheckBoxShowPath.setText("Show Full Path:");
        jCheckBoxShowPath.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jPanelOptions.add(jCheckBoxShowPath);

        jPanelLeft23.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanelLeft23.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanelLeft23.setPreferredSize(new java.awt.Dimension(5, 5));

        javax.swing.GroupLayout jPanelLeft23Layout = new javax.swing.GroupLayout(jPanelLeft23);
        jPanelLeft23.setLayout(jPanelLeft23Layout);
        jPanelLeft23Layout.setHorizontalGroup(
            jPanelLeft23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanelLeft23Layout.setVerticalGroup(
            jPanelLeft23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanelOptions.add(jPanelLeft23);

        jLabelDisplay.setText("Display Order:");
        jLabelDisplay.setToolTipText("");
        jPanelOptions.add(jLabelDisplay);

        jRadioButtonOriginalOrder.setText("Original");
        jPanelOptions.add(jRadioButtonOriginalOrder);

        jRadioButtonSort.setText("Sorted");
        jPanelOptions.add(jRadioButtonSort);

        jPanelOptionsMain.add(jPanelOptions);

        jPanelMessage.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING, 10, 9));

        jLabelMessage.setFont(new java.awt.Font("Tahoma", 2, 13)); // NOI18N
        jLabelMessage.setText("Found JDBC Drivers are displayed in blue.");
        jPanelMessage.add(jLabelMessage);

        jPanelOptionsMain.add(jPanelMessage);

        getContentPane().add(jPanelOptionsMain);

        jPanelSep.setMaximumSize(new java.awt.Dimension(32767, 5));
        jPanelSep.setMinimumSize(new java.awt.Dimension(0, 5));
        jPanelSep.setName(""); // NOI18N
        jPanelSep.setPreferredSize(new java.awt.Dimension(671, 5));

        javax.swing.GroupLayout jPanelSepLayout = new javax.swing.GroupLayout(jPanelSep);
        jPanelSep.setLayout(jPanelSepLayout);
        jPanelSepLayout.setHorizontalGroup(
            jPanelSepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 705, Short.MAX_VALUE)
        );
        jPanelSepLayout.setVerticalGroup(
            jPanelSepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelSep);

        jPanelHelp.setLayout(new javax.swing.BoxLayout(jPanelHelp, javax.swing.BoxLayout.LINE_AXIS));

        jPanelHelpLeft.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelHelp.add(jPanelHelpLeft);

        jPanelHelpMain.setPreferredSize(new java.awt.Dimension(319, 180));
        jPanelHelpMain.setLayout(new javax.swing.BoxLayout(jPanelHelpMain, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane1.setViewportView(jTextPane1);

        jPanelHelpMain.add(jScrollPane1);

        jPanelHelp.add(jPanelHelpMain);

        jPanelHelpRight.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelHelp.add(jPanelHelpRight);

        getContentPane().add(jPanelHelp);

        jPanelBottom.setMaximumSize(new java.awt.Dimension(10, 10));

        javax.swing.GroupLayout jPanelBottomLayout = new javax.swing.GroupLayout(jPanelBottom);
        jPanelBottom.setLayout(jPanelBottomLayout);
        jPanelBottomLayout.setHorizontalGroup(
            jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanelBottomLayout.setVerticalGroup(
            jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelBottom);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClasspathDisplayer(null);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox jCheckBoxShowPath;
    private javax.swing.JLabel jLabelDisplay;
    private javax.swing.JLabel jLabelMessage;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanelBorderTop;
    private javax.swing.JPanel jPanelBottom;
    private javax.swing.JPanel jPanelHelp;
    private javax.swing.JPanel jPanelHelpLeft;
    private javax.swing.JPanel jPanelHelpMain;
    private javax.swing.JPanel jPanelHelpRight;
    private javax.swing.JPanel jPanelLeft22;
    private javax.swing.JPanel jPanelLeft23;
    private javax.swing.JPanel jPanelMessage;
    private javax.swing.JPanel jPanelOptions;
    private javax.swing.JPanel jPanelOptions1;
    private javax.swing.JPanel jPanelOptionsMain;
    private javax.swing.JPanel jPanelSep;
    private javax.swing.JRadioButton jRadioButtonOriginalOrder;
    private javax.swing.JRadioButton jRadioButtonSort;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}

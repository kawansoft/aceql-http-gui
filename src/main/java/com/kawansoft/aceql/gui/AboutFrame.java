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

import com.kawansoft.app.parms.MessagesManager;
import com.kawansoft.app.parms.Parms;
import com.kawansoft.app.parms.util.ImageParmsUtil;
import com.kawansoft.app.util.ButtonResizer;
import com.kawansoft.app.util.SystemPropDisplayer;
import com.kawansoft.app.util.WindowSettingMgr;
import com.swing.util.SwingUtil;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.commons.lang3.SystemUtils;
import com.kawansoft.app.version.GuiVersion;

/** 
 *
 * @author Nicolas de Pomereu
 */
public class AboutFrame extends javax.swing.JFrame {

    public static final String CR_LF = System.getProperty("line.separator");

    /**
     * The parent Window
     */
    private Window parent = null;
    /**
     * The SystemInfo Windo
     */
    private SystemPropDisplayer systemPropDisplayer = null;
    private CreditsFrame creditsFrame;

    /**
     * Creates new form PostUploaderMain
     *
     * @param postUploaderTray
     */
    public AboutFrame(Window parent) {
        this.parent = parent;

        initComponents();
        initializeIt();

        this.setVisible(true);

    }

    /**
     * This is the method to include in *our* constructor
     */
    public void initializeIt() {

        Dimension dimBase = new Dimension(388, 388);
        this.setPreferredSize(dimBase);
        //this.setSize(dim);

        try {
            this.setIconImage(ImageParmsUtil.getAppIcon());
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }
        
        jPanelCenter.setBorder(javax.swing.BorderFactory.createTitledBorder(MessagesManager.get("about")));
        
        this.jLabelVersion.setText(org.kawanfw.sql.version.Version.getServerVersion());
        this.jLabelVersionGui.setText(GuiVersion.getVersion());
        this.jLabelCopyright .setText("<html>" + GuiVersion.VENDOR.COPYRIGHT + "</html>");

        jLabelLogo.setIcon(ImageParmsUtil.getSmallLogo());
        jButtonUrl.setText(Parms.ABOUT_WEB_SITE);
        jButtonEmailSupport.setText(Parms.ABOUT_EMAIL_SUPPORT);
                
        ButtonResizer buttonResizer = new ButtonResizer(jPanelButtons);
        buttonResizer.setWidthToMax();

        SwingUtil.resizeJComponentsForNimbusAndMacOsX(rootPane);

        // For Mac OS X behavior (rounded default buttons)
        jButtonUrl.putClientProperty("JButton.buttonType", "square");
        jButtonEmailSupport.putClientProperty("JButton.buttonType", "square");
        jButtonCredits.putClientProperty("JButton.buttonType", "square");
        jButtonDeveloppedBy.putClientProperty("JButton.buttonType", "square");

        // Because URL buttons are wider on MAC OS X 
        if (SystemUtils.IS_OS_MAC_OSX) {
            jPanelUrlTrail.setPreferredSize(new Dimension(0, 10));
            jPanelBugReportTrail.setPreferredSize(new Dimension(0, 10));
            jPanelCreditsTrail.setPreferredSize(new Dimension(0, 10));
            jPanelKawanSoftTrail.setPreferredSize(new Dimension(0, 10));
        }

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

        this.keyListenerAdder();

        this.setTitle(MessagesManager.get("about_from"));

        // Load and activate previous windows settings
        this.setLocationRelativeTo(parent);

        // Necessary to update the buttons width
        //Dimension dim = this.getPreferredSize();
        //this.setPreferredSize(new Dimension(dim.width + 1, dim.height));

        // Load and activate previous windows settings
        WindowSettingMgr.load(this);
        
        pack();
    }


   public void saveSettings() {
        WindowSettingMgr.save(this);
    }

    private void closeOnExit() {
        saveSettings();
        this.dispose();
    }

    public void callSystemInfo() {
        if (systemPropDisplayer != null) {
            systemPropDisplayer.dispose();
        }

        systemPropDisplayer = new SystemPropDisplayer(this);
    }

    /**
     * Universal key listener
     */
    private void keyListenerAdder() {
        List<Component> components = SwingUtil.getAllComponants(this);

        for (int i = 0; i < components.size(); i++) {
            Component comp = components.get(i);

            comp.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    this_keyReleased(e);
                }
            });
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // KEYS PART
    ///////////////////////////////////////////////////////////////////////////    
    private void this_keyReleased(KeyEvent e) {
        //System.out.println("this_keyReleased(KeyEvent e) " + e.getComponent().getName()); 
        int id = e.getID();
        if (id == KeyEvent.KEY_RELEASED) {
            int keyCode = e.getKeyCode();

            if (keyCode == KeyEvent.VK_ENTER) {
                callSystemInfo();
            }

            if (keyCode == KeyEvent.VK_ESCAPE) {
                closeOnExit();
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

        jPanelLogos = new javax.swing.JPanel();
        jLabelLogo = new javax.swing.JLabel();
        jPanelBorderLeft1 = new javax.swing.JPanel();
        jPanelTop1 = new javax.swing.JPanel();
        jPanelMain = new javax.swing.JPanel();
        jPanelBorderLeft = new javax.swing.JPanel();
        jPanelCenter = new javax.swing.JPanel();
        jPanelTop = new javax.swing.JPanel();
        jPanelName = new javax.swing.JPanel();
        jLabelVersion = new javax.swing.JLabel();
        jPanelNameGui = new javax.swing.JPanel();
        jLabelVersionGui = new javax.swing.JLabel();
        jPanelCopyRight = new javax.swing.JPanel();
        jLabelCopyright = new javax.swing.JLabel();
        jPanelUrl = new javax.swing.JPanel();
        jButtonUrl = new javax.swing.JButton();
        jPanelUrlTrail = new javax.swing.JPanel();
        jPanelBugReport = new javax.swing.JPanel();
        jLabelSupport = new javax.swing.JLabel();
        jPanelSep5 = new javax.swing.JPanel();
        jButtonEmailSupport = new javax.swing.JButton();
        jPanelBugReportTrail = new javax.swing.JPanel();
        jPanelCredits = new javax.swing.JPanel();
        jButtonCredits = new javax.swing.JButton();
        jPanelCreditsTrail = new javax.swing.JPanel();
        jPanelKawanSoft = new javax.swing.JPanel();
        jButtonDeveloppedBy = new javax.swing.JButton();
        jPanelKawanSoftTrail = new javax.swing.JPanel();
        jPanelBorderRight = new javax.swing.JPanel();
        jPanelButtons = new javax.swing.JPanel();
        jButtonSystemInfo = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanelLogos.setMaximumSize(new java.awt.Dimension(32767, 55));
        jPanelLogos.setMinimumSize(new java.awt.Dimension(213, 54));
        jPanelLogos.setPreferredSize(new java.awt.Dimension(213, 54));
        jPanelLogos.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING, 0, 10));

        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/kawansoft/app/parms/images/logos/logo-AceQL_48.png"))); // NOI18N
        jPanelLogos.add(jLabelLogo);

        jPanelBorderLeft1.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelLogos.add(jPanelBorderLeft1);

        getContentPane().add(jPanelLogos);

        jPanelTop1.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelTop1.setLayout(new javax.swing.BoxLayout(jPanelTop1, javax.swing.BoxLayout.LINE_AXIS));
        getContentPane().add(jPanelTop1);

        jPanelMain.setLayout(new javax.swing.BoxLayout(jPanelMain, javax.swing.BoxLayout.X_AXIS));

        jPanelBorderLeft.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelMain.add(jPanelBorderLeft);

        jPanelCenter.setBorder(javax.swing.BorderFactory.createTitledBorder("A Propos"));
        jPanelCenter.setMaximumSize(new java.awt.Dimension(32783, 600));
        jPanelCenter.setMinimumSize(new java.awt.Dimension(290, 600));
        jPanelCenter.setLayout(new javax.swing.BoxLayout(jPanelCenter, javax.swing.BoxLayout.Y_AXIS));

        jPanelTop.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelTop.setLayout(new javax.swing.BoxLayout(jPanelTop, javax.swing.BoxLayout.LINE_AXIS));
        jPanelCenter.add(jPanelTop);

        jPanelName.setMaximumSize(new java.awt.Dimension(32767, 24));
        jPanelName.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabelVersion.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelVersion.setText("TheApp v1.00 - 12/02/16");
        jPanelName.add(jLabelVersion);

        jPanelCenter.add(jPanelName);

        jPanelNameGui.setMaximumSize(new java.awt.Dimension(32767, 24));
        jPanelNameGui.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabelVersionGui.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelVersionGui.setText("TheApp v1.00 - 12/02/16");
        jPanelNameGui.add(jLabelVersionGui);

        jPanelCenter.add(jPanelNameGui);

        jPanelCopyRight.setMaximumSize(new java.awt.Dimension(32767, 24));
        jPanelCopyRight.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabelCopyright.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelCopyright.setText("Copyright  2016");
        jPanelCopyRight.add(jLabelCopyright);

        jPanelCenter.add(jPanelCopyRight);

        jPanelUrl.setMaximumSize(new java.awt.Dimension(32767, 24));
        jPanelUrl.setPreferredSize(new java.awt.Dimension(89, 24));
        jPanelUrl.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 5));

        jButtonUrl.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonUrl.setForeground(new java.awt.Color(51, 0, 255));
        jButtonUrl.setToolTipText("");
        jButtonUrl.setBorderPainted(false);
        jButtonUrl.setContentAreaFilled(false);
        jButtonUrl.setFocusPainted(false);
        jButtonUrl.setIconTextGap(0);
        jButtonUrl.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonUrl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonUrlMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonUrlMouseExited(evt);
            }
        });
        jButtonUrl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUrlActionPerformed(evt);
            }
        });
        jPanelUrl.add(jButtonUrl);

        jPanelUrlTrail.setPreferredSize(new java.awt.Dimension(3, 10));
        jPanelUrl.add(jPanelUrlTrail);

        jPanelCenter.add(jPanelUrl);

        jPanelBugReport.setMaximumSize(new java.awt.Dimension(32767, 24));
        jPanelBugReport.setPreferredSize(new java.awt.Dimension(242, 24));
        jPanelBugReport.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 5));

        jLabelSupport.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelSupport.setText("Support / Signaler un bug :");
        jPanelBugReport.add(jLabelSupport);

        jPanelSep5.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanelSep5.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanelSep5.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanelBugReport.add(jPanelSep5);

        jButtonEmailSupport.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonEmailSupport.setForeground(new java.awt.Color(51, 0, 255));
        jButtonEmailSupport.setBorderPainted(false);
        jButtonEmailSupport.setContentAreaFilled(false);
        jButtonEmailSupport.setFocusPainted(false);
        jButtonEmailSupport.setIconTextGap(0);
        jButtonEmailSupport.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonEmailSupport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonEmailSupportMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonEmailSupportMouseExited(evt);
            }
        });
        jButtonEmailSupport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEmailSupportActionPerformed(evt);
            }
        });
        jPanelBugReport.add(jButtonEmailSupport);

        jPanelBugReportTrail.setPreferredSize(new java.awt.Dimension(3, 10));
        jPanelBugReport.add(jPanelBugReportTrail);

        jPanelCenter.add(jPanelBugReport);

        jPanelCredits.setMaximumSize(new java.awt.Dimension(32767, 24));
        jPanelCredits.setPreferredSize(new java.awt.Dimension(242, 24));
        jPanelCredits.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 5));

        jButtonCredits.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButtonCredits.setForeground(new java.awt.Color(51, 0, 255));
        jButtonCredits.setText("Autres Logiciels");
        jButtonCredits.setBorderPainted(false);
        jButtonCredits.setContentAreaFilled(false);
        jButtonCredits.setFocusPainted(false);
        jButtonCredits.setIconTextGap(0);
        jButtonCredits.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonCredits.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonCreditsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonCreditsMouseExited(evt);
            }
        });
        jButtonCredits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreditsActionPerformed(evt);
            }
        });
        jPanelCredits.add(jButtonCredits);

        jPanelCreditsTrail.setPreferredSize(new java.awt.Dimension(3, 10));
        jPanelCredits.add(jPanelCreditsTrail);

        jPanelCenter.add(jPanelCredits);

        jPanelKawanSoft.setMaximumSize(new java.awt.Dimension(32767, 24));
        jPanelKawanSoft.setPreferredSize(new java.awt.Dimension(242, 24));
        jPanelKawanSoft.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 5));

        jButtonDeveloppedBy.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        jButtonDeveloppedBy.setForeground(new java.awt.Color(51, 0, 255));
        jButtonDeveloppedBy.setText("Logiciel développé par KawanSoft");
        jButtonDeveloppedBy.setBorderPainted(false);
        jButtonDeveloppedBy.setContentAreaFilled(false);
        jButtonDeveloppedBy.setFocusPainted(false);
        jButtonDeveloppedBy.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButtonDeveloppedBy.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButtonDeveloppedBy.setIconTextGap(0);
        jButtonDeveloppedBy.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButtonDeveloppedBy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonDeveloppedByMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonDeveloppedByMouseExited(evt);
            }
        });
        jButtonDeveloppedBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeveloppedByActionPerformed(evt);
            }
        });
        jPanelKawanSoft.add(jButtonDeveloppedBy);

        jPanelKawanSoftTrail.setPreferredSize(new java.awt.Dimension(3, 10));
        jPanelKawanSoft.add(jPanelKawanSoftTrail);

        jPanelCenter.add(jPanelKawanSoft);

        jPanelMain.add(jPanelCenter);

        jPanelBorderRight.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelMain.add(jPanelBorderRight);

        getContentPane().add(jPanelMain);

        jPanelButtons.setMaximumSize(new java.awt.Dimension(32767, 65));
        jPanelButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 10));

        jButtonSystemInfo.setText("Infos Système");
        jButtonSystemInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSystemInfoActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonSystemInfo);

        jButtonClose.setText("Fermer");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonClose);

        jPanel4.setMaximumSize(new java.awt.Dimension(0, 0));
        jPanel4.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel4.setPreferredSize(new java.awt.Dimension(0, 0));
        jPanelButtons.add(jPanel4);

        getContentPane().add(jPanelButtons);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButtonSystemInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSystemInfoActionPerformed
    callSystemInfo();
}//GEN-LAST:event_jButtonSystemInfoActionPerformed

private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
    closeOnExit();
}//GEN-LAST:event_jButtonCloseActionPerformed

private void jButtonUrlMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonUrlMouseEntered
    this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
}//GEN-LAST:event_jButtonUrlMouseEntered

private void jButtonUrlMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonUrlMouseExited
    this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_jButtonUrlMouseExited

private void jButtonEmailSupportMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonEmailSupportMouseEntered
    this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
}//GEN-LAST:event_jButtonEmailSupportMouseEntered

private void jButtonEmailSupportMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonEmailSupportMouseExited
    this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_jButtonEmailSupportMouseExited

private void jButtonUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUrlActionPerformed
    Desktop desktop = Desktop.getDesktop();
    String url = "http://" + Parms.ABOUT_WEB_SITE;

    try {
        desktop.browse(new URI(url));
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Impossible to access website " + url + "\n(" + e.toString() + ")");
    }
}//GEN-LAST:event_jButtonUrlActionPerformed

private void jButtonEmailSupportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEmailSupportActionPerformed
    Desktop desktop = Desktop.getDesktop();
    String email = "mailto:" + Parms.ABOUT_EMAIL_SUPPORT;

    try {
        desktop.mail(new URI(email));
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Impossible to send an email\n(" + e.toString() + ")");
    }
}//GEN-LAST:event_jButtonEmailSupportActionPerformed

private void jButtonDeveloppedByMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDeveloppedByMouseEntered
    this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
}//GEN-LAST:event_jButtonDeveloppedByMouseEntered

private void jButtonDeveloppedByMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDeveloppedByMouseExited
    this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_jButtonDeveloppedByMouseExited

private void jButtonDeveloppedByActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeveloppedByActionPerformed
    Desktop desktop = Desktop.getDesktop();
    String url = "http://www.kawansoft.com";

    try {
        desktop.browse(new URI(url));
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Impossible to access website " + url + "\n(" + e.toString() + ")");
    }
}//GEN-LAST:event_jButtonDeveloppedByActionPerformed

    private void jButtonCreditsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCreditsMouseEntered
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jButtonCreditsMouseEntered

    private void jButtonCreditsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCreditsMouseExited
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_jButtonCreditsMouseExited

    private void jButtonCreditsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreditsActionPerformed
        if (creditsFrame != null) {
            creditsFrame.dispose();
        }

        creditsFrame = new CreditsFrame(this, Parms.APP_NAME);
    }//GEN-LAST:event_jButtonCreditsActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);

                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Exception ex) {
                    System.out.println("Failed loading L&F: ");
                    System.out.println(ex);
                }

                new AboutFrame(null);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonCredits;
    private javax.swing.JButton jButtonDeveloppedBy;
    private javax.swing.JButton jButtonEmailSupport;
    private javax.swing.JButton jButtonSystemInfo;
    private javax.swing.JButton jButtonUrl;
    private javax.swing.JLabel jLabelCopyright;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelSupport;
    private javax.swing.JLabel jLabelVersion;
    private javax.swing.JLabel jLabelVersionGui;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelBorderLeft;
    private javax.swing.JPanel jPanelBorderLeft1;
    private javax.swing.JPanel jPanelBorderRight;
    private javax.swing.JPanel jPanelBugReport;
    private javax.swing.JPanel jPanelBugReportTrail;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelCenter;
    private javax.swing.JPanel jPanelCopyRight;
    private javax.swing.JPanel jPanelCredits;
    private javax.swing.JPanel jPanelCreditsTrail;
    private javax.swing.JPanel jPanelKawanSoft;
    private javax.swing.JPanel jPanelKawanSoftTrail;
    private javax.swing.JPanel jPanelLogos;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelName;
    private javax.swing.JPanel jPanelNameGui;
    private javax.swing.JPanel jPanelSep5;
    private javax.swing.JPanel jPanelTop;
    private javax.swing.JPanel jPanelTop1;
    private javax.swing.JPanel jPanelUrl;
    private javax.swing.JPanel jPanelUrlTrail;
    // End of variables declaration//GEN-END:variables

}

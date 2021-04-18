/*
 * This file is part of AceQL HTTP.
 * AceQL HTTP: SQL Over HTTP                                     
 * Copyright (C) 2021,  KawanSoft SAS
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

import com.kawansoft.aceql.gui.util.AceQLManagerUtil;
import com.kawansoft.app.parms.ParmsConstants;
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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/** 
 *
 * @author Nicolas de Pomereu
 */
public class AboutFrame extends JFrame {

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

        // Do first part of init to avoid long method
        initStart();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                AceQLManagerUtil.debugEvent(e);
                saveSettings();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                AceQLManagerUtil.debugEvent(e);
                saveSettings();
            }
        });
                
        // Our window listener for all events
        // If window is closed ==> call close()
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                AceQLManagerUtil.debugEvent(e);
                closeOnExit();
            }
        });

        this.keyListenerAdder();

        this.setTitle("About");

        // Load and activate previous windows settings
        this.setLocationRelativeTo(parent);

        // Necessary to update the buttons width
        //Dimension dim = this.getPreferredSize();
        //this.setPreferredSize(new Dimension(dim.width + 1, dim.height));

        // Load and activate previous windows settings
        WindowSettingMgr.load(this);
        
        pack();
    }

    private void initStart() {
        Dimension dimBase = new Dimension(388, 388);
        this.setPreferredSize(dimBase);
        //this.setSize(dim);
        
        try {
            this.setIconImage(ImageParmsUtil.getAppIcon());
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }
        
        jPanelCenter.setBorder(BorderFactory.createTitledBorder("About"));
        
        jLabelVersion.setText(org.kawanfw.sql.version.Version.getVersion());
        this.jLabelVersionGui.setText(GuiVersion.getVersion());
        this.jLabelCopyright .setText("<html>" + GuiVersion.VENDOR.COPYRIGHT + "</html>");
        
        jLabelLogo.setIcon(ImageParmsUtil.getSmallLogo());
        jButtonUrl.setText(ParmsConstants.ABOUT_WEB_SITE);
        jButtonEmailSupport.setText(ParmsConstants.ABOUT_EMAIL_SUPPORT);
        
        ButtonResizer buttonResizer = new ButtonResizer(jPanelButtons);
        buttonResizer.setWidthToMax();
        
        SwingUtil.resizeJComponentsForNimbusAndMacOsX(rootPane);
        
        // For Mac OS X behavior (rounded default buttons)
        jButtonUrl.putClientProperty("JButton.buttonType", "square");
        jButtonEmailSupport.putClientProperty("JButton.buttonType", "square");
        jButtonCredits.putClientProperty("JButton.buttonType", "square");
        jButtonDeveloppedBy.putClientProperty("JButton.buttonType", "square");
        
        this.jButtonUrl.setForeground(ThemeUtil.getHyperLinkColor());
        this.jButtonEmailSupport.setForeground(ThemeUtil.getHyperLinkColor());
        
        this.jButtonCredits.setForeground(ThemeUtil.getHyperLinkColor());
        this.jButtonDeveloppedBy.setForeground(ThemeUtil.getHyperLinkColor());
                
        // Because URL buttons are wider on MAC OS X
        if (SystemUtils.IS_OS_MAC_OSX) {
            jPanelUrlTrail.setPreferredSize(new Dimension(0, 10));
            jPanelBugReportTrail.setPreferredSize(new Dimension(0, 10));
            jPanelCreditsTrail.setPreferredSize(new Dimension(0, 10));
            jPanelKawanSoftTrail.setPreferredSize(new Dimension(0, 10));
        }
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

        jPanelLogos = new JPanel();
        jLabelLogo = new JLabel();
        jPanelBorderLeft1 = new JPanel();
        jPanelTop1 = new JPanel();
        jPanelMain = new JPanel();
        jPanelBorderLeft = new JPanel();
        jPanelCenter = new JPanel();
        jPanelTop = new JPanel();
        jPanelName = new JPanel();
        jLabelVersion = new JLabel();
        jPanelNameGui = new JPanel();
        jLabelVersionGui = new JLabel();
        jPanelCopyRight = new JPanel();
        jLabelCopyright = new JLabel();
        jPanelUrl = new JPanel();
        jButtonUrl = new JButton();
        jPanelUrlTrail = new JPanel();
        jPanelBugReport = new JPanel();
        jLabelSupport = new JLabel();
        jPanelSep5 = new JPanel();
        jButtonEmailSupport = new JButton();
        jPanelBugReportTrail = new JPanel();
        jPanelCredits = new JPanel();
        jButtonCredits = new JButton();
        jPanelCreditsTrail = new JPanel();
        jPanelKawanSoft = new JPanel();
        jButtonDeveloppedBy = new JButton();
        jPanelKawanSoftTrail = new JPanel();
        jPanelBorderRight = new JPanel();
        jPanelButtons = new JPanel();
        jButtonSystemInfo = new JButton();
        jButtonClose = new JButton();
        jPanel4 = new JPanel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        jPanelLogos.setMaximumSize(new Dimension(32767, 55));
        jPanelLogos.setMinimumSize(new Dimension(213, 54));
        jPanelLogos.setPreferredSize(new Dimension(213, 54));
        jPanelLogos.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 10));

        jLabelLogo.setIcon(new ImageIcon(getClass().getResource("/com/kawansoft/app/parms/images/logos/logo-AceQL_48.png"))); // NOI18N
        jPanelLogos.add(jLabelLogo);

        jPanelBorderLeft1.setMaximumSize(new Dimension(10, 10));
        jPanelLogos.add(jPanelBorderLeft1);

        getContentPane().add(jPanelLogos);

        jPanelTop1.setMaximumSize(new Dimension(10, 10));
        jPanelTop1.setLayout(new BoxLayout(jPanelTop1, BoxLayout.LINE_AXIS));
        getContentPane().add(jPanelTop1);

        jPanelMain.setLayout(new BoxLayout(jPanelMain, BoxLayout.X_AXIS));

        jPanelBorderLeft.setMaximumSize(new Dimension(10, 10));
        jPanelMain.add(jPanelBorderLeft);

        jPanelCenter.setBorder(BorderFactory.createTitledBorder("A Propos"));
        jPanelCenter.setMaximumSize(new Dimension(32783, 600));
        jPanelCenter.setMinimumSize(new Dimension(290, 600));
        jPanelCenter.setLayout(new BoxLayout(jPanelCenter, BoxLayout.Y_AXIS));

        jPanelTop.setMaximumSize(new Dimension(10, 10));
        jPanelTop.setLayout(new BoxLayout(jPanelTop, BoxLayout.LINE_AXIS));
        jPanelCenter.add(jPanelTop);

        jPanelName.setMaximumSize(new Dimension(32767, 24));
        jPanelName.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jLabelVersion.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jLabelVersion.setText("TheApp v1.00 - 12/02/16");
        jPanelName.add(jLabelVersion);

        jPanelCenter.add(jPanelName);

        jPanelNameGui.setMaximumSize(new Dimension(32767, 24));
        jPanelNameGui.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jLabelVersionGui.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jLabelVersionGui.setText("TheApp v1.00 - 12/02/16");
        jPanelNameGui.add(jLabelVersionGui);

        jPanelCenter.add(jPanelNameGui);

        jPanelCopyRight.setMaximumSize(new Dimension(32767, 24));
        jPanelCopyRight.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jLabelCopyright.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jLabelCopyright.setText("Copyright  2021");
        jPanelCopyRight.add(jLabelCopyright);

        jPanelCenter.add(jPanelCopyRight);

        jPanelUrl.setMaximumSize(new Dimension(32767, 24));
        jPanelUrl.setPreferredSize(new Dimension(89, 24));
        jPanelUrl.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 5));

        jButtonUrl.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jButtonUrl.setForeground(new Color(51, 0, 255));
        jButtonUrl.setToolTipText("");
        jButtonUrl.setBorderPainted(false);
        jButtonUrl.setContentAreaFilled(false);
        jButtonUrl.setFocusPainted(false);
        jButtonUrl.setIconTextGap(0);
        jButtonUrl.setMargin(new Insets(0, 0, 0, 0));
        jButtonUrl.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                jButtonUrlMouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                jButtonUrlMouseExited(evt);
            }
        });
        jButtonUrl.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonUrlActionPerformed(evt);
            }
        });
        jPanelUrl.add(jButtonUrl);

        jPanelUrlTrail.setPreferredSize(new Dimension(3, 10));
        jPanelUrl.add(jPanelUrlTrail);

        jPanelCenter.add(jPanelUrl);

        jPanelBugReport.setMaximumSize(new Dimension(32767, 24));
        jPanelBugReport.setPreferredSize(new Dimension(242, 24));
        jPanelBugReport.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 5));

        jLabelSupport.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jLabelSupport.setText("Support / Report a Bug :");
        jPanelBugReport.add(jLabelSupport);

        jPanelSep5.setMaximumSize(new Dimension(5, 5));
        jPanelSep5.setMinimumSize(new Dimension(5, 5));
        jPanelSep5.setPreferredSize(new Dimension(5, 5));
        jPanelBugReport.add(jPanelSep5);

        jButtonEmailSupport.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jButtonEmailSupport.setForeground(new Color(51, 0, 255));
        jButtonEmailSupport.setBorderPainted(false);
        jButtonEmailSupport.setContentAreaFilled(false);
        jButtonEmailSupport.setFocusPainted(false);
        jButtonEmailSupport.setIconTextGap(0);
        jButtonEmailSupport.setMargin(new Insets(0, 0, 0, 0));
        jButtonEmailSupport.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                jButtonEmailSupportMouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                jButtonEmailSupportMouseExited(evt);
            }
        });
        jButtonEmailSupport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonEmailSupportActionPerformed(evt);
            }
        });
        jPanelBugReport.add(jButtonEmailSupport);

        jPanelBugReportTrail.setPreferredSize(new Dimension(3, 10));
        jPanelBugReport.add(jPanelBugReportTrail);

        jPanelCenter.add(jPanelBugReport);

        jPanelCredits.setMaximumSize(new Dimension(32767, 24));
        jPanelCredits.setPreferredSize(new Dimension(242, 24));
        jPanelCredits.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 5));

        jButtonCredits.setFont(new Font("Tahoma", 0, 12)); // NOI18N
        jButtonCredits.setForeground(new Color(51, 0, 255));
        jButtonCredits.setText("Other Software");
        jButtonCredits.setBorderPainted(false);
        jButtonCredits.setContentAreaFilled(false);
        jButtonCredits.setFocusPainted(false);
        jButtonCredits.setIconTextGap(0);
        jButtonCredits.setMargin(new Insets(0, 0, 0, 0));
        jButtonCredits.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                jButtonCreditsMouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                jButtonCreditsMouseExited(evt);
            }
        });
        jButtonCredits.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonCreditsActionPerformed(evt);
            }
        });
        jPanelCredits.add(jButtonCredits);

        jPanelCreditsTrail.setPreferredSize(new Dimension(3, 10));
        jPanelCredits.add(jPanelCreditsTrail);

        jPanelCenter.add(jPanelCredits);

        jPanelKawanSoft.setMaximumSize(new Dimension(32767, 24));
        jPanelKawanSoft.setPreferredSize(new Dimension(242, 24));
        jPanelKawanSoft.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 5));

        jButtonDeveloppedBy.setFont(new Font("Tahoma", 2, 12)); // NOI18N
        jButtonDeveloppedBy.setForeground(new Color(51, 0, 255));
        jButtonDeveloppedBy.setText("Software Developed by KawanSoft");
        jButtonDeveloppedBy.setBorderPainted(false);
        jButtonDeveloppedBy.setContentAreaFilled(false);
        jButtonDeveloppedBy.setFocusPainted(false);
        jButtonDeveloppedBy.setHorizontalAlignment(SwingConstants.LEFT);
        jButtonDeveloppedBy.setHorizontalTextPosition(SwingConstants.LEFT);
        jButtonDeveloppedBy.setIconTextGap(0);
        jButtonDeveloppedBy.setMargin(new Insets(0, 0, 0, 0));
        jButtonDeveloppedBy.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                jButtonDeveloppedByMouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                jButtonDeveloppedByMouseExited(evt);
            }
        });
        jButtonDeveloppedBy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonDeveloppedByActionPerformed(evt);
            }
        });
        jPanelKawanSoft.add(jButtonDeveloppedBy);

        jPanelKawanSoftTrail.setPreferredSize(new Dimension(3, 10));
        jPanelKawanSoft.add(jPanelKawanSoftTrail);

        jPanelCenter.add(jPanelKawanSoft);

        jPanelMain.add(jPanelCenter);

        jPanelBorderRight.setMaximumSize(new Dimension(10, 10));
        jPanelMain.add(jPanelBorderRight);

        getContentPane().add(jPanelMain);

        jPanelButtons.setMaximumSize(new Dimension(32767, 65));
        jPanelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 10));

        jButtonSystemInfo.setText("System Info");
        jButtonSystemInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonSystemInfoActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonSystemInfo);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonClose);

        jPanel4.setMaximumSize(new Dimension(0, 0));
        jPanel4.setMinimumSize(new Dimension(0, 0));
        jPanel4.setPreferredSize(new Dimension(0, 0));
        jPanelButtons.add(jPanel4);

        getContentPane().add(jPanelButtons);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButtonSystemInfoActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonSystemInfoActionPerformed
    AceQLManagerUtil.debugEvent(evt);
    callSystemInfo();
}//GEN-LAST:event_jButtonSystemInfoActionPerformed

private void jButtonCloseActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
    AceQLManagerUtil.debugEvent(evt);
    closeOnExit();
}//GEN-LAST:event_jButtonCloseActionPerformed

private void jButtonUrlMouseEntered(MouseEvent evt) {//GEN-FIRST:event_jButtonUrlMouseEntered
    AceQLManagerUtil.debugEvent(evt);
    this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
}//GEN-LAST:event_jButtonUrlMouseEntered

private void jButtonUrlMouseExited(MouseEvent evt) {//GEN-FIRST:event_jButtonUrlMouseExited
    AceQLManagerUtil.debugEvent(evt);
    this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_jButtonUrlMouseExited

private void jButtonEmailSupportMouseEntered(MouseEvent evt) {//GEN-FIRST:event_jButtonEmailSupportMouseEntered
    AceQLManagerUtil.debugEvent(evt);
    this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
}//GEN-LAST:event_jButtonEmailSupportMouseEntered

private void jButtonEmailSupportMouseExited(MouseEvent evt) {//GEN-FIRST:event_jButtonEmailSupportMouseExited
    AceQLManagerUtil.debugEvent(evt);
    this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_jButtonEmailSupportMouseExited

private void jButtonUrlActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonUrlActionPerformed
    AceQLManagerUtil.debugEvent(evt);
    Desktop desktop = Desktop.getDesktop();
    String url = "http://" + ParmsConstants.ABOUT_WEB_SITE;

    try {
        desktop.browse(new URI(url));
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Impossible to access website " + url + "\n(" + e.toString() + ")");
    }
}//GEN-LAST:event_jButtonUrlActionPerformed

private void jButtonEmailSupportActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonEmailSupportActionPerformed
    AceQLManagerUtil.debugEvent(evt);
    Desktop desktop = Desktop.getDesktop();
    String email = "mailto:" + ParmsConstants.ABOUT_EMAIL_SUPPORT;

    try {
        desktop.mail(new URI(email));
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Impossible to send an email\n(" + e.toString() + ")");
    }
}//GEN-LAST:event_jButtonEmailSupportActionPerformed

private void jButtonDeveloppedByMouseEntered(MouseEvent evt) {//GEN-FIRST:event_jButtonDeveloppedByMouseEntered
    AceQLManagerUtil.debugEvent(evt);
    this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
}//GEN-LAST:event_jButtonDeveloppedByMouseEntered

private void jButtonDeveloppedByMouseExited(MouseEvent evt) {//GEN-FIRST:event_jButtonDeveloppedByMouseExited
    AceQLManagerUtil.debugEvent(evt);
    this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_jButtonDeveloppedByMouseExited

private void jButtonDeveloppedByActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonDeveloppedByActionPerformed
    AceQLManagerUtil.debugEvent(evt);
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

    private void jButtonCreditsMouseEntered(MouseEvent evt) {//GEN-FIRST:event_jButtonCreditsMouseEntered
        AceQLManagerUtil.debugEvent(evt);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jButtonCreditsMouseEntered

    private void jButtonCreditsMouseExited(MouseEvent evt) {//GEN-FIRST:event_jButtonCreditsMouseExited
        AceQLManagerUtil.debugEvent(evt);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_jButtonCreditsMouseExited

    private void jButtonCreditsActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonCreditsActionPerformed
        AceQLManagerUtil.debugEvent(evt);        
        if (creditsFrame != null) {
            creditsFrame.dispose();
        }
        creditsFrame = new CreditsFrame(this, ParmsConstants.APP_NAME);
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
    public JButton jButtonClose;
    public JButton jButtonCredits;
    public JButton jButtonDeveloppedBy;
    public JButton jButtonEmailSupport;
    public JButton jButtonSystemInfo;
    public JButton jButtonUrl;
    public JLabel jLabelCopyright;
    public JLabel jLabelLogo;
    public JLabel jLabelSupport;
    public JLabel jLabelVersion;
    public JLabel jLabelVersionGui;
    public JPanel jPanel4;
    public JPanel jPanelBorderLeft;
    public JPanel jPanelBorderLeft1;
    public JPanel jPanelBorderRight;
    public JPanel jPanelBugReport;
    public JPanel jPanelBugReportTrail;
    public JPanel jPanelButtons;
    public JPanel jPanelCenter;
    public JPanel jPanelCopyRight;
    public JPanel jPanelCredits;
    public JPanel jPanelCreditsTrail;
    public JPanel jPanelKawanSoft;
    public JPanel jPanelKawanSoftTrail;
    public JPanel jPanelLogos;
    public JPanel jPanelMain;
    public JPanel jPanelName;
    public JPanel jPanelNameGui;
    public JPanel jPanelSep5;
    public JPanel jPanelTop;
    public JPanel jPanelTop1;
    public JPanel jPanelUrl;
    public JPanel jPanelUrlTrail;
    // End of variables declaration//GEN-END:variables

}

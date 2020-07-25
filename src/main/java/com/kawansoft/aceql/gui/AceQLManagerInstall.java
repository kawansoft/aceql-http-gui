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
import com.kawansoft.aceql.gui.util.AceQLManagerUtil;
import com.kawansoft.app.parms.util.ImageParmsUtil;
import com.kawansoft.app.parms.util.ParmsUtil;
import com.kawansoft.app.util.ButtonResizer;
import com.kawansoft.app.util.Help;
import com.kawansoft.app.util.WindowSettingMgr;
import com.swing.util.SwingUtil;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;
import org.apache.commons.lang3.SystemUtils;


/**
 *
 * @author Nicolas de Pomereu
 */
public class AceQLManagerInstall extends javax.swing.JFrame {

    /**
     * Says if we continue to udate Windows Service Status
     */
    private static boolean UPDATE_SERVICE_STATUS_RUNING = false;

    private Help help = null;
    private JFrame thisOne = this;

   private Window parentJframe = null;
    /**
     * Windows Service Status
     */
    private int serviceStatus = ServiceUtil.NOT_INSTALLED;

    /**
     * Creates new form AceQLManagerInstall
     */
    public AceQLManagerInstall(Window parentJframe) {
        this.parentJframe = parentJframe;
        initComponents();
        initializeIt();
    }

    /**
     * This is the method to include in *our* constructor(s)
     */
    public void initializeIt() {

        Dimension dim = new Dimension(577, 283);
        this.setPreferredSize(dim);
        this.setSize(dim);

        this.jLabelLogo.setText("Windows Service Installation");

        try {
            this.setIconImage(ImageParmsUtil.getAppIcon());
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }

        if (SystemUtils.IS_OS_WINDOWS) {
            updateServiceStatusThreadStart();
        } else {
            jLabelServiceStatusValue.setText("Not installed");
            jLabelServiceStatusValue
                    .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_GREY_PNG));

            jButtonInstallService.setEnabled(false);
            jButtonUninstallService.setEnabled(false);
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

        this.keyListenerAdder();
        this.setTitle(jLabelLogo.getText());

        ButtonResizer buttonResizer1 = new ButtonResizer(jPaneServiceInstall);
        buttonResizer1.setWidthToMax();

        ButtonResizer buttonResizer2 = new ButtonResizer(jPanelButtons);
        buttonResizer2.setWidthToMax();

        // Load and activate previous windows settings
        WindowSettingMgr.load(this);

        pack();
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
                closeOnExit();
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

    private void closeOnExit() {
        saveSettings();
        updateServiceStatusThreadStop();
        this.setVisible(false);
        this.dispose();
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
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(AceQLManager.class.getName()).log(
                        Level.SEVERE, null, ex);
            }

            serviceStatus = ServiceUtil
                    .getServiceStatus(ServiceUtil.ACEQL_HTTP_SERVICE);
            String startupTypeLabel = ServiceUtil
                    .getServiceStartupTypeLabel(ServiceUtil.ACEQL_HTTP_SERVICE);

            String startupTypeText = "";
            if (!startupTypeLabel.isEmpty()) {
                startupTypeText = "    - Startup type: " + startupTypeLabel;
            }

            if (serviceStatus == ServiceUtil.NOT_INSTALLED) {
                jLabelServiceStatusValue.setText("Not Installed");

                jLabelServiceStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_GREY_PNG));

                jButtonInstallService.setEnabled(true);

                jButtonUninstallService.setEnabled(false);

            } else if (serviceStatus == ServiceUtil.STOPPED) {
                jLabelServiceStatusValue.setText("Stopped" + startupTypeText);

                jLabelServiceStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_RED_PNG));

                jButtonInstallService.setEnabled(false);
                jButtonUninstallService.setEnabled(true);
            } else if (serviceStatus == ServiceUtil.STARTING) {
                jLabelServiceStatusValue.setText("Starting..."
                        + startupTypeText);

                jLabelServiceStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_YELLOW_PNG));

                jButtonInstallService.setEnabled(false);
                jButtonUninstallService.setEnabled(true);
            } else if (serviceStatus == ServiceUtil.STOPPING) {
                jLabelServiceStatusValue.setText("Stopping..."
                        + startupTypeText);

                jLabelServiceStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_YELLOW_PNG));

                jButtonInstallService.setEnabled(false);
                jButtonUninstallService.setEnabled(true);
            } else if (serviceStatus == ServiceUtil.RUNNING) {
                jLabelServiceStatusValue.setText("Started" + startupTypeText);

                jLabelServiceStatusValue
                        .setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.IMAGES_BULLET_BALL_GREEN_PNG));

                jButtonInstallService.setEnabled(false);
                jButtonUninstallService.setEnabled(true);
            }

        }

    }

    private void installService() {
            
        try {
            ServiceInstaller.installService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Unable to install Windows Service: "
                    + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            ServiceInstaller.updateServiceClasspath();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Unable to update Windows Service: "
                    + e.getMessage());
            e.printStackTrace();
        }

    }

    private void uninstallService() {

        try {
            ServiceInstaller.uninstallService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Unable to uninstall Windows Service: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    private void help() {
        if (help != null) {
            help.dispose();
        }

        help = new Help(this, "help_aceql_manager_install");
    }

    private void actionOk() {
        closeOnExit();
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
        jPanelRadioService = new JPanel();
        jPanelLeft25 = new JPanel();
        jLabelWindowsServiceMode = new JLabel();
        jLabelServiceStatus = new JLabel();
        jLabelServiceStatusValue = new JLabel();
        jPaneServiceInstall = new JPanel();
        jPanelLeft33 = new JPanel();
        jButtonInstallService = new JButton();
        jButtonUninstallService = new JButton();
        jPanelSepBlanc8spaces8 = new JPanel();
        jPanelSepLine2New = new JPanel();
        jPanel28 = new JPanel();
        jSeparator2 = new JSeparator();
        jPanel29 = new JPanel();
        jPanelButtons = new JPanel();
        jButtonOk = new JButton();
        jButtonHelp = new JButton();
        jPanel1 = new JPanel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));

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

        jPanelRadioService.setMaximumSize(new Dimension(2147483647, 32));
        jPanelRadioService.setMinimumSize(new Dimension(91, 32));
        jPanelRadioService.setPreferredSize(new Dimension(191, 32));
        jPanelRadioService.setLayout(new FlowLayout(FlowLayout.LEFT));

        jPanelLeft25.setMaximumSize(new Dimension(0, 0));

        GroupLayout jPanelLeft25Layout = new GroupLayout(jPanelLeft25);
        jPanelLeft25.setLayout(jPanelLeft25Layout);
        jPanelLeft25Layout.setHorizontalGroup(jPanelLeft25Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelLeft25Layout.setVerticalGroup(jPanelLeft25Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanelRadioService.add(jPanelLeft25);

        jLabelWindowsServiceMode.setText("Service mode \"AceQL HTTP Server\" Service");
        jPanelRadioService.add(jLabelWindowsServiceMode);

        jLabelServiceStatus.setText(" - Status:");
        jPanelRadioService.add(jLabelServiceStatus);
        jPanelRadioService.add(jLabelServiceStatusValue);

        jPanelMain.add(jPanelRadioService);

        jPaneServiceInstall.setMaximumSize(new Dimension(2147483647, 32));
        jPaneServiceInstall.setMinimumSize(new Dimension(91, 32));
        jPaneServiceInstall.setPreferredSize(new Dimension(191, 32));
        jPaneServiceInstall.setLayout(new FlowLayout(FlowLayout.LEFT));

        jPanelLeft33.setMaximumSize(new Dimension(15, 10));
        jPanelLeft33.setMinimumSize(new Dimension(15, 10));

        GroupLayout jPanelLeft33Layout = new GroupLayout(jPanelLeft33);
        jPanelLeft33.setLayout(jPanelLeft33Layout);
        jPanelLeft33Layout.setHorizontalGroup(jPanelLeft33Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanelLeft33Layout.setVerticalGroup(jPanelLeft33Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPaneServiceInstall.add(jPanelLeft33);

        jButtonInstallService.setText("Install Service ");
        jButtonInstallService.setToolTipText("");
        jButtonInstallService.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonInstallServiceActionPerformed(evt);
            }
        });
        jPaneServiceInstall.add(jButtonInstallService);

        jButtonUninstallService.setText("Uninstall Service");
        jButtonUninstallService.setToolTipText("");
        jButtonUninstallService.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonUninstallServiceActionPerformed(evt);
            }
        });
        jPaneServiceInstall.add(jButtonUninstallService);

        jPanelMain.add(jPaneServiceInstall);

        jPanelSepBlanc8spaces8.setMaximumSize(new Dimension(32767, 34));
        jPanelSepBlanc8spaces8.setMinimumSize(new Dimension(10, 34));
        jPanelSepBlanc8spaces8.setPreferredSize(new Dimension(1000, 34));
        jPanelMain.add(jPanelSepBlanc8spaces8);

        jPanelSepLine2New.setMaximumSize(new Dimension(32787, 10));
        jPanelSepLine2New.setMinimumSize(new Dimension(0, 10));
        jPanelSepLine2New.setPreferredSize(new Dimension(20, 10));
        jPanelSepLine2New.setLayout(new BoxLayout(jPanelSepLine2New, BoxLayout.LINE_AXIS));

        jPanel28.setMaximumSize(new Dimension(10, 5));
        jPanel28.setMinimumSize(new Dimension(10, 5));

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

        jPanelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jButtonOk.setText("OK");
        jButtonOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonOk);

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

        jPanelMain.add(jPanelButtons);

        getContentPane().add(jPanelMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonInstallServiceActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonInstallServiceActionPerformed
        AceQLManagerUtil.printEvent(evt);
        installService();
    }//GEN-LAST:event_jButtonInstallServiceActionPerformed

    private void jButtonUninstallServiceActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonUninstallServiceActionPerformed
        AceQLManagerUtil.printEvent(evt);
        uninstallService();
    }//GEN-LAST:event_jButtonUninstallServiceActionPerformed

    private void jButtonOkActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        AceQLManagerUtil.printEvent(evt);
        actionOk();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonHelpActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonHelpActionPerformed
        AceQLManagerUtil.printEvent(evt);
        help();
    }//GEN-LAST:event_jButtonHelpActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AceQLManagerInstall.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AceQLManagerInstall.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AceQLManagerInstall.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AceQLManagerInstall.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AceQLManagerInstall(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public JButton jButtonHelp;
    public JButton jButtonInstallService;
    public JButton jButtonOk;
    public JButton jButtonUninstallService;
    public JLabel jLabelLogo;
    public JLabel jLabelServiceStatus;
    public JLabel jLabelServiceStatusValue;
    public JLabel jLabelWindowsServiceMode;
    public JPanel jPaneServiceInstall;
    public JPanel jPanel1;
    public JPanel jPanel22;
    public JPanel jPanel23;
    public JPanel jPanel28;
    public JPanel jPanel29;
    public JPanel jPanelButtons;
    public JPanel jPanelLeft25;
    public JPanel jPanelLeft33;
    public JPanel jPanelLogo;
    public JPanel jPanelMain;
    public JPanel jPanelRadioService;
    public JPanel jPanelSepBlanc8spaces1;
    public JPanel jPanelSepBlanc8spaces8;
    public JPanel jPanelSepBlank11;
    public JPanel jPanelSepLine2New;
    public JPanel jPanelSepLine2New2;
    public JSeparator jSeparator2;
    public JSeparator jSeparator4;
    // End of variables declaration//GEN-END:variables

}

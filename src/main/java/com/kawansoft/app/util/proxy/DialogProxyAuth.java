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
package com.kawansoft.app.util.proxy;

import com.kawansoft.aceql.gui.util.AceQLManagerUtil;
import com.kawansoft.app.parms.MessagesManager;
import com.kawansoft.app.parms.ParmsConstants;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import com.kawansoft.app.parms.util.ImageParmsUtil;
import com.kawansoft.app.util.ButtonResizer;
import com.kawansoft.app.util.ClipboardManager;
import com.kawansoft.app.util.WindowSettingMgr;
import com.kawansoft.app.util.preference.AppPreferencesManager;
import com.kawansoft.app.util.preference.DefaultAppPreferencesManager;
import com.swing.util.SwingUtil;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 *
 * @author Alexandre Becquereau
 */
public class DialogProxyAuth extends javax.swing.JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 7475004633125793748L;

    /** if true, uuse has canceled the form */
    private boolean isCancelled = true;

    /** The proxy login */
    private String proxyUsername = null;

    /** The proxy password */
    private String proxyPassword = null;

    public ClipboardManager clipboardManager;
    private final AppPreferencesManager appUserPreference;
    
        /** Creates new form JDialogProxyAuth
     * @param parent the value of parent
     * @param appUserPreference the value of appUserPreference */
    
    public DialogProxyAuth(Window parent) {
        super(parent);
        this.appUserPreference = new DefaultAppPreferencesManager();
        initComponents();
        initCompany();
        this.setLocationRelativeTo(parent);
        
    }
    
    /** Creates new form JDialogProxyAuth
     * @param parent the value of parent
     * @param appUserPreference the value of appUserPreference */
    
    public DialogProxyAuth(Window parent, AppPreferencesManager appUserPreference) {
        super(parent);
        this.appUserPreference = appUserPreference;
        initComponents();
        initCompany();
        this.setLocationRelativeTo(parent);
        
    }

    private void initCompany(){
        clipboardManager = new ClipboardManager(rootPane);
        
        /*
        this.jLabelTitle.setText(messages.getMessage("proxy_authentification"));
        this.jLabelMessage.setText(messages.getMessage("proxy_requires_authentification"));
        this.jLabelUsername.setText(messages.getMessage("username"));
        this.jLabelPassword.setText(messages.getMessage("password_2"));
        this.jCheckBoxRememberInfo.setText(messages.getMessage("remember_information"));
        this.jButtonCancel.setText(messages.getMessage("cancel"));
        this.jButtonOk.setText(messages.getMessage("ok"));
        */
        
        boolean rememberInfo = appUserPreference.getBooleanPreference(AppPreferencesManager.PROXY_AUTH_REMEMBER_INFO);
        
        this.jCheckBoxRememberInfo.setSelected(rememberInfo);

        this.jTextFieldUsername.setText(null);
        this.jPasswordField.setText(null);
        
        if (jCheckBoxRememberInfo.isSelected())
        {
            this.jTextFieldUsername.setText(appUserPreference.getPreference(AppPreferencesManager.PROXY_AUTH_USERNAME));
            try {
                this.jPasswordField.setText(appUserPreference.getPreferenceDecrypt(AppPreferencesManager.PROXY_AUTH_PASSWORD));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        try {
            this.setIconImage(ImageParmsUtil.getAppIcon());
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }
        
        keyListenerAdder();
        
        ButtonResizer buttonResizer = new ButtonResizer(jPanelButtons);
        buttonResizer.setWidthToMax();
        SwingUtil.resizeJComponentsForNimbusAndMacOsX(rootPane);
        
        WindowSettingMgr.load(this);
        
        // Must be done after load that sets the label values dynamically
        this.setTitle(jLabelTitle.getText());
                
        pack();
        
    }

    /**
     * Universal key listener
     */
    private void keyListenerAdder()
    {
        List<Component> components = SwingUtil.getAllComponants(this);

        for (int i = 0; i < components.size(); i++)
        {
            Component comp = components.get(i);

            comp.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e)
                {
                    thisKeyPressed(e);
                }
            });
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // KEYS PART
    ///////////////////////////////////////////////////////////////////////////

    private void thisKeyPressed(KeyEvent e)
    {
        int id = e.getID();
        if (id == KeyEvent.KEY_PRESSED)
        {
            int keyCode = e.getKeyCode();

            if (keyCode == KeyEvent.VK_ENTER)
            {
                doIt();
            }

            if (keyCode == KeyEvent.VK_ESCAPE)
            {
                jButtonCancelActionPerformed(null);
            }

        }
    }

    
    private void doIt(){

        appUserPreference.setPreference(AppPreferencesManager.PROXY_AUTH_REMEMBER_INFO, jCheckBoxRememberInfo.isSelected());

        this.proxyUsername = jTextFieldUsername.getText();
        this.proxyPassword = new String(jPasswordField.getPassword());
        
        if (this.proxyUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, MessagesManager.get("please_enter_a_username"), ParmsConstants.APP_NAME, JOptionPane.ERROR_MESSAGE);
            jTextFieldUsername.requestFocusInWindow();
            return;
        }
        
        if (this.jPasswordField.getPassword().length  == 0) {
            JOptionPane.showMessageDialog(this, MessagesManager.get("please_enter_a_password"), ParmsConstants.APP_NAME, JOptionPane.ERROR_MESSAGE);
            jPasswordField.requestFocusInWindow();
            return;
        }
        
        this.isCancelled = false;

        if (jCheckBoxRememberInfo.isSelected())
        {
            appUserPreference.setPreference(AppPreferencesManager.PROXY_AUTH_USERNAME, proxyUsername);
            int keyLength = 128;
            appUserPreference.setPreferenceEncrypt(keyLength, AppPreferencesManager.PROXY_AUTH_PASSWORD, proxyPassword);
        }
        else
        {
            appUserPreference.removePreference(AppPreferencesManager.PROXY_AUTH_USERNAME);
            appUserPreference.removePreference(AppPreferencesManager.PROXY_AUTH_PASSWORD);
        }

        dispose();
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * @return the proxyLogin
     */
    public String getProxyUsername() {
        return proxyUsername;
    }

    /**
     * @return the proxyPassword
     */
    public String getProxyPassword() {
        return proxyPassword;
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelNorth = new JPanel();
        jPanelCenter = new JPanel();
        jPanelTitle = new JPanel();
        jLabelTitle = new JLabel();
        jPanelSepLine1 = new JPanel();
        jPanel15 = new JPanel();
        jSeparator3 = new JSeparator();
        jPanel16 = new JPanel();
        jPanel2 = new JPanel();
        jPanel1 = new JPanel();
        jLabelMessage = new JLabel();
        jPanelSep = new JPanel();
        jPanelUsername = new JPanel();
        jLabelUsername = new JLabel();
        jTextFieldUsername = new JTextField();
        jPanelPassword = new JPanel();
        jLabelPassword = new JLabel();
        jPasswordField = new JPasswordField();
        jPanelRememberPassword = new JPanel();
        jLabelPassword1 = new JLabel();
        jCheckBoxRememberInfo = new JCheckBox();
        jPanelSepBlank = new JPanel();
        jPanelSepLine = new JPanel();
        jPanel8 = new JPanel();
        jSeparator2 = new JSeparator();
        jPanel11 = new JPanel();
        jPanelButtons = new JPanel();
        jButtonOk = new JButton();
        jButtonCancel = new JButton();
        jPanel12 = new JPanel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(jPanelNorth);

        jPanelCenter.setLayout(new BoxLayout(jPanelCenter, BoxLayout.Y_AXIS));

        jPanelTitle.setMaximumSize(new Dimension(32767, 45));
        jPanelTitle.setMinimumSize(new Dimension(153, 45));
        jPanelTitle.setPreferredSize(new Dimension(384, 45));
        jPanelTitle.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        jLabelTitle.setText("Proxy avec Authentification");
        jPanelTitle.add(jLabelTitle);

        jPanelCenter.add(jPanelTitle);

        jPanelSepLine1.setMaximumSize(new Dimension(32767, 10));
        jPanelSepLine1.setLayout(new BoxLayout(jPanelSepLine1, BoxLayout.LINE_AXIS));

        jPanel15.setMaximumSize(new Dimension(10, 10));
        jPanelSepLine1.add(jPanel15);
        jPanelSepLine1.add(jSeparator3);

        jPanel16.setMaximumSize(new Dimension(10, 10));
        jPanelSepLine1.add(jPanel16);

        jPanelCenter.add(jPanelSepLine1);

        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.Y_AXIS));

        jPanel1.setMaximumSize(new Dimension(32767, 30));
        jPanel1.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        jLabelMessage.setText("Une authentification est requise pour le proxy :");
        jPanel1.add(jLabelMessage);

        jPanel2.add(jPanel1);

        jPanelSep.setMaximumSize(new Dimension(32767, 5));
        jPanelSep.setMinimumSize(new Dimension(10, 5));
        jPanelSep.setPreferredSize(new Dimension(10, 5));
        jPanel2.add(jPanelSep);

        jPanelUsername.setLayout(new FlowLayout(FlowLayout.LEFT));

        jLabelUsername.setHorizontalAlignment(SwingConstants.TRAILING);
        jLabelUsername.setText("Nom d'utilisateur");
        jLabelUsername.setPreferredSize(new Dimension(160, 16));
        jPanelUsername.add(jLabelUsername);

        jTextFieldUsername.setText("jTextFieldLogin");
        jTextFieldUsername.setPreferredSize(new Dimension(200, 20));
        jPanelUsername.add(jTextFieldUsername);

        jPanel2.add(jPanelUsername);

        jPanelPassword.setPreferredSize(new Dimension(100, 39));
        jPanelPassword.setLayout(new FlowLayout(FlowLayout.LEFT));

        jLabelPassword.setHorizontalAlignment(SwingConstants.TRAILING);
        jLabelPassword.setText("Mot de passe");
        jLabelPassword.setPreferredSize(new Dimension(160, 16));
        jPanelPassword.add(jLabelPassword);

        jPasswordField.setText("jPasswordField1");
        jPasswordField.setMinimumSize(new Dimension(200, 20));
        jPasswordField.setPreferredSize(new Dimension(200, 20));
        jPanelPassword.add(jPasswordField);

        jPanel2.add(jPanelPassword);

        jPanelRememberPassword.setPreferredSize(new Dimension(100, 39));
        jPanelRememberPassword.setLayout(new FlowLayout(FlowLayout.LEFT));

        jLabelPassword1.setHorizontalAlignment(SwingConstants.TRAILING);
        jLabelPassword1.setPreferredSize(new Dimension(160, 14));
        jPanelRememberPassword.add(jLabelPassword1);

        jCheckBoxRememberInfo.setText("Mémoriser ces informations");
        jCheckBoxRememberInfo.setMargin(new Insets(0, 0, 0, 0));
        jPanelRememberPassword.add(jCheckBoxRememberInfo);

        jPanel2.add(jPanelRememberPassword);

        jPanelCenter.add(jPanel2);

        jPanelSepBlank.setMaximumSize(new Dimension(32767, 10));
        jPanelSepBlank.setPreferredSize(new Dimension(0, 10));
        jPanelSepBlank.setLayout(new BoxLayout(jPanelSepBlank, BoxLayout.LINE_AXIS));
        jPanelCenter.add(jPanelSepBlank);

        jPanelSepLine.setMaximumSize(new Dimension(32767, 10));
        jPanelSepLine.setLayout(new BoxLayout(jPanelSepLine, BoxLayout.LINE_AXIS));

        jPanel8.setMaximumSize(new Dimension(10, 10));
        jPanelSepLine.add(jPanel8);
        jPanelSepLine.add(jSeparator2);

        jPanel11.setMaximumSize(new Dimension(10, 10));
        jPanelSepLine.add(jPanel11);

        jPanelCenter.add(jPanelSepLine);

        getContentPane().add(jPanelCenter);

        jPanelButtons.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 10));

        jButtonOk.setText("Ok");
        jButtonOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonOk);

        jButtonCancel.setText("Annuler");
        jButtonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonCancel);

        jPanel12.setMaximumSize(new Dimension(0, 10));
        jPanel12.setMinimumSize(new Dimension(0, 10));
        jPanel12.setPreferredSize(new Dimension(0, 10));
        jPanel12.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
        jPanelButtons.add(jPanel12);

        getContentPane().add(jPanelButtons);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        AceQLManagerUtil.debugEvent(evt);
        doIt();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        AceQLManagerUtil.debugEvent(evt);
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (Exception ex)
        {
            System.out.println("Failed loading L&F: ");
            System.out.println(ex);
        }
            
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogProxyAuth dialog = new DialogProxyAuth(new javax.swing.JFrame());
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        AceQLManagerUtil.systemExitWrapper();
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public JButton jButtonCancel;
    public JButton jButtonOk;
    public JCheckBox jCheckBoxRememberInfo;
    public JLabel jLabelMessage;
    public JLabel jLabelPassword;
    public JLabel jLabelPassword1;
    public JLabel jLabelTitle;
    public JLabel jLabelUsername;
    public JPanel jPanel1;
    public JPanel jPanel11;
    public JPanel jPanel12;
    public JPanel jPanel15;
    public JPanel jPanel16;
    public JPanel jPanel2;
    public JPanel jPanel8;
    public JPanel jPanelButtons;
    public JPanel jPanelCenter;
    public JPanel jPanelNorth;
    public JPanel jPanelPassword;
    public JPanel jPanelRememberPassword;
    public JPanel jPanelSep;
    public JPanel jPanelSepBlank;
    public JPanel jPanelSepLine;
    public JPanel jPanelSepLine1;
    public JPanel jPanelTitle;
    public JPanel jPanelUsername;
    public JPasswordField jPasswordField;
    public JSeparator jSeparator2;
    public JSeparator jSeparator3;
    public JTextField jTextFieldUsername;
    // End of variables declaration//GEN-END:variables

}

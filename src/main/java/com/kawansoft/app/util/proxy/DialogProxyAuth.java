
/*
 * Dialog to ask for proxy authentication
 *
 * Created on 1 juil. 2010, 10:25:49
 */
package com.kawansoft.app.util.proxy;

import com.kawansoft.app.parms.MessagesManager;
import com.kawansoft.app.parms.Parms;
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
import javax.swing.JOptionPane;

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

    ClipboardManager clipboardManager;
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
                    this_keyPressed(e);
                }
            });
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // KEYS PART
    ///////////////////////////////////////////////////////////////////////////

    private void this_keyPressed(KeyEvent e)
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
            JOptionPane.showMessageDialog(this, MessagesManager.get("please_enter_a_username"), Parms.APP_NAME, JOptionPane.ERROR_MESSAGE);
            jTextFieldUsername.requestFocusInWindow();
            return;
        }
        
        if (this.jPasswordField.getPassword().length  == 0) {
            JOptionPane.showMessageDialog(this, MessagesManager.get("please_enter_a_password"), Parms.APP_NAME, JOptionPane.ERROR_MESSAGE);
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

        jPanelNorth = new javax.swing.JPanel();
        jPanelCenter = new javax.swing.JPanel();
        jPanelTitle = new javax.swing.JPanel();
        jLabelTitle = new javax.swing.JLabel();
        jPanelSepLine1 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel16 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabelMessage = new javax.swing.JLabel();
        jPanelSep = new javax.swing.JPanel();
        jPanelUsername = new javax.swing.JPanel();
        jLabelUsername = new javax.swing.JLabel();
        jTextFieldUsername = new javax.swing.JTextField();
        jPanelPassword = new javax.swing.JPanel();
        jLabelPassword = new javax.swing.JLabel();
        jPasswordField = new javax.swing.JPasswordField();
        jPanelRememberPassword = new javax.swing.JPanel();
        jLabelPassword1 = new javax.swing.JLabel();
        jCheckBoxRememberInfo = new javax.swing.JCheckBox();
        jPanelSepBlank = new javax.swing.JPanel();
        jPanelSepLine = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel11 = new javax.swing.JPanel();
        jPanelButtons = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));
        getContentPane().add(jPanelNorth);

        jPanelCenter.setLayout(new javax.swing.BoxLayout(jPanelCenter, javax.swing.BoxLayout.Y_AXIS));

        jPanelTitle.setMaximumSize(new java.awt.Dimension(32767, 45));
        jPanelTitle.setMinimumSize(new java.awt.Dimension(153, 45));
        jPanelTitle.setPreferredSize(new java.awt.Dimension(384, 45));
        jPanelTitle.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));

        jLabelTitle.setText("Proxy avec Authentification");
        jPanelTitle.add(jLabelTitle);

        jPanelCenter.add(jPanelTitle);

        jPanelSepLine1.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanelSepLine1.setLayout(new javax.swing.BoxLayout(jPanelSepLine1, javax.swing.BoxLayout.LINE_AXIS));

        jPanel15.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelSepLine1.add(jPanel15);
        jPanelSepLine1.add(jSeparator3);

        jPanel16.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelSepLine1.add(jPanel16);

        jPanelCenter.add(jPanelSepLine1);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));

        jLabelMessage.setText("Une authentification est requise pour le proxy :");
        jPanel1.add(jLabelMessage);

        jPanel2.add(jPanel1);

        jPanelSep.setMaximumSize(new java.awt.Dimension(32767, 5));
        jPanelSep.setMinimumSize(new java.awt.Dimension(10, 5));
        jPanelSep.setPreferredSize(new java.awt.Dimension(10, 5));
        jPanel2.add(jPanelSep);

        jPanelUsername.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabelUsername.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabelUsername.setText("Nom d'utilisateur");
        jLabelUsername.setPreferredSize(new java.awt.Dimension(160, 16));
        jPanelUsername.add(jLabelUsername);

        jTextFieldUsername.setText("jTextFieldLogin");
        jTextFieldUsername.setPreferredSize(new java.awt.Dimension(200, 20));
        jPanelUsername.add(jTextFieldUsername);

        jPanel2.add(jPanelUsername);

        jPanelPassword.setPreferredSize(new java.awt.Dimension(100, 39));
        jPanelPassword.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabelPassword.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabelPassword.setText("Mot de passe");
        jLabelPassword.setPreferredSize(new java.awt.Dimension(160, 16));
        jPanelPassword.add(jLabelPassword);

        jPasswordField.setText("jPasswordField1");
        jPasswordField.setMinimumSize(new java.awt.Dimension(200, 20));
        jPasswordField.setPreferredSize(new java.awt.Dimension(200, 20));
        jPanelPassword.add(jPasswordField);

        jPanel2.add(jPanelPassword);

        jPanelRememberPassword.setPreferredSize(new java.awt.Dimension(100, 39));
        jPanelRememberPassword.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabelPassword1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabelPassword1.setPreferredSize(new java.awt.Dimension(160, 14));
        jPanelRememberPassword.add(jLabelPassword1);

        jCheckBoxRememberInfo.setText("Mémoriser ces informations");
        jCheckBoxRememberInfo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanelRememberPassword.add(jCheckBoxRememberInfo);

        jPanel2.add(jPanelRememberPassword);

        jPanelCenter.add(jPanel2);

        jPanelSepBlank.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanelSepBlank.setPreferredSize(new java.awt.Dimension(0, 10));
        jPanelSepBlank.setLayout(new javax.swing.BoxLayout(jPanelSepBlank, javax.swing.BoxLayout.LINE_AXIS));
        jPanelCenter.add(jPanelSepBlank);

        jPanelSepLine.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanelSepLine.setLayout(new javax.swing.BoxLayout(jPanelSepLine, javax.swing.BoxLayout.LINE_AXIS));

        jPanel8.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelSepLine.add(jPanel8);
        jPanelSepLine.add(jSeparator2);

        jPanel11.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelSepLine.add(jPanel11);

        jPanelCenter.add(jPanelSepLine);

        getContentPane().add(jPanelCenter);

        jPanelButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING, 5, 10));

        jButtonOk.setText("Ok");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonOk);

        jButtonCancel.setText("Annuler");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonCancel);

        jPanel12.setMaximumSize(new java.awt.Dimension(0, 10));
        jPanel12.setMinimumSize(new java.awt.Dimension(0, 10));
        jPanel12.setPreferredSize(new java.awt.Dimension(0, 10));
        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 5));
        jPanelButtons.add(jPanel12);

        getContentPane().add(jPanelButtons);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        doIt();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
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
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JCheckBox jCheckBoxRememberInfo;
    private javax.swing.JLabel jLabelMessage;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelPassword1;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelUsername;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelCenter;
    private javax.swing.JPanel jPanelNorth;
    private javax.swing.JPanel jPanelPassword;
    private javax.swing.JPanel jPanelRememberPassword;
    private javax.swing.JPanel jPanelSep;
    private javax.swing.JPanel jPanelSepBlank;
    private javax.swing.JPanel jPanelSepLine;
    private javax.swing.JPanel jPanelSepLine1;
    private javax.swing.JPanel jPanelTitle;
    private javax.swing.JPanel jPanelUsername;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField jTextFieldUsername;
    // End of variables declaration//GEN-END:variables

}

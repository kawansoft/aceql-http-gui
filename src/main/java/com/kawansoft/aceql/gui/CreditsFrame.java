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
import com.kawansoft.app.parms.util.ImageParmsUtil;
import com.kawansoft.app.util.WindowSettingMgr;
import com.swing.util.SwingUtil;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author Nicolas de Pomereu
 */
public class CreditsFrame extends javax.swing.JFrame {

    public static final String CR_LF = System.getProperty("line.separator");

    /**
     * The parent Window
     */
    private Window parent = null;
    private String appName = null;

    /**
     * Creates new form PostUploaderMain
     *
     * @param parent
     * @param appName
     */
    public CreditsFrame(Window parent, String appName) {
        this.parent = parent;
        this.appName = appName;
        initComponents();
        initializeIt();

        this.setVisible(true);

    }

    /**
     * This is the method to include in *our* constructor
     */
    public void initializeIt() {

        Dimension dimBase = new Dimension(415, 415);
        this.setPreferredSize(dimBase);
        //this.setSize(dim);

        try {
            this.setIconImage(ImageParmsUtil.getAppIcon());
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }

        jPanelCredits.setBorder(javax.swing.BorderFactory.createTitledBorder(MessagesManager.get("credits")));
                
        jEditorPaneCredits.setContentType("text/html");
        jEditorPaneCredits.setEditable(false);

        String creditText
                = "<P ALIGN=RIGHT><font face=\"Arial\" size=4>"
                + "{0} <br>" + MessagesManager.get("includes_software_developed_by")
                + "<br>"
                + "<br><a href=http://www.apache.org>The Apache Software Foundation</a>"
                + "<br><a href=http://www.bouncycastle.org>The Legion of the Bouncy Castle</a>"
                + "<br><a href=http://iharder.sourceforge.net/current/java/filedrop>iHarder.net</a>"
                + "<br>";

        creditText = creditText.replace("{0}", appName);
        
        creditText = SwingUtil.formatHtmlContentForSyntheticaAndNimbus(creditText);

        jEditorPaneCredits.setText(creditText);
        //jPanelCredits.add(jEditorPaneCredits);

        jEditorPaneCredits.setOpaque(false);

        // Hyperlink listener that will open a new Browser with the given URL
        jEditorPaneCredits.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent r) {
                if (r.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    java.awt.Desktop dekstop = java.awt.Desktop.getDesktop();
                    try {
                        dekstop.browse(r.getURL().toURI());
                    } catch (Exception ex) {
                        Logger.getLogger(CreditsFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        //ButtonResizer buttonResizer = new ButtonResizer(jPanelButtons);
        //buttonResizer.setWidthToMax();

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

        TitledBorder titledBorder = (TitledBorder) jPanelCredits.getBorder();
        this.setTitle(titledBorder.getTitle());

        // Load and activate previous windows settings
        this.setLocationRelativeTo(parent);
        
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
                closeOnExit();
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
        jPanelMain = new javax.swing.JPanel();
        jPanelBorderLeft = new javax.swing.JPanel();
        jPanelCredits = new javax.swing.JPanel();
        jEditorPaneCredits = new javax.swing.JEditorPane();
        jPanelBorderRight = new javax.swing.JPanel();
        jPanelButtons = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanelLogos.setMaximumSize(new java.awt.Dimension(32767, 54));
        jPanelLogos.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING, 0, 10));

        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/kawansoft/app/parms/images/logos/logo-AceQL_48.png"))); // NOI18N
        jPanelLogos.add(jLabelLogo);

        jPanelBorderLeft1.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelLogos.add(jPanelBorderLeft1);

        getContentPane().add(jPanelLogos);

        jPanelMain.setLayout(new javax.swing.BoxLayout(jPanelMain, javax.swing.BoxLayout.X_AXIS));

        jPanelBorderLeft.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelMain.add(jPanelBorderLeft);

        jPanelCredits.setBorder(javax.swing.BorderFactory.createTitledBorder("Autres Logiciels"));
        jPanelCredits.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        jEditorPaneCredits.setEditable(false);
        jEditorPaneCredits.setMinimumSize(new java.awt.Dimension(106, 80));
        jPanelCredits.add(jEditorPaneCredits);

        jPanelMain.add(jPanelCredits);

        jPanelBorderRight.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelMain.add(jPanelBorderRight);

        getContentPane().add(jPanelMain);

        jPanelButtons.setMaximumSize(new java.awt.Dimension(32767, 65));
        jPanelButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 10));

        jButtonClose.setText("OK");
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

private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
    closeOnExit();
}//GEN-LAST:event_jButtonCloseActionPerformed

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

                new CreditsFrame(null, "KawanDoc");
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    private javax.swing.JEditorPane jEditorPaneCredits;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelBorderLeft;
    private javax.swing.JPanel jPanelBorderLeft1;
    private javax.swing.JPanel jPanelBorderRight;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelCredits;
    private javax.swing.JPanel jPanelLogos;
    private javax.swing.JPanel jPanelMain;
    // End of variables declaration//GEN-END:variables

}

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
package com.kawansoft.app.util;

import com.kawansoft.app.parms.LanguageManager;
import com.kawansoft.app.parms.MessagesManager;
import com.kawansoft.app.parms.Parms;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.kawansoft.app.parms.util.ImageParmsUtil;
import com.swing.util.SwingUtil;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import org.apache.commons.io.IOUtils;


/**
 *
 * @author Nicolas de Pomereu
 */
public class Help extends javax.swing.JFrame {

    public static final String CR_LF = System.getProperty("line.separator");

        
    /**
     * Add a clipboard manager for content management
     */
    private ClipboardManager clipboard = null;
    private java.awt.Window parent = null;

    /**
     * Creates new form Help using content from a file
     *
     * @param parent the parent Window
     * @param helpFileHtmlRawName the help file ram name (withtout luanguahe and .html) located in com.kawansoft.app.parms.helpfiles
     */
    public Help(Window parent, String helpFileHtmlRawName) {

        initComponents();

        this.parent = parent;
        String content = getHtmlHelpContent(helpFileHtmlRawName);
        initializeIt(content);

        this.setVisible(true);
    }
    
     /**
     * Creates new form Help using a content from a string
     *
     * @param parent the parent Window
     * @param content the content to display
     * @param isContent to be set to true (parameter because we can not overload other constructor)
     */
    public Help(Window parent, String content, boolean isContent) {

        initComponents();

        this.parent = parent;
        initializeIt(content);

        this.setVisible(true);
    }

    /**
     * This is the method to include in *our* constructor
     * @param content the cntent to display
     */
    public void initializeIt(String content) {
        this.setPreferredSize(new Dimension(372, 440));

        try
        {
            this.setIconImage(ImageParmsUtil.getAppIcon());
        }
        catch (RuntimeException e1)
        {
            e1.printStackTrace();
        } 

        // Add a Clipboard Manager
        clipboard = new ClipboardManager((JPanel) this.getContentPane());

        // Content is not editable
        jEditorPane1.setContentType("text/html");
        jEditorPane1.setEditable(false);
        jEditorPane1.setText(content);

        // Hyperlink listener that will open a new Broser with the given URL 
        jEditorPane1.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent r) {
                if (r.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(r.getURL().toURI());
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(parent, e.toString());
                    }
                }

            }
        });

        this.setTitle(MessagesManager.get("help"));

        ButtonResizer buttonResizer = new ButtonResizer(jPanelButtons);
        buttonResizer.setWidthToMax();

        SwingUtil.resizeJComponentsForNimbusAndMacOsX(rootPane);
                
        this.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent e) {
                saveSettings();
            }

            public void componentResized(ComponentEvent e) {
                saveSettings();
            }
        });

        // Our window listener for all events
        // If window is closed ==> call close()
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        this.keyListenerAdder();

        int theWidth = this.getPreferredSize().width;
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Point upRight = new Point(dim.width - theWidth  - 1, 0);
                
        WindowSettingMgr.load(this, upRight);

        // These 2 stupid lines : only to Force to diplay top of file first
        jEditorPane1.moveCaretPosition(0);
        jEditorPane1.setSelectionEnd(0);

        pack();

    }
    
    /**
     * Returns the content of a help text file as text
     * @param helpFileRaw   raw name of help file the string _fr.txt or _en.txt will be added to get the full name
     * @return  
     */
    public static String getHelpContentAsText(String helpFileRaw) throws IOException {

        String helpFileTxt = helpFileRaw + "_" + LanguageManager.getLanguage() + ".txt";
                    
        try (InputStream  is = Parms.class.getResourceAsStream("helpfiles/" + helpFileTxt);
        ByteArrayOutputStream out = new ByteArrayOutputStream();){
            
            try {
                IOUtils.copy(is, out);
                String text = out.toString("ISO-8859-1");
                
                if (isHtmlEncoded(text)) {
                    text = HtmlConverter.fromHtml(text);
                }

                return text;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return ioe.getMessage();
            }
        } finally {
            //IOUtils.closeQuietly(is);
            //IOUtils.closeQuietly(out);
        }
    }

        /**
     * Minimalist method to test if a string is HTML encoded
     * @param text
     * @return 
     */
    private static boolean isHtmlEncoded(String text) {
        if (text.contains("&") && text.contains(";")) {
            return true;
        } else {
            return false;
        }
    }
    
    private static String getHtmlNameWithLanguage(String helpFileHtmlRawName) {

        return helpFileHtmlRawName + "_" + LanguageManager.getLanguage() + ".html";
    }
        
    /**
     * Return the HTML content of a HTML resource file in the message file
     * package
     *
     * @param helpFileRaw The help file to retrieve raw name without lnguage and .html
     * @return the HTML content of a HTML resource file in the message file
     * package
     */
    public static String getHtmlHelpContent(String helpFileRaw) {
        String htmlContent;
        
        try (InputStream is = Parms.class.getResourceAsStream("helpfiles/" + getHtmlNameWithLanguage(helpFileRaw))){
            //debug(urlResource);
            if (is == null) {
                return "<font face=\"Arial\" size=4><br>"
                        + "<b>Please apologize. <br>  "
                        + "Help is not yet available for this topic. </b> <br>"
                        + "<br>"
                        + "(" + getHtmlNameWithLanguage(helpFileRaw) + ")";
            }

            BufferedInputStream bisIn = new BufferedInputStream(is);
            LineInputStream lisIn = new LineInputStream(bisIn);

            String sLine = new String();

            htmlContent = "";

            while ((sLine = lisIn.readLine()) != null) {
                sLine = sLine.trim();
                //debug(sLine);                
                htmlContent += sLine; //+ CR_LF;
            }

            lisIn.close();
        } catch (IOException e) {
            e.printStackTrace();
            htmlContent = e.getMessage();
        }
        finally {
            //IOUtils.closeQuietly(is);
        }

        return htmlContent;
    }

    private void close() {
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

            if (keyCode == KeyEvent.VK_ESCAPE) {
                actionCancel();
            }
        }
    }

    private void actionCancel() {
        this.dispose();
    }

    public void saveSettings() {
        WindowSettingMgr.save(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelBorderTop = new javax.swing.JPanel();
        jPanelHelp = new javax.swing.JPanel();
        jPanelHelpLeft = new javax.swing.JPanel();
        jPanelHelpMain = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanelHelpRight = new javax.swing.JPanel();
        jPanelBottom = new javax.swing.JPanel();
        jPanelButtons = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Aide");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanelBorderTop.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanelBorderTop.setPreferredSize(new java.awt.Dimension(20, 10));
        jPanelBorderTop.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));
        getContentPane().add(jPanelBorderTop);

        jPanelHelp.setLayout(new javax.swing.BoxLayout(jPanelHelp, javax.swing.BoxLayout.LINE_AXIS));

        jPanelHelpLeft.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelHelp.add(jPanelHelpLeft);

        jPanelHelpMain.setPreferredSize(new java.awt.Dimension(319, 180));
        jPanelHelpMain.setLayout(new javax.swing.BoxLayout(jPanelHelpMain, javax.swing.BoxLayout.LINE_AXIS));

        jEditorPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jEditorPane1.setMargin(new java.awt.Insets(5, 5, 5, 5));
        jScrollPane1.setViewportView(jEditorPane1);

        jPanelHelpMain.add(jScrollPane1);

        jPanelHelp.add(jPanelHelpMain);

        jPanelHelpRight.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanelHelp.add(jPanelHelpRight);

        getContentPane().add(jPanelHelp);

        jPanelBottom.setMaximumSize(new java.awt.Dimension(32767, 5));
        jPanelBottom.setPreferredSize(new java.awt.Dimension(100, 5));

        javax.swing.GroupLayout jPanelBottomLayout = new javax.swing.GroupLayout(jPanelBottom);
        jPanelBottom.setLayout(jPanelBottomLayout);
        jPanelBottomLayout.setHorizontalGroup(
            jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );
        jPanelBottomLayout.setVerticalGroup(
            jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelBottom);

        jPanelButtons.setMaximumSize(new java.awt.Dimension(32767, 33));
        jPanelButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 10));

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

private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
    actionCancel();
}//GEN-LAST:event_jButtonCloseActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Help(null, "help_main");
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelBorderTop;
    private javax.swing.JPanel jPanelBottom;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelHelp;
    private javax.swing.JPanel jPanelHelpLeft;
    private javax.swing.JPanel jPanelHelpMain;
    private javax.swing.JPanel jPanelHelpRight;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables


}

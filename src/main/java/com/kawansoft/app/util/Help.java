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
package com.kawansoft.app.util;

import com.kawansoft.aceql.gui.util.AceQLManagerUtil;
import com.kawansoft.app.parms.LanguageManager;
import com.kawansoft.app.parms.MessagesManager;
import com.kawansoft.app.parms.ParmsConstants;
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
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
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
    public ClipboardManager clipboard = null;
    private Window parent = null;

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
        
        System.out.println(isContent);
        
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
                    
        try (InputStream  is = ParmsConstants.class.getResourceAsStream("helpfiles/" + helpFileTxt);
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
        } 
    }

        /**
     * Minimalist method to test if a string is HTML encoded
     * @param text
     * @return 
     */
    private static boolean isHtmlEncoded(String text) {
        return text.contains("&") && text.contains(";");
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
        
        try (InputStream is = ParmsConstants.class.getResourceAsStream("helpfiles/" + getHtmlNameWithLanguage(helpFileRaw))){
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

            String sLine;

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

        jPanelBorderTop = new JPanel();
        jPanelHelp = new JPanel();
        jPanelHelpLeft = new JPanel();
        jPanelHelpMain = new JPanel();
        jScrollPane1 = new JScrollPane();
        jEditorPane1 = new JEditorPane();
        jPanelHelpRight = new JPanel();
        jPanelBottom = new JPanel();
        jPanelButtons = new JPanel();
        jButtonClose = new JButton();
        jPanel4 = new JPanel();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Aide");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        jPanelBorderTop.setMaximumSize(new Dimension(32767, 10));
        jPanelBorderTop.setPreferredSize(new Dimension(20, 10));
        jPanelBorderTop.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        getContentPane().add(jPanelBorderTop);

        jPanelHelp.setLayout(new BoxLayout(jPanelHelp, BoxLayout.LINE_AXIS));

        jPanelHelpLeft.setMaximumSize(new Dimension(10, 10));
        jPanelHelp.add(jPanelHelpLeft);

        jPanelHelpMain.setPreferredSize(new Dimension(319, 180));
        jPanelHelpMain.setLayout(new BoxLayout(jPanelHelpMain, BoxLayout.LINE_AXIS));

        jEditorPane1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jEditorPane1.setMargin(new Insets(5, 5, 5, 5));
        jScrollPane1.setViewportView(jEditorPane1);

        jPanelHelpMain.add(jScrollPane1);

        jPanelHelp.add(jPanelHelpMain);

        jPanelHelpRight.setMaximumSize(new Dimension(10, 10));
        jPanelHelp.add(jPanelHelpRight);

        getContentPane().add(jPanelHelp);

        jPanelBottom.setMaximumSize(new Dimension(32767, 5));
        jPanelBottom.setPreferredSize(new Dimension(100, 5));

        GroupLayout jPanelBottomLayout = new GroupLayout(jPanelBottom);
        jPanelBottom.setLayout(jPanelBottomLayout);
        jPanelBottomLayout.setHorizontalGroup(jPanelBottomLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );
        jPanelBottomLayout.setVerticalGroup(jPanelBottomLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelBottom);

        jPanelButtons.setMaximumSize(new Dimension(32767, 33));
        jPanelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 10));

        jButtonClose.setText("Fermer");
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

    private void jButtonCloseActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        AceQLManagerUtil.debugEvent(evt);
        this.dispose();
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
    public JButton jButtonClose;
    public JEditorPane jEditorPane1;
    public JPanel jPanel4;
    public JPanel jPanelBorderTop;
    public JPanel jPanelBottom;
    public JPanel jPanelButtons;
    public JPanel jPanelHelp;
    public JPanel jPanelHelpLeft;
    public JPanel jPanelHelpMain;
    public JPanel jPanelHelpRight;
    public JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables


}

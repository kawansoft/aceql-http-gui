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

import com.kawansoft.app.parms.util.ImageParmsUtil;
import com.kawansoft.app.util.ClipboardManager;
import com.kawansoft.app.util.WindowSettingMgr;
import com.kawansoft.app.util.console.MessageConsole;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;


/**
 *
 * @author Nicolas de Pomereu
 */
public class AceQLConsole extends JFrame {

    public static final String CR_LF = System.getProperty("line.separator");
    /**
     * Add a clipboard manager for content management
     */
    private Color light_red = new Color(255, 58,58);

    
    public AceQLConsole() {
        super();
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
    public void initializeIt() {

        Dimension dim = new Dimension(1259, 616);
        this.setPreferredSize(dim);
        this.setSize(dim);
        
        try {
            this.setIconImage(ImageParmsUtil.getAppIcon());
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }

       // Toolkit.getDefaultToolkit().setDynamicLayout(true);

        // Add a Clipboard Manager
        ClipboardManager clipboard  = new ClipboardManager(this.getContentPane());

        jTextPane1.setEditable(false);
        
        // Redirect Out and Error to console
        MessageConsole mc = new MessageConsole(jTextPane1);
        mc.redirectOut();
        mc.redirectErr(light_red, null);
        mc.setMessageLines(10_000);
        
        //System.out.println(jEditorPane.getMargin());
        jTextPane1.setMargin(new Insets(3, 10, 3, 10));

        //Our key listeners

        jTextPane1.addKeyListener(new KeyAdapter() {
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

        this.setTitle("AceQL Console");

        // These 2 stupid lines : only to Force to diplay top of file first
        jTextPane1.moveCaretPosition(0);
        jTextPane1.setSelectionEnd(0);

        this.pack();
        this.setVisible(true);
       
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

        jPanelBorderTop = new JPanel();
        jPanelHelp = new JPanel();
        jPanelHelpLeft = new JPanel();
        jPanelHelpMain = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTextPane1 = new JTextPane();
        jPanelHelpRight = new JPanel();
        jPanelBottom = new JPanel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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

        jTextPane1.setBackground(new Color(0, 0, 0));
        jTextPane1.setFont(new Font("Lucida Console", 0, 18)); // NOI18N
        jTextPane1.setForeground(new Color(192, 192, 192));
        jScrollPane1.setViewportView(jTextPane1);

        jPanelHelpMain.add(jScrollPane1);

        jPanelHelp.add(jPanelHelpMain);

        jPanelHelpRight.setMaximumSize(new Dimension(10, 10));
        jPanelHelp.add(jPanelHelpRight);

        getContentPane().add(jPanelHelp);

        jPanelBottom.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanelBottomLayout = new GroupLayout(jPanelBottom);
        jPanelBottom.setLayout(jPanelBottomLayout);
        jPanelBottomLayout.setHorizontalGroup(jPanelBottomLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanelBottomLayout.setVerticalGroup(jPanelBottomLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
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
                new AceQLConsole();
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public JPanel jPanelBorderTop;
    public JPanel jPanelBottom;
    public JPanel jPanelHelp;
    public JPanel jPanelHelpLeft;
    public JPanel jPanelHelpMain;
    public JPanel jPanelHelpRight;
    public JScrollPane jScrollPane1;
    public JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}

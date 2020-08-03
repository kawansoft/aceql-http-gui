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
/**
 * JFrame for clean display of System Info (System.getProperties)
 */ 
package com.kawansoft.app.util;

import com.kawansoft.aceql.gui.util.AceQLManagerUtil;
import com.kawansoft.app.util.table.TableClipboardManager;
import com.kawansoft.app.parms.util.ImageParmsUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import com.swing.util.SwingUtil;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author Nicolas de Pomereu
 */
public class SystemPropDisplayer extends javax.swing.JFrame {

    public static final String CR_LF = System.getProperty("line.separator");
    /**
     * The parent JFrame
     */
    private Window parentJframe = null;
    private Window thisOne;
    //private MessagesManager messages = new MessagesManager();
    /**
     * Pop Up menu
     */
    public JPopupMenu popupMenu;
    private Font m_font = new Font("Tahoma", Font.PLAIN, 13);
    /**
     * Add a clipboard manager for right button mouse control over input text
     * fields
     */
    public TableClipboardManager clipboard = null;

    /**
     * Creates new form NewsFrame
     */
    public SystemPropDisplayer(Window parentJframe) {
        this(parentJframe, null);
    }
    
    /**
     * Creates new form NewsFrame
     */
    public SystemPropDisplayer(Window parentJframe, String jButtonUrlText) {
        this.parentJframe = parentJframe;
        thisOne = this;
        initComponents();
        this.jButtonUrl.setText(jButtonUrlText);
        initializeIt();
    }
    
    /**
     * This is the method to include in the constructor
     */
    public void initializeIt() {

      try {
            this.setIconImage(ImageParmsUtil.getAppIcon());
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        }

        this.setSize(500, 500);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);

        if (parentJframe != null) {
            this.setLocationRelativeTo(parentJframe);
        }

        this.jButtonClose.setText("OK");

        this.keyListenerAdder();
        this.setLocationByPlatform(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                WindowSettingMgr.save(thisOne);
            }
        });

        //this.setLocationRelativeTo(parentJframe);
        WindowSettingMgr.load(this);

        jScrollPane1.setAutoscrolls(true);

        //Ok; clean (re)recration of the JTable

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createTable();
            }
        });

        ButtonResizer buttonResizer = new ButtonResizer(jPanelButtons);
        buttonResizer.setWidthToMax();
        
        SwingUtil.resizeJComponentsForNimbusAndMacOsX(rootPane);
      
        jScrollPane1.setViewportView(jTable1);

        this.setTitle(jLabelMiniIcon.getText());
        
        pack();
                
        this.setVisible(true);
    }

    /**
     * Will (re)create the JTable with all the public keys
     *
     */
    private void createTable() {
        SystemPropsTableCreator systemPropsTableCreator = new SystemPropsTableCreator(m_font);
        jTable1 = systemPropsTableCreator.create();

        jTable1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                thisKeyReleased(e);
            }
        });

        jTable1.requestFocusInWindow();

        // Sey colors to be clean with all environments
        // jTable1.setSelectionBackground(PgeepColor.LIGHT_BLUE);
        // jTable1.setSelectionForeground(Color.BLACK);

        jScrollPane1.setViewportView(jTable1);

        Color tableBackground = null;
        tableBackground = jTable1.getBackground();
        jTable1.getParent().setBackground(tableBackground);

        jTable1.setIntercellSpacing(new Dimension(5, 1));

        // Add a Clipboard Manager
        clipboard = new TableClipboardManager(this, jTable1, false);
    }

    /**
     * Universal key listener
     */
    private void keyListenerAdder() {
        List<Component> components = SwingUtil.getAllComponants(this);

        for (int i = 0; i < components.size(); i++) {
            Component comp = components.get(i);

            comp.addKeyListener(new KeyAdapter() {
                @Override
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
                this.dispose();
            }
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTop = new JPanel();
        jPanelIcon = new JPanel();
        jLabelMiniIcon = new JLabel();
        jPanel3 = new JPanel();
        jButtonUrl = new JButton();
        jPanelEnd = new JPanel();
        jPanelCenter = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jPanelSep = new JPanel();
        jPanelWest = new JPanel();
        jPanelSouth = new JPanel();
        jPanelButtons = new JPanel();
        copyToClipboard = new JButton();
        jButtonClose = new JButton();
        jPanelEast = new JPanel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jPanelTop.setMaximumSize(new Dimension(32767, 80));
        jPanelTop.setLayout(new BoxLayout(jPanelTop, BoxLayout.LINE_AXIS));

        jPanelIcon.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        jLabelMiniIcon.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jLabelMiniIcon.setIcon(new ImageIcon(getClass().getResource("/com/kawansoft/app/parms/images/about.png"))); // NOI18N
        jLabelMiniIcon.setText("System Info");
        jLabelMiniIcon.setToolTipText("");
        jPanelIcon.add(jLabelMiniIcon);

        jPanelTop.add(jPanelIcon);

        jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.LINE_AXIS));

        jButtonUrl.setFont(new Font("Tahoma", 1, 11)); // NOI18N
        jButtonUrl.setForeground(new Color(51, 0, 255));
        jButtonUrl.setText("https://www.kawandoc.com/");
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
        jPanel3.add(jButtonUrl);

        jPanelEnd.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanelEndLayout = new GroupLayout(jPanelEnd);
        jPanelEnd.setLayout(jPanelEndLayout);
        jPanelEndLayout.setHorizontalGroup(jPanelEndLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanelEndLayout.setVerticalGroup(jPanelEndLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel3.add(jPanelEnd);

        jPanelTop.add(jPanel3);

        getContentPane().add(jPanelTop, BorderLayout.PAGE_START);

        jPanelCenter.setLayout(new BoxLayout(jPanelCenter, BoxLayout.Y_AXIS));

        jTable1.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanelCenter.add(jScrollPane1);

        jPanelSep.setMaximumSize(new Dimension(32767, 4));
        jPanelSep.setMinimumSize(new Dimension(0, 4));
        jPanelSep.setPreferredSize(new Dimension(608, 4));

        GroupLayout jPanelSepLayout = new GroupLayout(jPanelSep);
        jPanelSep.setLayout(jPanelSepLayout);
        jPanelSepLayout.setHorizontalGroup(jPanelSepLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 489, Short.MAX_VALUE)
        );
        jPanelSepLayout.setVerticalGroup(jPanelSepLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        jPanelCenter.add(jPanelSep);

        getContentPane().add(jPanelCenter, BorderLayout.CENTER);

        jPanelWest.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanelWestLayout = new GroupLayout(jPanelWest);
        jPanelWest.setLayout(jPanelWestLayout);
        jPanelWestLayout.setHorizontalGroup(jPanelWestLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanelWestLayout.setVerticalGroup(jPanelWestLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 339, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelWest, BorderLayout.WEST);

        jPanelSouth.setLayout(new GridLayout(1, 2));

        jPanelButtons.setLayout(new FlowLayout(FlowLayout.TRAILING, 10, 10));

        copyToClipboard.setText("Copy to Clipboard");
        copyToClipboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                copyToClipboardActionPerformed(evt);
            }
        });
        jPanelButtons.add(copyToClipboard);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonClose);

        jPanelSouth.add(jPanelButtons);

        getContentPane().add(jPanelSouth, BorderLayout.PAGE_END);

        jPanelEast.setMaximumSize(new Dimension(10, 10));

        GroupLayout jPanelEastLayout = new GroupLayout(jPanelEast);
        jPanelEast.setLayout(jPanelEastLayout);
        jPanelEastLayout.setHorizontalGroup(jPanelEastLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanelEastLayout.setVerticalGroup(jPanelEastLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 339, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelEast, BorderLayout.LINE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButtonCloseActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
    AceQLManagerUtil.debugEvent(evt);
    dispose();
}//GEN-LAST:event_jButtonCloseActionPerformed

    private void copyToClipboardActionPerformed(ActionEvent evt) {//GEN-FIRST:event_copyToClipboardActionPerformed
        AceQLManagerUtil.debugEvent(evt);
        jTable1.selectAll();

        // Put the content of the table in a JTextField
        int[] selRows = jTable1.getSelectedRows();

        if (selRows.length > 0) {
            String value = "";

            for (int i = 0; i < selRows.length; i++) {
                // get Table data
                TableModel tm = jTable1.getModel();

                // Force the cast of the getValueAt into a String
                Object oValue0 = tm.getValueAt(selRows[i], 0);
                Object oValue1 = tm.getValueAt(selRows[i], 1);

                value += (String) oValue0.toString();
                value += "\t";
                value += (String) oValue1.toString();
                value += CR_LF;
            }

            JEditorPane jEditorPane = new JEditorPane();
            jEditorPane.setText(value);
            jEditorPane.selectAll();
            jEditorPane.copy();

            /*
             try {
             Desktop dekstop = java.awt.Desktop.getDesktop();

             String mailTo = "contact@kawansoft.com" + "?subject=System Properties&body=" + value;

             this.dispose();

             URI uriMailTo;
             uriMailTo = new URI("mailto", mailTo, null);
             dekstop.mail(uriMailTo);

             } catch (Exception ex) {
             JOptionPane.showMessageDialog(parentJframe, "Impossible d\' envoyer l'email: " + ex.getMessage());
             }
             */
        }

    }//GEN-LAST:event_copyToClipboardActionPerformed

    private void jButtonUrlMouseEntered(MouseEvent evt) {//GEN-FIRST:event_jButtonUrlMouseEntered
        AceQLManagerUtil.debugEvent(evt);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jButtonUrlMouseEntered

    private void jButtonUrlMouseExited(MouseEvent evt) {//GEN-FIRST:event_jButtonUrlMouseExited
        AceQLManagerUtil.debugEvent(evt);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_jButtonUrlMouseExited

    private void jButtonUrlActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonUrlActionPerformed
        AceQLManagerUtil.debugEvent(evt);
        Desktop desktop = Desktop.getDesktop();
        String url = jButtonUrl.getText();

        if (url == null || url.isEmpty()) {
            return;
        }
        
        try {
            desktop.browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Impossible d\'accéder au site Internet " + url + "\n(" + e.toString() + ")");
        }
    }//GEN-LAST:event_jButtonUrlActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SystemPropDisplayer(null).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public JButton copyToClipboard;
    public JButton jButtonClose;
    public JButton jButtonUrl;
    public JLabel jLabelMiniIcon;
    public JPanel jPanel3;
    public JPanel jPanelButtons;
    public JPanel jPanelCenter;
    public JPanel jPanelEast;
    public JPanel jPanelEnd;
    public JPanel jPanelIcon;
    public JPanel jPanelSep;
    public JPanel jPanelSouth;
    public JPanel jPanelTop;
    public JPanel jPanelWest;
    public JScrollPane jScrollPane1;
    public JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

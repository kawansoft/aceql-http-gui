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
package com.swing.util;

import com.kawansoft.app.util.UiUtilsConstants;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.apache.commons.lang3.SystemUtils;

import java.awt.Color;
import java.awt.Window;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;



/**
 * @author Nicolas de Pomereu
 */

public class SwingUtil
{
    public static final String CR_LF = System.getProperty("line.separator") ;

    /**
     * Format the HTML content for Synthetica ==> increase font size per +1
     * @param content   the html content to format 
     * @return the formated html content
     */
    public static String formatHtmlContentForSyntheticaAndNimbus(final String content) {
        String contentNew = content;
        if (UiUtilsConstants.isSynthetica() || UiUtilsConstants.isNimbus() ) {
            contentNew = contentNew.replaceAll("size=4", "size=5");
            contentNew = contentNew.replaceAll("size=6", "size=7");
        }
        return contentNew;
    }
    
    /**
     * Format the JPanel for Synthetica ==> remove border
     * @param jpanel the panel to format
     * 
     * @return the JPanel without border
     */
    public static void formatJpanelBorderForSynthetica(JPanel jpanel) {
        if (UiUtilsConstants.isSynthetica()) {
         jpanel.setBorder(null);
        }
    }
    
    /**
     * Format the JPanel containing a JXTextField for Synthetica
     * ==> add a line border thickness 1 and rounded
     * @param jpanel the panel containing the JXTextField to  format
     * 
     * @return the JPanel with a line border light gray - thickness 1 and rounded
     */
    public static void formatJXTextFieldForSynthetica(JPanel jPanel) {
        if (UiUtilsConstants.isSynthetica()) {
         jPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        }
    }    
    
     /**
     * Allows to set the background color for the window
     * @param color the color 
     */
    public static void setBackgroundColor(Window window, Color color) {
        setBackgroundColor(window, color, true);
    } 
    
     /**
     * Allows to set the background color for the window
     * @param color the color 
     */
    public static void setBackgroundColor(Window window, Color color, boolean includeJTextField) {
        List<Component> components = SwingUtil.getAllComponants(window);

        for (int i = 0; i < components.size(); i++) {
            Component comp = components.get(i);

            if (comp instanceof JPanel || comp instanceof JCheckBox || comp instanceof JRadioButton) {
                comp.setBackground(color);
            }
            
            if (includeJTextField && comp instanceof JTextField) {
                 JTextField jTextField = (JTextField)comp;
                 if (! jTextField.isEditable()) {
                     jTextField.setBackground(color);
                     jTextField.setBorder(null);
                     jTextField = addColonBeforeDisplay(jTextField);
                 }         
            }                       
        }        
    }     
    
    
    public static JTextField addColonBeforeDisplay(JTextField jTextField) {
        String text = jTextField.getText();

        if (!text.startsWith(": ")) {
            text = ": " + text;
            jTextField.setText(text);
        }

        return jTextField;
    }
    
    public static JLabel addColonBeforeDisplay(JLabel jLabel) {
        String text = jLabel.getText();

        if (!text.startsWith(": ")) {
            text = ": " + text;
            jLabel.setText(text);
        }

        return jLabel;
    }    
    
    
        
    /**
     * Resize jComponents for Nimbus look and feel
     * @param container
     */
    public static void resizeJComponentsForNimbusAndMacOsX(Container container){
        
        List<Component> components = SwingUtil.getAllComponants(container);
                
        // Reset minimm to (10, 10) if max(10,10)
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel)component;
                Dimension max = panel.getMaximumSize();
                
                if (max.width == 10 && max.height == 10) {
                    panel.setMinimumSize(max);
                }
            }
        }
        
        if ((SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_LINUX) && ! UiUtilsConstants.isNimbus()) {
            return;
        }
        
         // To be done for all platforms for Nimbus + Mac OS X

        //List<Component> components = SwingUtil.getAllComponants(container);
        
        int growthWidth = 5;
        
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel)component;
                Dimension max = panel.getMaximumSize();
                Dimension pre = panel.getPreferredSize();
                
                if (max.width == 10 && pre.width == 10) {
                    max.width += growthWidth;
                    pre.width += growthWidth;
                    
                    panel.setPreferredSize(pre);
                    panel.setMaximumSize(pre);
                    panel.setMinimumSize(pre);
                }
            }
        }
        /*
        for (Component component : components) {
            int maxWidth = (int)component.getMaximumSize().getWidth();
            int minWidth = (int)component.getMinimumSize().getWidth();
            int prefWidth = (int)component.getPreferredSize().getWidth();

            int newHeight = 28;
            
            if(component instanceof JTextField || component instanceof JPasswordField){
                component.setMaximumSize(new Dimension(maxWidth, newHeight));
                component.setMinimumSize(new Dimension(minWidth, newHeight));
                component.setPreferredSize(new Dimension(prefWidth, newHeight));
            }else if(component instanceof JLabel){
                
                int maxHeigth = (int)component.getMaximumSize().getHeight();
                int minHeigth = (int)component.getMinimumSize().getHeight();
                int prefHeigth = (int)component.getPreferredSize().getHeight();
                //int heigth = (int)component.getSize().getHeight();

                component.setMaximumSize(new Dimension(maxWidth, maxHeigth + 2));
                component.setMinimumSize(new Dimension(minWidth, minHeigth + 2));
                component.setPreferredSize(new Dimension(prefWidth, prefHeigth + 2));
                //component.setSize(new Dimension(prefWidth, heigth + 2));
            }
        }
        */
    }
    
    /**
     * Return all Components contained in a Container
     * 
     * @param container The Container        
     * @return          The complete list of inside Components                     
     */
    public static List<Component> getAllComponants(Container container)
    {
        List<Component> componentList  = new Vector();
        
        getAllComponents(container, componentList);
        
        return componentList;
    }

    /**
     * Disable or enablez a Tool bar
     * @param enable        true or false
     * @param jToolBar      the tool bar to enable disable
     */
    public static void enableToolbar( JToolBar jToolBar, boolean enable)
    {
        for(Component comp : jToolBar.getComponents())
        {
           comp.setEnabled(enable);
        }
    }
    
    /**
     *     
     * Get all the components inside a component and put it in a collection.
     * Recursiv method
     * 
     * @param c              The Component
     * @param collection     The collection to store in the result 
     */
    private static void getAllComponents(Component c, Collection collection) {
        collection.add(c);
        if (c instanceof Container) {
          Component[] kids = ((Container)c).getComponents();
          for(int i=0; i<kids.length; i++)
            getAllComponents(kids[i], collection);
        }
      }

    
}

/**
 * 
 */

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

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.swing.util.SwingUtil;

/**
 * Utility class to resize all buttons in a JPanel to the maximum width, that is
 * the width of the widest button
 * @author Nicolas de Pomereu
 *
 */
public class ButtonResizer
{
    /** The debug flag */ 
    protected boolean DEBUG =false;
    
    /** The panel containing the buttons **/
    private JPanel jPanelButtons;

    
    /**
     * Default contructor
     *
     */
    public ButtonResizer()
    {
        
    }

    /**
     * Constructor
     * @param jPanelButtons         The panel containing the buttons
     */
    public ButtonResizer(JPanel jPanelButtons)
    {
        this.jPanelButtons = jPanelButtons;
    }

    /**
     * Resize all buttons to the width of the biggest button
     *
     * @param the list of Components to resize
     */
    public void setWidthToMax(List components)
    {        
        int buttonWidth = 0;
        int maxButtonWidth =0;
        
        for (int i = 0; i < components.size(); i++)
        {
            Component component = (Component) components.get(i);
            
            if (component instanceof JButton)
            {                
                debug("Button ! " + i + " " + component.getName());
                
                JButton currentButton = (JButton)component;                
                Dimension dim = currentButton.getPreferredSize();
                
                buttonWidth = (int)dim.getWidth();
                debug("Button width: " + buttonWidth);
                
                if  (buttonWidth > maxButtonWidth)
                {
                    maxButtonWidth = buttonWidth;
                }
            }                      
        }
        
        debug("Button MAX WIDTH: " + maxButtonWidth);
        
        for (int i = 0; i < components.size(); i++)
        {
            Component component = (Component) components.get(i);
            
            debug("component: " + component.getName());
            
            if (component instanceof JButton)
            {                
                debug("Button ! " + i + " " + component.getName());
                
                JButton currentButton = (JButton)component;    
                Dimension dim = currentButton.getPreferredSize();
                int height = (int)dim.getHeight();
                currentButton.setPreferredSize(new Dimension(maxButtonWidth, height));
                debug("Button width set! " + i + " " + maxButtonWidth);
            }                      
        }
    }

    /**
     * Resize all buttons to the width of the biggest button
     *
     */
    public void setWidthToMax()
    {
        List<Component> components = SwingUtil.getAllComponants(jPanelButtons);
        
        setWidthToMax(components);
    }
    
    /**
     * debug tool
     */
    private void debug(String s)
    {
        if (DEBUG)
        {
            System.out.println(s);
            //System.out.println(this.getClass().getName() + " " + new Date() + " " + s);
        }
    }    
}


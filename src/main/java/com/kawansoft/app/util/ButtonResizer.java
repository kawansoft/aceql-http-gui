//ButtonResizer.java
//Copyright (c) SafeLogic, 2000 - 2005
//
//Last Updates: 
// 14 f�vr. 2006 19:15:44 Nicolas de Pomereu
// 05/04/06 20:00 NDP - use SwingUtil to get all components
// 29/11/06 20:00 NDP - New constructor & methods

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


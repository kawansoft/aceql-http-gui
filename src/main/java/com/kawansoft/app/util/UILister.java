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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

public class UILister
{
    public static void main(String[] args)
    {
        try
        {
            Set defaults = UIManager.getLookAndFeelDefaults().entrySet();
            TreeSet ts = new TreeSet(new Comparator()
            {
                public int compare(Object a, Object b)
                {
                    
                    Map.Entry ea = (Map.Entry) a;
                    Map.Entry eb = (Map.Entry) b;
                    return ((String) ea.getKey().toString()).compareTo(((String) eb
                            .getKey().toString()));
                    
                }
            });
            
            ts.addAll(defaults);
            Object[][] kvPairs = new Object[defaults.size()][2];
            Object[] columnNames = new Object[]
                                              { "Key", "Value" };
            int row = 0;
            for (Iterator i = ts.iterator(); i.hasNext();)
            {
                Object o = i.next();
                Map.Entry entry = (Map.Entry) o;
                kvPairs[row][0] = entry.getKey();
                kvPairs[row][1] = entry.getValue();
                row++;
            }

            JTable table = new JTable(kvPairs, columnNames);
            JScrollPane tableScroll = new JScrollPane(table);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    System.exit(0);
                }
            });
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
            buttons.add(closeButton, null);

            JPanel main = new JPanel(new BorderLayout());
            main.add(tableScroll, BorderLayout.CENTER);
            main.add(buttons, BorderLayout.SOUTH);

            JFrame frame = new JFrame("UI Properties");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(main);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

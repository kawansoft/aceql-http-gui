//UILister.java
//Copyright (c) SafeLogic, 2000 - 2005
//
//Last Updates: 
// 28 nov. 2008 10:58:18 Nicolas de Pomereu

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

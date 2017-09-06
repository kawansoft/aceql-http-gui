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

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;


/**
 * Utilities for JTable
 *
 * @author Nicolas de Pomereu
 */
public class JTableUtil {

    /**
     * The DEBUG flag
     */
    private boolean DEBUG = false;

    /**
     * Allow to resize columns in Jtable
     *
     * @param table table to resize
     * @param expansion expansion factor for special fonts
     *
     */
    public static void calcColumnWidths(JTable table, double expansion) {
        JTableUtil jTableUtil = new JTableUtil(); // Fod debug

        JTableHeader header = table.getTableHeader();

        TableCellRenderer defaultHeaderRenderer = null;

        if (header != null) {
            defaultHeaderRenderer = header.getDefaultRenderer();
        }

        TableColumnModel columns = table.getColumnModel();
        TableModel data = table.getModel();

        int margin = columns.getColumnMargin(); // only JDK1.3
        margin += 5;

        jTableUtil.debug("margin: " + margin);

        int rowCount = data.getRowCount();

        int totalWidth = 0;

        for (int i = columns.getColumnCount() - 1; i >= 0; --i) {
            TableColumn column = columns.getColumn(i);

            int columnIndex = column.getModelIndex();

            int width = -1;

            TableCellRenderer h = column.getHeaderRenderer();

            if (h == null) {
                h = defaultHeaderRenderer;
            }

            if (h != null) // Not explicitly impossible
            {
                Component c = h.getTableCellRendererComponent(table, column.getHeaderValue(),
                        false, false, -1, i);

                width = c.getPreferredSize().width;
            }

            for (int row = rowCount - 1; row >= 0; --row) {
                TableCellRenderer r = table.getCellRenderer(row, i);

                Component c = r.getTableCellRendererComponent(table,
                        data.getValueAt(row, columnIndex),
                        false, false, row, i);

                // 17 = x
                // 12 = 100
                //
                // ==> x = 17/12

                width = Math.max(width, c.getPreferredSize().width);
            }

            if (width >= 0) {
                width = (int) (width * expansion);

                column.setPreferredWidth(width + margin); // <1.3: without margin
            } else
                ; // ???

            totalWidth += column.getPreferredWidth();
        }

        // only <1.3:   totalWidth += columns.getColumnCount() * columns.getColumnMargin();

        /* If you like; This does not make sense for two many columns!
         Dimension size = table.getPreferredScrollableViewportSize();

         size.width = totalWidth;

         table.setPreferredScrollableViewportSize(size);
         */

        // table.sizeColumnsToFit(-1); <1.3; possibly even table.revalidate()

        // if (header != null)
        //     header.repaint(); only makes sense when the header is visible (only <1.3)
    }

    /**
     * Displays the specified message if the DEBUG flag is set.
     *
     * @param sMsg the debug message to display
     */
    protected void debug(String sMsg) {
        if (DEBUG) {
            System.out.println("DBG> " + sMsg);
        }
    }

    /**
     * Fit Jtable to colum size
     *
     * @param table
     * @param vColIndex
     * @param margin
     */
    // METHOD NOT USED IN PGEEP V1.0
    private void fitSizeToContent(JTable table, int vColIndex, int margin) {

        TableModel model = table.getModel();
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
        TableColumn col = colModel.getColumn(vColIndex);
        int width = 0;

//       Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        Component comp = renderer.getTableCellRendererComponent(
                table, col.getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;

//       Get maximum width of column data
        for (int r = 0; r < table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, vColIndex);
            comp = renderer.getTableCellRendererComponent(
                    table, table.getValueAt(r, vColIndex), false, false, r, vColIndex);
            width = Math.max(width, comp.getPreferredSize().width);
        }

//       Add margin
        width += 2 * margin;

//       Set the width
        col.setPreferredWidth(width);
    }
}

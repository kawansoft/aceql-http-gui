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
package com.kawansoft.app.util.table;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * Thus class overides the DefaultTableModel so that *no* Cell is editable.
 * This is done by overcharging isCellEditable() which returns always false
 * 
 * @author Nicolas de Pomereu
 */
public class TableModelNonEditable extends DefaultTableModel
{

    /**
     * Consrructor
     */
    public TableModelNonEditable()
    {
        super();
    }

    /**
     * @param rowCount
     * @param columnCount
     */
    public TableModelNonEditable(int rowCount, int columnCount)
    {
        super(rowCount, columnCount);

    }

    /**
     * @param columnNames
     * @param rowCount
     */
    public TableModelNonEditable(Vector columnNames, int rowCount)
    {
        super(columnNames, rowCount);

    }

    /**
     * @param columnNames
     * @param rowCount
     */
    public TableModelNonEditable(Object[] columnNames, int rowCount)
    {
        super(columnNames, rowCount);

    }

    /**
     * @param data
     * @param columnNames
     */
    public TableModelNonEditable(Vector data, Vector columnNames)
    {
        super(data, columnNames);

    }

    /**
     * @param data
     * @param columnNames
     */
    public TableModelNonEditable(Object[][] data, Object[] columnNames)
    {
        super(data, columnNames);
    }
    
    /**
     * Returns true regardless of parameter values.
     *
     * @param   row             the row whose value is to be queried
     * @param   column          the column whose value is to be queried
     * @return                  true
     * @see #setValueAt
     */
    public boolean isCellEditable(int row, int column) {
        return false;
    }
       
}

/**
 * 
 */

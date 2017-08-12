//TableModelNonEditable.java
//Copyright (c) SafeLogic, 2000 - 2005
//
//Last Updates: 
// 7 f�vr. 2006 10:41:57 Nicolas de Pomereu

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
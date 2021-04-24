package com.chesterccw.exceldiffer.ui.component;

import javax.swing.table.DefaultTableModel;

/**
 * @author chesterccw
 * @date 2020/7/17
 */
public class MyTableModel extends DefaultTableModel {

    public MyTableModel(){

    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {

    }

}

package com.chesterccw.exceldiffer.ui.component;

import com.chesterccw.exceldiffer.resolve.Resolver;
import com.chesterccw.exceldiffer.util.TableTool;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.ui.table.JBTable;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Vector;

/**
 * @author chesterccw
 * @date 2020/8/7
 */
public class MyTableListener implements ListSelectionListener, MouseListener {

    JBTable tableL,tableR,tableStatus;
    JBPopupMenu popupMenuL,popupMenuR;
    TableTool tool = new TableTool();

    public MyTableListener(JBTable tableL,JBTable tableR,JBTable tableStatus,JBPopupMenu popupMenuL,
                           JBPopupMenu popupMenuR) {
        this.tableL = tableL;
        this.tableR = tableR;
        this.tableStatus = tableStatus;
        this.popupMenuL = popupMenuL;
        this.popupMenuR = popupMenuR;
        setListener();
    }

    void setListener() {
        tableL.getSelectionModel().addListSelectionListener(this);
        tableR.getSelectionModel().addListSelectionListener(this);
        tableL.addMouseListener(this);
        tableR.addMouseListener(this);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getSource() == tableL.getSelectionModel()){
            int[] selectedRows = tableL.getSelectedRows();
            if(selectedRows.length == 0){
                return;
            }
            for(int i : selectedRows){
                if(i < tableR.getRowCount()){
                    tableR.setRowSelectionInterval(i,i);
                }
            }
        } else if (e.getSource() == tableR.getSelectionModel()) {
            int[] selectedRows = tableR.getSelectedRows();
            if(selectedRows.length == 0){
                return;
            }
            for(int i : selectedRows){
                if(i < tableL.getRowCount()){
                    tableL.setRowSelectionInterval(i,i);
                }
            }
        }
        setStatus();
        refreshTableCellRenderer();
    }

    public void setStatus() {
        MyTableModel model = (MyTableModel)tableStatus.getModel();
        Vector<Vector<Object>> result = new Vector<>();
        Vector<String> headers = new Vector<>();
        getSelectedData(tableL,result);
        getSelectedData(tableR,result);
        getHeader(headers);
        model.setDataVector(result,headers);
    }

    public void refreshTableCellRenderer(){
        int indexL = tableL.getSelectedRow();
        int indexR = tableR.getSelectedRow();
        if(indexL != indexR){
            return;
        }
        MyTableCellRenderer tableCellRenderer = new MyTableCellRenderer(resolveDifference(indexL));
        tool.setTableStyle(tableStatus,tableCellRenderer);
    }

    Resolver.Difference resolveDifference(int index){
        Resolver.Difference difference = new Resolver.Difference();
        Resolver.Difference currentDifference = Resolver.getInstance().getDifference();
        List<Resolver.DifferenceCell> result = currentDifference.getResult();
        for(Resolver.DifferenceCell differenceCell : result){
            if(differenceCell.getRow() == index){
                difference.getResult().add(new Resolver.DifferenceCell(0,differenceCell.getCol()));
                difference.getResult().add(new Resolver.DifferenceCell(1,differenceCell.getCol()));
            }
        }
        Resolver.getInstance().setStatusDifference(difference);
        return difference;
    }

    public void getSelectedData(JBTable table, Vector<Vector<Object>> result) {
        Vector<Object> rows = new Vector<>();
        if(table.getSelectedRow() < 0){
            return;
        }
        int row = table.getSelectedRow();
        for(int i = 0 ; i < table.getColumnCount() ; i++){
            rows.add(table.getValueAt(row,i));
        }
        result.add(rows);
    }

    public void getHeader(Vector<String> headers) {
        if(tableL.getColumnCount() == 0){
            for(int i = 0 ; i < tableR.getColumnCount() ; i++){
                headers.add(tableR.getColumnName(i));
            }
        } else {
            for(int i = 0 ; i < tableL.getColumnCount() ; i++){
                headers.add(tableL.getColumnName(i));
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
//            if (e.getSource() == tableL) {
//                int selectIndex = tableL.rowAtPoint(e.getPoint());
//                tableL.setRowSelectionInterval(selectIndex,selectIndex);
//                popupMenuL.show(tableL,e.getX(),e.getY());
//            } else if (e.getSource() == tableR) {
//                int selectIndex = tableR.rowAtPoint(e.getPoint());
//                tableR.setRowSelectionInterval(selectIndex,selectIndex);
//                popupMenuR.show(tableR,e.getX(),e.getY());
//            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

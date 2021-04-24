package com.chesterccw.exceldiffer.resolve;

import com.chesterccw.exceldiffer.ui.MyToolWindow;
import com.chesterccw.exceldiffer.ui.component.MyTableModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author chesterccw
 */
public class Resolver {

    public static final Resolver INSTANCE = new Resolver();
    public static Difference difference = new Difference();
    public static Difference statusDifference = new Difference();

    private Resolver() {

    }

    public static Resolver getInstance() {
        return INSTANCE;
    }

    public Difference getDifference() {
        return difference;
    }

    @SuppressWarnings("all")
    public Difference resolve(){
        MyToolWindow toolWindow = MyToolWindow.getInstance();
        MyTableModel tableModelL = toolWindow.getTableModelL();
        MyTableModel tableModelR = toolWindow.getTableModelR();


        Vector<Vector> dataVectorL = tableModelL.getDataVector();
        Vector<Vector> dataVectorR = tableModelR.getDataVector();

        int length = Math.min(dataVectorL.size(),dataVectorR.size());
        difference.result.clear();
        for(int i = 0 ; i < length ; i++) {
            equals(dataVectorL.get(i),dataVectorR.get(i),i);
        }

        toolWindow.setTableStyle();
        toolWindow.refreshDifferenceCountLabelText(difference.result.size() + " difference ");
        return difference;
    }

    void equals(Vector<Object> var1, Vector<Object> var2, int col){
        int length = Math.min(var1.size(),var2.size());
        for(int i = 0 ; i < length ; i++) {
            if(!equals(var1.get(i),var2.get(i))){
                difference.result.add(new DifferenceCell(col,i));
            }
        }
    }

    boolean equals(Object var1, Object var2){
        if(var1 == null && var2 == null){
            return true;
        }
        if(var1 == null || var2 == null){
            return false;
        }
        return var1.equals(var2);
    }

    public static class Difference {
        private final List<DifferenceCell> result = new ArrayList<>();
        public List<DifferenceCell> getResult() {
            return result;
        }
    }

    public static class DifferenceCell {
        int row;
        int col;

        public DifferenceCell(int row, int col){
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        @Override
        public String toString() {
            return "[" + row + "," + col +"]";
        }
    }

    public Difference getStatusDifference() {
        return statusDifference;
    }

    public void setStatusDifference(Difference statusDifference) {
        Resolver.statusDifference = statusDifference;
    }
}

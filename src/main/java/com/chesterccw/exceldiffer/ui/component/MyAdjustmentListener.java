package com.chesterccw.exceldiffer.ui.component;

import com.intellij.ui.components.JBScrollPane;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * @author chesterccw
 * @date 2020/8/6
 */
public class MyAdjustmentListener implements AdjustmentListener {

    JBScrollPane scrollPaneL,scrollPaneR;

    public MyAdjustmentListener(JBScrollPane scrollPaneL,JBScrollPane scrollPaneR){
        this.scrollPaneL = scrollPaneL;
        this.scrollPaneR = scrollPaneR;
        scrollPaneL.getVerticalScrollBar().addAdjustmentListener(this);
        scrollPaneR.getVerticalScrollBar().addAdjustmentListener(this);
        scrollPaneL.getHorizontalScrollBar().addAdjustmentListener(this);
        scrollPaneR.getHorizontalScrollBar().addAdjustmentListener(this);
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        if(e.getSource() == scrollPaneL.getVerticalScrollBar()){
            scrollPaneR.getVerticalScrollBar().setValue(e.getValue());
        } else if (e.getSource() == scrollPaneL.getHorizontalScrollBar()) {
            scrollPaneR.getHorizontalScrollBar().setValue(e.getValue());
        } else if (e.getSource() == scrollPaneR.getVerticalScrollBar()) {
            scrollPaneL.getVerticalScrollBar().setValue(e.getValue());
        } else if (e.getSource() == scrollPaneR.getHorizontalScrollBar()) {
            scrollPaneL.getHorizontalScrollBar().setValue(e.getValue());
        }
    }

}

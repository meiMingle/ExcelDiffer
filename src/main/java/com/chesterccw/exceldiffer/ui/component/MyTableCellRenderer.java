package com.chesterccw.exceldiffer.ui.component;

import com.chesterccw.exceldiffer.resolve.Resolver;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * @author wuzhijian
 */
public class MyTableCellRenderer extends DefaultTableCellRenderer {

	private static final Color BASE_COLOR = JBColor.WHITE;
	private static final Color EVEN_COLOR = new JBColor(new Color(243, 249, 255), new Color(243, 249, 255));
	private static final Color GREW_COLOR = Gray._245;
	private static final Color DEFAULT_COLOR = Gray._60;
	private static final Color DARK_COLOR = new JBColor(Gray._36, Gray._36);
	private static final Color DARK_FONT_COLOR = new JBColor(Gray._187, Gray._187);
	private static final Color SELECTED_BACKGROUND_COLOR = new JBColor(new Color(40, 145, 225), new Color(40, 145, 225));
	private static final Color DIFFERENCE_COLOR = new JBColor(new Color(240, 210, 210), new Color(240, 210, 210));

	private Resolver.Difference difference;

	public MyTableCellRenderer() {

	}

	public MyTableCellRenderer(Resolver.Difference difference) {
		this.difference = difference;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(!UIUtil.isUnderDarcula()){
			if(isSelected){
				setBackground(SELECTED_BACKGROUND_COLOR);
				setForeground(JBColor.WHITE);
			} else {
				if ((row+1) % 6 == 0) {
					setBackground(EVEN_COLOR);
				} else if ((row+1) % 3 == 0) {
					setBackground(GREW_COLOR);
				} else {
					setBackground(BASE_COLOR);
				}
				setForeground(DEFAULT_COLOR);
			}
		} else {
			if(isSelected){
				setBackground(SELECTED_BACKGROUND_COLOR);
				setForeground(JBColor.WHITE);
			} else {
				if ((row+1) % 3 == 0) {
					setBackground(DARK_COLOR);
				} else {
					setBackground(BASE_COLOR);
				}
				setForeground(DARK_FONT_COLOR);
			}
		}
		if(difference != null) {
			List<Resolver.DifferenceCell> result = difference.getResult();
			for(Resolver.DifferenceCell cell : result) {
				if(cell.getRow() == row && cell.getCol() == column){
					setBackground(DIFFERENCE_COLOR);
				}
			}
		}

		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}

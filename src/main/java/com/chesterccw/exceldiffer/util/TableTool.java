package com.chesterccw.exceldiffer.util;

import com.chesterccw.exceldiffer.resolve.Resolver;
import com.chesterccw.exceldiffer.ui.component.MyTableCellRenderer;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Enumeration;
import java.util.List;

/**
 * @author wuzhijian
 */
public class TableTool {

	/**
	 * 设置 JTable 样式
	 * @param table JTable
	 */
	public void setTableStyle(JTable table) {
		MyTableCellRenderer r;
		Resolver.Difference difference = Resolver.getInstance().getDifference();
		if(difference.getResult().size() == 0){
			r = new MyTableCellRenderer();
		} else {
			r = new MyTableCellRenderer(difference);
		}
		setTableStyle(table,r);
	}

	/**
	 * 设置 JTable 样式
	 * @param table JTable
	 * @param r MyTableCellRenderer
	 */
	public void setTableStyle(JTable table, MyTableCellRenderer r) {
		// 设置选中项背景
		table.setSelectionBackground(new JBColor(new Color(48, 106, 190), new Color(48, 106, 190)));
		table.setSelectionForeground(Gray._255);
		// 设置行高
		table.setAutoCreateRowSorter(true);
		table.setRowHeight(27);
		r.setHorizontalAlignment(SwingConstants.LEFT);
		table.setDefaultRenderer(Object.class, r);
		table.getTableHeader().setFont(MyFont.Bold);
		table.getTableHeader().setReorderingAllowed(false);
		table.setFont(MyFont.Common);
		fitTableColumns(table);
		setDefaultColumnColor(table,r);
	}

	/**
	 * 设置 JTable 列宽
	 * @param table JTable
	 */
	private void fitTableColumns(JTable table) {
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JTableHeader header = table.getTableHeader();
		int rowCount = table.getRowCount();
		Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = columns.nextElement();
			int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
			int width = (int) table.getTableHeader().getDefaultRenderer()
					.getTableCellRendererComponent(table, column.getIdentifier()
							, false, false, -1, col).getPreferredSize().getWidth();
			for (int row = 0; row < rowCount; row++) {
				int preferedWidth = (int) table.getCellRenderer(row, col).getTableCellRendererComponent(table,
						table.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
				width = Math.max(width, preferedWidth);
			}
			header.setResizingColumn(column);
			column.setWidth(width + table.getIntercellSpacing().width + 20);
		}
	}

	/**
	 * 设置 JScrollPane 的样式
	 * @param scrollPane JScrollPane
	 */
	public void setJspStyle(JScrollPane scrollPane) {
		if(scrollPane == null){
			return;
		}
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.getHorizontalScrollBar().setOpaque(false);
	}

	private static void setDefaultColumnColor(JTable table, MyTableCellRenderer tcr) {
		try {
			for (int i = 0; i < table.getColumnCount(); i++) {
				table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.chesterccw.exceldiffer.ui;

import cn.hutool.core.util.StrUtil;
import com.chesterccw.exceldiffer.actions.MyButtonActions;
import com.chesterccw.exceldiffer.load.Loader;
import com.chesterccw.exceldiffer.notify.MyNotifier;
import com.chesterccw.exceldiffer.ui.component.MyAdjustmentListener;
import com.chesterccw.exceldiffer.ui.component.MyTableListener;
import com.chesterccw.exceldiffer.util.*;
import com.chesterccw.exceldiffer.ui.component.MyTableModel;

import com.intellij.ide.actions.ShowFilePathAction;
import com.intellij.notification.Notification;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.components.JBBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;

/**
 * @author chesterccw
 * @date 2020/7/21
 */
public class MyToolWindow {

    private static final MyToolWindow INSTANCE = new MyToolWindow();
    private boolean initialization = false;

    private static final String BUTTON_KEY = "SWING_BUTTON_KEY";
    public JPanel mainPanel;
    JPanel tablePanel,statusPanel,panelL,panelR,resultPanelL,resultPanelR;
    JBTable tableL,tableR,tableStatus;
    JBScrollPane scrollPaneL,scrollPaneR,scrollPaneStatus;
    JSplitPane splitPane;
    TableTool tool = new TableTool();
    MyTableModel tableModelL,tableModelR,tableModelStatus;
    JBTextField textFieldL,textFieldR;
    MyAdjustmentListener listener;
    MyTableListener listSelectionListener;
    NonOpaquePanel operationPanel;
    JBBox box;
    JBLabel differenceCountLabel;
    JBLabel rowsCountL,selectedRowL,editingL;
    JBLabel rowsCountR,selectedRowR,editingR;
    JBPopupMenu popupMenuL,popupMenuR;
    JBMenuItem acceptL,acceptR,revealInFinderL,revealInFinderR;

    private MyToolWindow () {

    }

    public static MyToolWindow getInstance() {
        return INSTANCE;
    }

    public boolean isInitialization() {
        return initialization;
    }

    public void init() {
        initTable();
        initOperation();
        initStatus();
        initMain();
        initialization = true;
    }

    void initMain() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(box,BorderLayout.NORTH);
        mainPanel.add(tablePanel,BorderLayout.CENTER);
        mainPanel.add(statusPanel,BorderLayout.SOUTH);
    }

    void initOperation() {
        box = JBBox.createHorizontalBox();
        operationPanel = new NonOpaquePanel(new FlowLayout(FlowLayout.LEFT));
        DefaultActionGroup actionGroup = new DefaultActionGroup();

        actionGroup.add(new MyButtonActions.PrevAction());
        actionGroup.add(new MyButtonActions.NextAction());
        actionGroup.addSeparator();
        actionGroup.add(new MyButtonActions.ReloadAction());
        // actionGroup.addSeparator();
        // actionGroup.add(new MyButtonActions.AcceptAllLeftAction());
        // actionGroup.add(new MyButtonActions.AcceptAllRightAction());

        @NotNull
        AnAction[] myActions = actionGroup.getChildren(null);
        for (AnAction action : myActions) {
            if (action instanceof Separator) {
                continue;
            }
            if(StrUtil.isEmpty(action.getTemplateText())){
                continue;
            }
            Presentation presentation = PresentationUtil.getPresentation(action.getTemplateText());
            ActionButton button = new ActionButton(action, presentation, ActionPlaces.UNKNOWN,
                    ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE);
            operationPanel.add(button);
            presentation.putClientProperty(BUTTON_KEY, button);
        }
        box.add(operationPanel);
        differenceCountLabel = new JBLabel("32 difference ");
        box.add(differenceCountLabel);
    }

    void initTable() {
        tablePanel = new JPanel(new BorderLayout());
        splitPane = new JSplitPane();
        splitPane.setBorder(JBUI.Borders.empty());

        initPopupMenu();
        initTableModel();

        tableL = new JBTable(tableModelL);
        tableR = new JBTable(tableModelR);
        tableStatus = new JBTable(tableModelStatus);

        setTableStyle();
        listSelectionListener = new MyTableListener(tableL,tableR,tableStatus,popupMenuL,popupMenuR);

        scrollPaneL = new JBScrollPane(tableL);
        scrollPaneR = new JBScrollPane(tableR);
        scrollPaneStatus = new JBScrollPane(tableStatus);
        scrollPaneStatus.setPreferredSize(new Dimension(scrollPaneStatus.getWidth(),80));
        listener = new MyAdjustmentListener(scrollPaneL,scrollPaneR);

        tool.setJspStyle(scrollPaneL);
        tool.setJspStyle(scrollPaneR);
        tool.setJspStyle(scrollPaneStatus);

        panelL = new JPanel(new BorderLayout());
        panelR = new JPanel(new BorderLayout());

        textFieldL = new JBTextField();
        textFieldR = new JBTextField();

        textFieldL.setBorder(JBUI.Borders.customLine(null,1));
        textFieldR.setBorder(JBUI.Borders.customLine(null,1));

        initResultPanel();

        panelL.add(textFieldL,BorderLayout.NORTH);
        panelL.add(scrollPaneL,BorderLayout.CENTER);
        // panelL.add(resultPanelL,BorderLayout.SOUTH);
        panelR.add(textFieldR,BorderLayout.NORTH);
        panelR.add(scrollPaneR,BorderLayout.CENTER);
        // panelR.add(resultPanelR,BorderLayout.SOUTH);

        splitPane.setLeftComponent(panelL);
        splitPane.setRightComponent(panelR);

        JFrame frame = WindowManager.getInstance().getFrame(Loader.getInstance().getProject());
        int dividerLocation = 800;
        if(frame != null){
            dividerLocation = frame.getWidth()/2 - 25;
            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    splitPane.setDividerLocation(frame.getWidth()/2 - 25);
                }
            });
        }
        splitPane.setDividerLocation(dividerLocation);
        splitPane.setDividerSize(5);
        tablePanel.add(splitPane);
    }

    void initTableModel(){
        tableModelL = new MyTableModel();
        tableModelR = new MyTableModel();
        tableModelStatus = new MyTableModel();
    }

    void initPopupMenu(){
        initMenuItem();
        popupMenuL = new JBPopupMenu();
        popupMenuR = new JBPopupMenu();

        // popupMenuL.add(acceptL);
        // popupMenuL.addSeparator();
        popupMenuL.add(revealInFinderL);

        // popupMenuR.add(acceptR);
        // popupMenuR.addSeparator();
        popupMenuR.add(revealInFinderR);
    }

    void initMenuItem(){
        acceptL = new JBMenuItem("Accept Left",IconLoader.findIcon("/diff/arrowRight.svg"));
        acceptR = new JBMenuItem("Accept Right",IconLoader.findIcon("/diff/arrow.svg"));
        revealInFinderL = new JBMenuItem("Reveal in Finder");
//        revealInFinderL.addActionListener(new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                showFile(textFieldL);
//            }
//        });
        revealInFinderR = new JBMenuItem("Reveal in Finder");
//        revealInFinderR.addActionListener(new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                showFile(textFieldR);
//            }
//        });
    }

//    public void showFile(JBTextField textField){
//        if(textField == null || StrUtil.isEmpty(textField.getText())){
//            return;
//        }
//        File file = new File(textField.getText());
//        if(!file.exists()){
//            Notification notify = notifier.notify("File " + file.getPath() + " not exists!");
//            return;
//        }
//        if(file.isDirectory()){
//            Notification notify = notifier.notify("File " + file.getPath() + " is directory!");
//            return;
//        }
//         ShowFilePathAction.openFile(file);
//    }

    public void setTableStyle(){
        tool.setTableStyle(tableL);
        tool.setTableStyle(tableR);
        tool.setTableStyle(tableStatus);
    }

    void initResultPanel(){
        resultPanelL = new JPanel(new GridLayout(1,3));
        resultPanelR = new JPanel(new GridLayout(1,3));

        rowsCountL = new JBLabel("Rows count: 85");
        rowsCountR = new JBLabel("Rows count: 35");
        selectedRowL = new JBLabel("Selected row: 20");
        selectedRowR = new JBLabel("Selected row: 18");
        editingL = new JBLabel("Editing disabled");
        editingR = new JBLabel("Editing disabled");

        rowsCountL.setFont(MyFont.Common);
        rowsCountR.setFont(MyFont.Common);
        selectedRowL.setFont(MyFont.Common);
        selectedRowR.setFont(MyFont.Common);
        editingL.setFont(MyFont.Common);
        editingR.setFont(MyFont.Common);

        resultPanelL.add(rowsCountL);
        resultPanelL.add(selectedRowL);
        resultPanelL.add(editingL);

        resultPanelR.add(rowsCountR);
        resultPanelR.add(selectedRowR);
        resultPanelR.add(editingR);
    }

    void initStatus() {
        statusPanel = new JPanel(new GridLayout(1,1));
        statusPanel.add(scrollPaneStatus);
    }

    public void setData(Vector<String> header, Vector<Vector<Object>> rows, String container, String filePath) {
        if(StrUtil.isEmpty(container)){
            return;
        }
        if(container.equals(Constant.LEFT_CONTAINER)){
            tableModelL.setDataVector(rows,header);
            textFieldL.setText(filePath);
        } else if(container.equals(Constant.RIGHT_CONTAINER)) {
            tableModelR.setDataVector(rows,header);
            textFieldR.setText(filePath);
        }
        tool.setTableStyle(tableL);
        tool.setTableStyle(tableR);
        tool.setTableStyle(tableStatus);
        tool.setJspStyle(scrollPaneL);
        tool.setJspStyle(scrollPaneR);
        tool.setJspStyle(scrollPaneStatus);
    }

    public JBTable getTableL() {
        return tableL;
    }

    public JBTable getTableR() {
        return tableR;
    }

    public JBTable getTableStatus() {
        return tableStatus;
    }

    public MyTableModel getTableModelL() {
        return tableModelL;
    }

    public MyTableModel getTableModelR() {
        return tableModelR;
    }

    public JBTextField getTextFieldL() {
        return textFieldL;
    }

    public JBTextField getTextFieldR() {
        return textFieldR;
    }

    public void refreshDifferenceCountLabelText(String text) {
        if(text == null){
            return;
        }
        differenceCountLabel.setText(text);
    }
}

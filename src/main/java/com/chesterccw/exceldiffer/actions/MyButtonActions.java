package com.chesterccw.exceldiffer.actions;

import cn.hutool.core.util.StrUtil;
import com.chesterccw.exceldiffer.ExcelDifferAction;
import com.chesterccw.exceldiffer.resolve.Resolver;
import com.chesterccw.exceldiffer.ui.MyToolWindow;
import com.chesterccw.exceldiffer.util.Constant;
import com.chesterccw.exceldiffer.util.ResolveData;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * @author chesterccw
 * @date 2020/8/7
 */
public class MyButtonActions {

    static int currentIndex = -1;

    public static class PrevAction extends AnAction {
        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            findDifferenceCell(false);
        }

        @Override
        public @Nullable @Nls(capitalization = Nls.Capitalization.Title) String getTemplateText() {
            return "PREV";
        }
    }

    public static class NextAction extends AnAction {
        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            findDifferenceCell(true);
        }

        @Override
        public @Nullable @Nls(capitalization = Nls.Capitalization.Title) String getTemplateText() {
            return "NEXT";
        }
    }

    static void findDifferenceCell(boolean isNext){
        JBTable tableL = MyToolWindow.getInstance().getTableL();
        JBTable tableR = MyToolWindow.getInstance().getTableR();
        List<Resolver.DifferenceCell> result = Resolver.getInstance().resolve().getResult();
        int maxIndex = result.size();
        if(maxIndex == 0){
            return;
        }
        if(isNext){
            currentIndex = Math.min(currentIndex + 1, maxIndex);
            if(currentIndex == maxIndex){
                currentIndex = maxIndex - 1;
            }
        } else {
            currentIndex = Math.max(currentIndex - 1, 0);
        }
        Resolver.DifferenceCell differenceCell = result.get(currentIndex);
        if(differenceCell == null){
            return;
        }
        Rectangle cellRectL = tableL.getCellRect(differenceCell.getRow(), differenceCell.getCol(), true);
        tableL.scrollRectToVisible(cellRectL);
        tableL.setRowSelectionInterval(differenceCell.getRow(),differenceCell.getRow());

        Rectangle cellRectR = tableR.getCellRect(differenceCell.getRow(), differenceCell.getCol(), true);
        tableR.scrollRectToVisible(cellRectR);
        tableR.setRowSelectionInterval(differenceCell.getRow(),differenceCell.getRow());

        JBTable tableStatus = MyToolWindow.getInstance().getTableStatus();
        List<Resolver.DifferenceCell> statusResult = Resolver.getInstance().getStatusDifference().getResult();
        Resolver.DifferenceCell statusDifferenceCell = statusResult.get(0);
        Rectangle cellRectStatus = tableStatus.getCellRect(statusDifferenceCell.getRow(), statusDifferenceCell.getCol(), true);
        tableStatus.scrollRectToVisible(cellRectStatus);
    }

    public static class ReloadAction extends AnAction {
        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            String textL = MyToolWindow.getInstance().getTextFieldL().getText();
            String textR = MyToolWindow.getInstance().getTextFieldR().getText();
            if(StrUtil.isNotEmpty(textL)){
                File file = new File(textL);
                ExcelDifferAction.Data data = new ResolveData(file).resolve();
                MyToolWindow.getInstance().setData(data.header,data.rows, Constant.LEFT_CONTAINER,textL);
            }
            if(StrUtil.isNotEmpty(textR)){
                File file = new File(textR);
                ExcelDifferAction.Data data = new ResolveData(file).resolve();
                MyToolWindow.getInstance().setData(data.header,data.rows,Constant.RIGHT_CONTAINER,textR);
                Resolver.getInstance().resolve();
            }
        }

        @Override
        public @Nullable @Nls(capitalization = Nls.Capitalization.Title) String getTemplateText() {
            return "RELOAD";
        }
    }

    public static class AcceptAllLeftAction extends AnAction {
        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            System.out.println("Accept All Left");
        }

        @Override
        public @Nullable @Nls(capitalization = Nls.Capitalization.Title) String getTemplateText() {
            return "AcceptAllLeft";
        }
    }

    public static class AcceptAllRightAction extends AnAction {
        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            System.out.println("Accept All Right");
        }

        @Override
        public @Nullable @Nls(capitalization = Nls.Capitalization.Title) String getTemplateText() {
            return "AcceptAllRight";
        }
    }

}

package com.chesterccw.exceldiffer.load;

import com.chesterccw.exceldiffer.ExcelDifferAction;
import com.chesterccw.exceldiffer.ui.MyToolWindow;
import com.chesterccw.exceldiffer.ui.MyToolWindowFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.RegisterToolWindowTask;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;

import javax.swing.*;
import java.util.function.Supplier;

/**
 * @author chesterccw
 * @date 2020/7/30
 */
public class Loader {

    private static final Loader INSTANCE = new Loader();

    Project project;

    private Loader(){

    }

    public Project getProject() {
        return project;
    }

    public static Loader getInstance() {
        return INSTANCE;
    }

    public void load(ExcelDifferAction.Data data, Project project, String container, String filePath){
        this.project = project;
        MyToolWindow toolWindow = MyToolWindow.getInstance();
        if(!toolWindow.isInitialization()){
            toolWindow.init();
        }
        toolWindow.setData(data.header,data.rows,container,filePath);

        MyToolWindowFactory toolWindowFactory = new MyToolWindowFactory(toolWindow);
        JPanel panel = toolWindow.mainPanel;
        ToolWindowManager twm = ToolWindowManager.getInstance(project);
        ToolWindow excelReader = twm.getToolWindow("ExcelDiffer");
        if(excelReader != null){
            excelReader.remove();
        }
        Supplier<String> supplier = () -> "ExcelDiffer";
        RegisterToolWindowTask task = new RegisterToolWindowTask(
                "ExcelDiffer", ToolWindowAnchor.BOTTOM,
                panel,false,true,true,
                true,toolWindowFactory, IconLoader.findIcon("/actions/diff.svg"),supplier
        );
        ToolWindow cipherToolWindow = twm.registerToolWindow(task);
        cipherToolWindow.show(() -> {

        });
    }

}

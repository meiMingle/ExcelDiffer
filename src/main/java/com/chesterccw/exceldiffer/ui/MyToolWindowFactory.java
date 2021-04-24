package com.chesterccw.exceldiffer.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author chesterccw
 * @date 2020/7/17
 */
public class MyToolWindowFactory implements ToolWindowFactory {

    MyToolWindow myToolWindow;

    public MyToolWindowFactory(MyToolWindow toolWindow){
        this.myToolWindow = toolWindow;
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindow.mainPanel,"", false);
        toolWindow.getContentManager().removeAllContents(true);
        toolWindow.getContentManager().addContent(content);
    }

    @Override
    public boolean isApplicable(@NotNull Project project) {
        return false;
    }

}

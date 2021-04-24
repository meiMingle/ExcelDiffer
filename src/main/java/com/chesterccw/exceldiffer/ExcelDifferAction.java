package com.chesterccw.exceldiffer;

import com.chesterccw.exceldiffer.load.Loader;
import com.chesterccw.exceldiffer.resolve.Resolver;
import com.chesterccw.exceldiffer.util.AnActionEventUtil;
import com.chesterccw.exceldiffer.util.ResolveData;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Vector;

/**
 * @author chesterccw
 * @date 2020/8/6
 */
public class ExcelDifferAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if(project == null || virtualFile == null){
            return;
        }
        Data data = new ResolveData(virtualFile).resolve();
        String text = e.getPresentation().getText();
        Loader.getInstance().load(data,project,text,virtualFile.getPath());
        Resolver.getInstance().resolve();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(AnActionEventUtil.available(e));
    }

    public static class Data {
        public String title;
        public Vector<String> header = new Vector<>();
        public Vector<Vector<Object>> rows = new Vector<>();
    }

}

package com.chesterccw.exceldiffer.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.Arrays;

/**
 * @author chesterccw
 * @date 2020/8/6
 */
public class AnActionEventUtil {

    public static boolean available(AnActionEvent e){
        VirtualFile data = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if(data == null){
            return false;
        }
        String extension = data.getExtension();
        return Arrays.asList(Constant.SUFFIX_ARRAY).contains(extension);
    }

}

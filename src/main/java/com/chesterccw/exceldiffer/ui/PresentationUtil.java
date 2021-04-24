package com.chesterccw.exceldiffer.ui;

import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.util.IconLoader;

/**
 * @author chesterccw
 * @date 2020/8/7
 */
public class PresentationUtil {

    public static Presentation getPresentation(String templateText) {
        Presentation presentation = new Presentation();
        switch (templateText) {
            case "PREV":
                presentation.setText("Prev Difference");
                presentation.setIcon(IconLoader.findIcon("/actions/previousOccurence.svg"));
                break;
            case "NEXT":
                presentation.setText("Next Difference");
                presentation.setIcon(IconLoader.findIcon("/actions/nextOccurence.svg"));
                break;
            case "RELOAD":
                presentation.setText("Reload Files");
                presentation.setIcon(IconLoader.findIcon("/actions/refresh.svg"));
                break;
            case "AcceptAllLeft":
                presentation.setText("Accept All Left");
                presentation.setIcon(IconLoader.findIcon("/diff/arrowRight.svg"));
                break;
            case "AcceptAllRight":
                presentation.setText("Accept All Right");
                presentation.setIcon(IconLoader.findIcon("/diff/arrow.svg"));
            default:
                break;
        }
        return presentation;
    }

}

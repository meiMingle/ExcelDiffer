package com.chesterccw.exceldiffer.notify;

import com.chesterccw.exceldiffer.load.Loader;
import com.intellij.notification.*;
import com.intellij.openapi.project.Project;

/**
 * @author chesterccw
 */
public class MyNotifier {

     private final NotificationGroup notificationGroup =
            NotificationGroupManager.getInstance().
                    getNotificationGroup("Plugins Suggestion");


    public Notification notify(String content) {
        Project project = Loader.getInstance().getProject();
        return notify(project, content);
    }

    public Notification notify(Project project, String content) {
        final Notification notification = notificationGroup.createNotification(content, NotificationType.ERROR);
        notification.notify(project);
        return notification;
    }

}

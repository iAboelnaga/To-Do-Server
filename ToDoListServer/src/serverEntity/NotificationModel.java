/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverEntity;

/**
 *
 * @author tamimy
 */
public class NotificationModel {
    
    public static final String TeamMateRequest ="TeamMate";
    public static final String AssignTask ="AssignTask";
    public static final String TaskIsDone ="TaskDone";
    
    
    int senderId;
    String userName;
    
    String ProjectTitle;

    public String getProjectTitle() {
        return ProjectTitle;
    }

    public void setProjectTitle(String ProjectTitle) {
        this.ProjectTitle = ProjectTitle;
    }
    
    int taskId;
    String taskDesc;
    
    String notificationType;
    
    String notificationDate;

   
    
    //Task Done
    public NotificationModel( int TaskId, String TaskDesc, String NotificationDate) {
        this.taskId = TaskId;
        this.taskDesc = TaskDesc;
        this.notificationType = TaskIsDone;
        this.notificationDate=NotificationDate;
    }
    
    //TeamMate Requst
    public NotificationModel(String UserName,int senderId, String NotificationDate) {
        this.senderId = senderId;
        this.userName = UserName;
        this.notificationType = TeamMateRequest;
        this.notificationDate=NotificationDate;
    }

    //Assign Teammate Task
    public NotificationModel(String ProjectTitle, String UserName, int TaskId, String TaskDesc, String NotificationDate) {
        this.userName = UserName;
        this.taskId = TaskId;
        this.taskDesc = TaskDesc;
        this.notificationType = AssignTask;
        this.notificationDate=NotificationDate;
        this.ProjectTitle = ProjectTitle;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String UserName) {
        this.userName = UserName;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int TaskId) {
        this.taskId = TaskId;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String TaskDesc) {
        this.taskDesc = TaskDesc;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String NotificationType) {
        this.notificationType = NotificationType;
    }
    
     public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String NotificationDate) {
        this.notificationDate = NotificationDate;
    }    
}

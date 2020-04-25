/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverEntity;

public class CommentsModel {

    private int taskId;
    private int userId;
    private String commentDate;
    private String commentText;
    private String userName;

    public CommentsModel(int taskId, int userId, String UserName, String commentDate, String commentText) {
        this.taskId = taskId;
        this.userId = userId;
        this.commentDate = commentDate;
        this.commentText = commentText;
        this.userName = UserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String UserName) {
        this.userName = UserName;
    }

    

    public CommentsModel(int taskId, int userId, String commentDate, String commentText) {
        this.taskId = taskId;
        this.userId = userId;
        this.commentDate = commentDate;
        this.commentText = commentText;
    }
    

    public CommentsModel() {
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

  

   

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }
    
    

}

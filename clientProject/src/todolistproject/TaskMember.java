/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todolistproject;

/**
 *
 * @author moomen
 */
public class TaskMember {
    private int userID ;
    private int TodoID;

    public TaskMember(int userID, int TodoID) {
        this.userID = userID;
        this.TodoID = TodoID;
    }

    public int getUserID() {
        return userID;
    }

    public int getTodoID() {
        return TodoID;
    }
}

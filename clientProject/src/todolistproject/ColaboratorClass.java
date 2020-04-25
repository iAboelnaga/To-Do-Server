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
public class ColaboratorClass {
private int userID ;
private int TodoID;

    public ColaboratorClass(int userID, int TodoID) {
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

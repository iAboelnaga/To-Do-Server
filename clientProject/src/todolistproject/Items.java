/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todolistproject;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aboelnaga
 */
public class Items implements Entity {

    private int id;
    private int listId;
    private String title;
    private String comment;
    private String description;
    private String deadLine;
    private String startTime;
    private ArrayList<User> taskMember;
    private boolean status;

    public String getComment() {
        return comment;
    }

    public ArrayList<User> getTaskMember() {
        return taskMember;
    }

    public Items(int id, int listId, String title,  String startTime,String deadLine, ArrayList<User> taskMember) {
        this.id = id;
        this.listId = listId;
        this.title = title;
        this.deadLine = deadLine;
        this.startTime = startTime;
        this.taskMember = taskMember;
    }

    public Items(int id, int listId, String title,  String startTime,String deadLine) {
        this.id = id;
        this.title = title;
        this.listId = listId;
        this.deadLine = deadLine;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public int getListId() {
        return listId;
    }

    public Items(String title,int listId) {

        this.title = title;
        this.listId=listId;

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setTaskMember(ArrayList<User> taskMember) {
        this.taskMember = taskMember;
    }

    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status )
    {
    this.status=status;
    }
}

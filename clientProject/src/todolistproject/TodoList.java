/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todolistproject;

import java.util.List;

/**
 *
 * @author moomen
 */
public class TodoList {
private int ID;
private String title;//askl
private String desc;
private String OwnerName;
private int OwnerID;
private String DeadLine;
private String StartTime;
private String BG_Color;
private List<User> members;
private List<Items> Items;
private String Status;

    public TodoList(int ID, String title, String desc, int OwnerID, String DeadLine, String StartTime, String BG_Color, List<User> members, List<Items> Items) {
        this.ID = ID;
        this.title = title;
        this.desc = desc;
        this.OwnerID = OwnerID;
        this.DeadLine = DeadLine;
        this.StartTime = StartTime;
        this.BG_Color = BG_Color;
        this.members = members;
        this.Items = Items;
    }

    public TodoList(int ID, String desc, int OwnerID, String DeadLine, String StartTime, String BG_Color) {
        this.ID = ID;
        this.desc = desc;
        this.OwnerID = OwnerID;
        this.DeadLine = DeadLine;
        this.StartTime = StartTime;
        this.BG_Color = BG_Color;
    }
    
    //setters
    public void setID(int ID) {
        this.ID = ID;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setOwnerName(String OwnerName) {
        this.OwnerName = OwnerName;
    }
    public void setOwnerID(int OwnerID) {
        this.OwnerID = OwnerID;
    }
    public void setDeadLine(String DeadLine) {
        this.DeadLine = DeadLine;
    }
    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }
    public void setBG_Color(String BG_Color) {
        this.BG_Color = BG_Color;
    }
    public void setMembers(List<User> members) {
        this.members = members;
    }
    public void setItems(List<Items> Items) {
        this.Items = Items;
    }
    public void setStatus(String Status) {
        this.Status = Status;
    }

    //getters
    public int getID() {
        return ID;
    }
    public String getTitle() {
        return title;
    }
    public String getDesc() {
        return desc;
    }
    public String getOwnerName() {
        return OwnerName;
    }
    public int getOwnerID() {
        return OwnerID;
    }
    public String getDeadLine() {
        return DeadLine;
    }
    public String getStartTime() {
        return StartTime;
    }
    public String getBG_Color() {
        return BG_Color;
    }
    public List<User> getMembers() {
        return members;
    }
    public List<Items> getItems() {
        return Items;
    }
    public String getStatus() {
        return Status;
    }
}

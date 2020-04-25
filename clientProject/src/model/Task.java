/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aboelnaga
 */
public class Task {

    private int id;
    private String description;
     private String bg_color;
    private String status;
    private int listId;
     private int UserAssignId;
    private String UserAssignName;
    private String UserAssignStatus;

    public Task(int id, String description, String bg_color, String status, int listId, int UserAssignId, String UserAssignName, String UserAssignStatus) {
        this.id = id;
        this.description = description;
        this.bg_color = bg_color;
        this.status = status;
        this.listId = listId;
        this.UserAssignId = UserAssignId;
        this.UserAssignName = UserAssignName;
        this.UserAssignStatus = UserAssignStatus;
    }

    public int getUserAssignId() {
        return UserAssignId;
    }

    public void setUserAssignId(int UserAssignId) {
        this.UserAssignId = UserAssignId;
    }

   

    public String getUserAssignName() {
        return UserAssignName;
    }

    public String getUserAssignStatus() {
        return UserAssignStatus;
    }

    public void setUserAssignStatus(String UserAssignStatus) {
        this.UserAssignStatus = UserAssignStatus;
    }

    public void setUserAssignName(String UserAssignName) {
        this.UserAssignName = UserAssignName;
    }
   

    public Task(int id, String description, String bg_color, String status, int listId) {
        this.id = id;
        this.description = description;
        this.bg_color = bg_color;
        this.status = status;
        this.listId = listId;
    }

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBg_color() {
        return bg_color;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }


   

   
}

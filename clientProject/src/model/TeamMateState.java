/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author tamimy
 */
public class TeamMateState {
    int userID;
    int teamMate;
    String state;

    public TeamMateState(int userID, int teamMate, String state) {
        this.userID = userID;
        this.teamMate = teamMate;
        this.state = state;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTeamMate() {
        return teamMate;
    }

    public void setTeamMate(int teamMate) {
        this.teamMate = teamMate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
}

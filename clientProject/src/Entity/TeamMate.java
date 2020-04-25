/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;


public class TeamMate {
    private String teamMateName;
    private int teamMateId;
    private String teamMateStatus;
    boolean online;

    public TeamMate(String teamMateName, int teamMateId, String teamMateStatus, boolean online) {
        this.teamMateName = teamMateName;
        this.teamMateId = teamMateId;
        this.teamMateStatus = teamMateStatus;
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    
    

    public TeamMate( int teamMateId,String teamMateName) {
        this.teamMateName = teamMateName;
        this.teamMateId = teamMateId;
    }

    public String getTeamMateStatus() {
        return teamMateStatus;
    }

    public void setTeamMateStatus(String teamMateStatus) {
        this.teamMateStatus = teamMateStatus;
    }

    public int getTeamMateId() {
        return teamMateId;
    }

    public void setTeamMateId(int teamMateId) {
        this.teamMateId = teamMateId;
    }

    public String getTeamMateName() {
        return teamMateName;
    }

    public void setTeamMateName(String teamMateName) {
        this.teamMateName = teamMateName;
    }
    
    
}

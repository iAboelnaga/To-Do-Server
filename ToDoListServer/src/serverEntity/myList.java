/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverEntity;

public class myList {
    private int id;
    private String title;
    private String assignDate;
    private String deadlineDate;
    private int ownerId;

    public int getId() {
        return id;
    }

    public myList(int id, String title, String assignDate, String deadlineDate, int ownerId) {
        this.id = id;
        this.title = title;
        this.assignDate = assignDate;
        this.deadlineDate = deadlineDate;
        this.ownerId = ownerId;
    }
   

    public myList() {
    }
    

    public void setId(int id) {
        this.id = id;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

   
    

    
    
}

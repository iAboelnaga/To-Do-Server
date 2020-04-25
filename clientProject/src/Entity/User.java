/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.Observable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Aboelnaga
 */
public class User extends Observable{
    
    private int id;
    private String userName;
    private String email;
    private String password;

    public User(int id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public User(int id, String userName, String password, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
    }
    
    public User(int id ,String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }
    public User(String email  , String password){
        this.email = email;
        this.password = password;
    }
    public User(String userName, String email, String password){
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
    public User(){
        this.userName = "";
    }
    
    
    public int getId() {
        return id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    public JSONObject getUserAsJson() throws JSONException{
        JSONObject user = new JSONObject();
                user.put("id",id);
                user.put("username",userName);
                user.put("email", email);
                user.put("password",password);
        
        return user;
    }

    
    public void setId(int id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

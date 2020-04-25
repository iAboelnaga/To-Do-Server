/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

/**
 *
 * @author Aboelnaga
 */
public class Validate {
    
    public static boolean checkPasswordEquality(String p1 ,String p2){
        return p1.equals(p2);
    }
    
    public static boolean checkPasswordAndEmail(String email, String pass){
        return (!email.isEmpty() && !pass.isEmpty());
    }
}

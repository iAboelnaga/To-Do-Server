/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author tamimy
 */
public class DataValidation {

    public static boolean isNotEmpty(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isEmpty = true;
        String validationString = null;

        if (inputTextField.getText().length()==0) {
            isEmpty = false;
            validationString = validationText;
        }
        
        inputLabel.setText(validationString);
        return isEmpty;
    }
    public static boolean isMatch(TextField passTextField, TextField rePassTextField, Label inputLabel, String validationText) {
        boolean isMatch = true;
        String validationString = null;

        if (!rePassTextField.getText().equals(passTextField.getText())) {
            isMatch = false;
            validationString = validationText;
        }
        
        inputLabel.setText(validationString);
        return isMatch;
    }
    
    public static boolean dataLength(TextField inputTextField, Label inputLabel, String validationText, int requiredLength) {
        boolean isDataLength = true;
        String validationString = null;

        if (inputTextField.getText().length() < requiredLength) {
            isDataLength = false;
            validationString = validationText;

        }
        inputLabel.setText(validationString);
        return isDataLength;

    }
    public static boolean textAlphabet(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isAlphabet = true;
        String validationString = null;

        if (!inputTextField.getText().matches("[a-z A-Z]+")) {
            isAlphabet = false;
            validationString = validationText;

        }
        inputLabel.setText(validationString);

        System.out.println(inputTextField.getText().matches("[a-z A-Z]"));
        return isAlphabet;

    }

    public static boolean textNumeric(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isNumeric = true;
        String validationString = null;

        if (!inputTextField.getText().matches("[0-9]+")) {
            isNumeric = false;
            validationString = validationText;

        }
        inputLabel.setText(validationString);
        return isNumeric;

    }

    public static boolean UserName(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isNumeric = true;
        String validationString = null;

        if (!inputTextField.getText().matches("[a-zA-Z0-9]+")) {
            isNumeric = false;
            validationString = validationText;

        }
        inputLabel.setText(validationString);
        return isNumeric;

    }
    
    public static boolean emailFormat(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isEmail = true;
        String validationString = null;

        if (!inputTextField.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com")) {
            isEmail = false;
            validationString = validationText;

        }
        inputLabel.setText(validationString);
        return isEmail;

    }

    public static boolean zID(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isZID = true;
        String validationString = null;

        if (!inputTextField.getText().matches("\\z[0-9]{7}")) {
            isZID = false;
            validationString = validationText;

        }
        inputLabel.setText(validationString);

        return isZID;

    }

    //Regular Expressions: zMail: \z[0-9]{7}
    public static boolean textFieldIsNull(TextField inputTextField, Label inputLabel, String validationText) {
        boolean isNull = false;
        String validationString = null;

        System.out.println("*******************************************************");

        //point out difference between null and isEmpty() *FIND OUT WHEN TO USE NULL
        if (inputTextField.getText().isEmpty()) {
            isNull = true;
            validationString = validationText;

        }
        String isEmpty = Boolean.toString(inputTextField.getText().isEmpty());
        String nil = Boolean.toString(inputTextField.getText() == null);

        inputLabel.setText(validationString);

        System.out.println("Label Should be Set to: " + validationString);
        System.out.println("Input TextField: " + inputTextField.getText());
        System.out.println("Null: " + nil + " isEmpty: " + isEmpty);

        return isNull;

    }

}

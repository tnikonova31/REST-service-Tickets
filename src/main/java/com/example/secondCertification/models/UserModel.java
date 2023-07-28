package com.example.secondCertification.models;

import com.example.secondCertification.models.answers.ApiAnswer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.regex.Pattern;

public class UserModel {
    public static final String RULE_PRHONE = "^8[0-9]{10}$";
    public static final String RULE_LOGIN = "^[A-Za-z]+([-_]?[a-z0-9]){4,}$";
    public static final String RULE_PASSWORD = "^[A-Za-z0-9]+([a-z0-9]){4,}$";
    public static boolean isUserDataCurrent(String login, String password, String phone){
        int flag =0;
        Pattern pattern = null;
        pattern = Pattern.compile(RULE_LOGIN);
        if (pattern.matcher(login).matches()) {
            flag++;
        }
        pattern = Pattern.compile(RULE_PRHONE);
        if(pattern.matcher(phone).matches()) {
            flag++;
        }
        pattern = Pattern.compile(RULE_PASSWORD);
        if(pattern.matcher(password).matches()) {
            flag++;
        }

        if (flag==3) {
            return true;
        }
        return false;
    }

    public static ResponseEntity<?> createEntity(String entity){
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.OK.value(),
                entity + " is created"),
                HttpStatus.OK);
    }
    public static ResponseEntity<?> entityNotCreate(String entity){
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.BAD_REQUEST.value(),
                entity + " is not created"),
                HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> userToken(String token){
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.OK.value(),
                "Token: " + token),
                HttpStatus.OK);
    }

    public static ResponseEntity<?> userNotRegistr(){
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.BAD_REQUEST.value(),
                "User is not registered."),
                HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> userLogOut(){
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.OK.value(),
                "User is logOut."),
                HttpStatus.OK);
    }

    public static ResponseEntity<?> notFind(String parametr){
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.NOT_FOUND.value(),
                parametr + " is not find."),
                HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<?> badUserData(){
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.BAD_REQUEST.value(),
                "Not all data for user is filled right"),
                HttpStatus.BAD_REQUEST);
    }
}

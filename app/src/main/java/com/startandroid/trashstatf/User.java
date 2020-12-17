package com.startandroid.trashstatf;

public class User {

    private String name;
    private String email;
    private String pass;
    private String phone;

    User(){}


    public User(String name, String email, String pass, String phone){
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    String getEmail(){
        return email;
    }

    void setEmail(String email){
        this.email = email;
    }

    String getPass(){
        return pass;
    }

    void setPass(String pass){
        this.pass = pass;
    }

    String getPhone(){
        return phone;
    }

    void setPhone(String phone){
        this.phone = phone;
    }






}

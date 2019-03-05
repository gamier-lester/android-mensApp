package com.thesis.group.periodecalcalendar;

public class User {
    String userUserName;
    String userPassword;
    String userContact;
    String userBirthDate;
    String userEmail;

    public User()
    {

    }

    public User(String userUserName, String userPassword, String userContact, String userBirthDate, String userEmail)
    {
        this.userUserName = userUserName;
        this.userPassword = userPassword;
        this.userContact = userContact;
        this.userBirthDate = userBirthDate;
        this.userEmail = userEmail;
    }

    public String getUserUserName() {
        return userUserName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserContact() {
        return userContact;
    }

    public String getUserBirthDate() {
        return userBirthDate;
    }

    public String getUserEmail() {
        return userEmail;
    }
}

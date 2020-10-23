package com.example.raternet_isp_app;

public class User
{
    private String emailId=null;
    private String firstName=null;
    private String lastName=null;
    private String photoURL = null;
    private String phoneNumber = null;

    public User() {}

    public User(String email,String fname,String lname)
    {
        this.emailId=email;
        this.firstName=fname;
        this.lastName=lname;
    }


    public User(String emailId, String firstName, String lastName, String photoURL, String phoneNumber) {
        this.emailId = emailId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoURL = photoURL;
        this.phoneNumber = phoneNumber;
    }


    public String getEmailId() {
        return emailId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}

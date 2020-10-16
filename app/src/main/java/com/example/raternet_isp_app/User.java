package com.example.raternet_isp_app;

public class User
{
    private String emailId=null;
    private String firstName=null;
    private String lastName=null;
    private String Uid=null;

    public User() {}

    public User(String email,String fname,String lname)
    {
        this.emailId=email;
        this.firstName=fname;
        this.lastName=lname;
    }

    public String getUid() {
        return Uid;
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

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}

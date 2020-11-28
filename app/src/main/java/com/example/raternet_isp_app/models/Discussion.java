package com.example.raternet_isp_app.models;

public class Discussion
{
    private String userEmail="";
    private String ISP="";
    private String Locality="";
    private String issueType="";
    private String issueTitle="";
    private String issueDetails="";

    Discussion(){};

    public Discussion(String userEmail, String ISP, String locality, String issueType, String issueTitle, String issueDetails) {
        this.userEmail = userEmail;
        this.ISP = ISP;
        Locality = locality;
        this.issueType = issueType;
        this.issueTitle = issueTitle;
        this.issueDetails = issueDetails;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getISP() {
        return ISP;
    }

    public void setISP(String ISP) {
        this.ISP = ISP;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getIssueDetails() {
        return issueDetails;
    }

    public void setIssueDetails(String issueDetails) {
        this.issueDetails = issueDetails;
    }
}

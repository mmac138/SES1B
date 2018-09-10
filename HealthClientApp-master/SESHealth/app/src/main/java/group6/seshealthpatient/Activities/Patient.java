package group6.seshealthpatient.Activities;

public class Patient {
    String patientID;
    String patientName;
    String patientEmail;
    String patientPassword;
    String patientMedicalInfo;
    String patientDOB;
    String patientHeight;
    String patientWeight;

    public Patient(){

    }
    public Patient(String patientName, String patientEmail, String patientMedicalInfo, String patientDOB, String patientHeight, String patientWeight){

        patientName=patientName;
        patientEmail = patientEmail;
        patientMedicalInfo = patientMedicalInfo;
        patientDOB = patientDOB;
        patientHeight = patientHeight;
        patientWeight = patientWeight;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientPassword() {
        return patientPassword;
    }

    public void setPatientPassword(String patientPassword) {
        this.patientPassword = patientPassword;
    }

    public String getPatientMedicalInfo() {
        return patientMedicalInfo;
    }

    public void setPatientMedicalInfo(String patientMedicalInfo) {
        this.patientMedicalInfo = patientMedicalInfo;
    }

    public String getPatientDOB() {
        return patientDOB;
    }

    public void setPatientDOB(String patientDOB) {
        this.patientDOB = patientDOB;
    }

    public String getPatientHeight() {
        return patientHeight;
    }

    public void setPatientHeight(String patientHeight) {
        this.patientHeight = patientHeight;
    }

    public String getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(String patientWeight) {
        this.patientWeight = patientWeight;
    }
}

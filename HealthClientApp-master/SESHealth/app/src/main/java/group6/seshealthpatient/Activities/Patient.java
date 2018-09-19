package group6.seshealthpatient.Activities;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Patient {
    private String name;
    private String email;
    private String medicalInfo;
    private String dob;
    private String height;
    private String weight;
    private String gender;

    public Patient() {

    }

    public Patient(String name, String email, String medicalInfo, String dob, String height, String weight, String gender) {
        this.name = name;
        this.email = email;
        this.medicalInfo = medicalInfo;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("medicalInfo", medicalInfo);
        result.put("dob", dob);
        result.put("height", height);
        result.put("weight", weight);
        result.put("gender", gender);

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMedicalInfo() {
        return medicalInfo;
    }

    public void setMedicalInfo(String medicalInfo) {
        this.medicalInfo = medicalInfo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}


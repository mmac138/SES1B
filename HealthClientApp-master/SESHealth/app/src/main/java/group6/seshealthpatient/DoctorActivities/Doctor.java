package group6.seshealthpatient.DoctorActivities;

public class Doctor {
    private String name;
    private String email;
    private String dob;
    private String gender;
    private String address;

    public Doctor() {

    }

    public Doctor(String name, String email, String dob, String gender, String address) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}


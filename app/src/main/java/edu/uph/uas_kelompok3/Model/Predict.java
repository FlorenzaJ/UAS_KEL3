package edu.uph.uas_kelompok3.Model;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Predict extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private int age;
    private String gender;
    private String smokingHistory;
    private float breathingDifficulty;
    private String coughFrequency;
    private String allergies;
    private String chronicConditions;
    private String riskLevel;
    private float riskScore;
    private Date createdAt = new Date();
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSmokingHistory() {
        return smokingHistory;
    }

    public void setSmokingHistory(String smokingHistory) {
        this.smokingHistory = smokingHistory;
    }

    public float getBreathingDifficulty() {
        return breathingDifficulty;
    }

    public void setBreathingDifficulty(float breathingDifficulty) {
        this.breathingDifficulty = breathingDifficulty;
    }

    public String getCoughFrequency() {
        return coughFrequency;
    }

    public void setCoughFrequency(String coughFrequency) {
        this.coughFrequency = coughFrequency;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getChronicConditions() {
        return chronicConditions;
    }

    public void setChronicConditions(String chronicConditions) {
        this.chronicConditions = chronicConditions;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public float getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(float riskScore) {
        this.riskScore = riskScore;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}

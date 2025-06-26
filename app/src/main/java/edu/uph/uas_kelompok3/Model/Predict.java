package edu.uph.uas_kelompok3.Model;

public class Predict {
    public String date;
    public String status;
    public String riskLevel;

    public Predict(String date, String status, String riskLevel) {
        this.date = date;
        this.status = status;
        this.riskLevel = riskLevel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
}

package edu.uph.uas_kelompok3.Model;

public class Doctor {
    private String name;
    private String specialty;
    private float rating;
    private int reviews;
    private String availability;

    public Doctor(String name, String specialty, float rating, int reviews, String availability) {
        this.name = name;
        this.specialty = specialty;
        this.rating = rating;
        this.reviews = reviews;
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public float getRating() {
        return rating;
    }

    public int getReviews() {
        return reviews;
    }

    public String getAvailability() {
        return availability;
    }
}

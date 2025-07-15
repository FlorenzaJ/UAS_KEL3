package edu.uph.uas_kelompok3.Model;

public class UserModel {
    private String nama;
    private String email;
    private String gender;
    private String tanggalLahir;

    public UserModel(String nama, String email, String gender, String tanggalLahir) {
        this.nama = nama;
        this.email = email;
        this.gender = gender;
        this.tanggalLahir = tanggalLahir;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }
}

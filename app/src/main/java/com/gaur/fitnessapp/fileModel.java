package com.gaur.fitnessapp;

public class fileModel {
    String title,vURL;


    public fileModel() {
    }

    public fileModel(String title, String vURL) {
        this.title = title;
        this.vURL = vURL;
    }

    public String getTitle() {
        return title;
    }

    public String getvURL() {
        return vURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setvURL(String vURL) {
        this.vURL = vURL;
    }
}

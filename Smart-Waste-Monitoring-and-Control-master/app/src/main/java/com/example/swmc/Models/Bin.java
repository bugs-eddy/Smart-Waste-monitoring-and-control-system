package com.example.swmc.Models;

public class Bin {
    private String location;
    private double wasteLevel;
    private boolean submitted = false;
    private String onDuty = "Not Assigned";



    public Bin() {
    }

    public Bin(String location, double wasteLevel) {
        this.location = location;
        this.wasteLevel = wasteLevel;
    }

    public Bin(String location, double wasteLevel, boolean submitted, String onDuty) {
        this.location = location;
        this.wasteLevel = wasteLevel;
        this.submitted = submitted;
        this.onDuty = onDuty;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getWasteLevel() {
        return wasteLevel;
    }

    public void setWasteLevel(double wasteLevel) {
        this.wasteLevel = wasteLevel;
    }

    public String getOnDuty() {
        return onDuty;
    }

    public void setOnDuty(String onDuty) {
        this.onDuty = onDuty;
    }

}

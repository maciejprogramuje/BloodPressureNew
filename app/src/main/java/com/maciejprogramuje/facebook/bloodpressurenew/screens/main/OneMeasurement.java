package com.maciejprogramuje.facebook.bloodpressurenew.screens.main;

public class OneMeasurement {
    private String date;
    private int sys;
    private int dia;
    private int pulse;

    public OneMeasurement(String date, int sys, int dia, int pulse) {
        this.date = date;
        this.sys = sys;
        this.dia = dia;
        this.pulse = pulse;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSys() {
        return sys;
    }

    public void setSys(int sys) {
        this.sys = sys;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }
}

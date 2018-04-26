package com.maciejprogramuje.facebook.bloodpressurenew;

public class OneMeasurement {
    private String date;
    private String sys;
    private String dia;
    private String pulse;

    public OneMeasurement(String date, String sys, String dia, String pulse) {
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

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }
}

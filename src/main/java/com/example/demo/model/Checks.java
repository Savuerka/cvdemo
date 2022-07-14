package com.example.demo.model;

import java.sql.Date;
import java.sql.Time;

public class Checks {
    String identificator;
    Date date;
    Time time;
    Float summ;

    public String getIdentificator() {
        return identificator;
    }

    public void setIdentificator(String identificator) {
        this.identificator = identificator;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Float getSumm() {
        return summ;
    }

    public void setSumm(Float summ) {
        this.summ = summ;
    }

    @Override
    public String toString() {
        return "Checks{" +
                "identificator='" + identificator + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", summ=" + summ +
                '}';
    }
}

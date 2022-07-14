package com.example.demo.model;

public class Checklines {

    String identificator;
    long product_id;
    int str_number;
    int product_count;
    Float summ;

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public int getStr_number() {
        return str_number;
    }

    public void setStr_number(int str_number) {
        this.str_number = str_number;
    }

    public int getProduct_count() {
        return product_count;
    }

    public void setProduct_count(int product_count) {
        this.product_count = product_count;
    }

    public Float getSumm() {
        return summ;
    }

    public void setSumm(Float summ) {
        this.summ = summ;
    }

    public String getIdentificator() {
        return identificator;
    }

    public void setIdentificator(String identificator) {
        this.identificator = identificator;
    }

    @Override
    public String toString() {
        return "Checklines{" +
                "identificator='" + identificator + '\'' +
                ", product_id=" + product_id +
                ", str_number=" + str_number +
                ", product_count=" + product_count +
                ", summ=" + summ +
                '}';
    }
}

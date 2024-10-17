package com.backend.vo;

import java.util.*;

public class QueryRequest {
    private String carrier;
    private String grade;
    private String price;
    private String month;
    private String to;
    private String from;
    private String have_child;
    private String[] bundle_item;
    private String train_id;

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHave_child() {
        return have_child;
    }

    public void setHave_child(String have_child) {
        this.have_child = have_child;
    }

    public String[] getBundle_item() {
        return bundle_item;
    }

    public void setBundle_item(String[] bundle_item) {
        this.bundle_item = bundle_item;
    }

    public String getTrain_id() {
        return train_id;
    }

    public void setTrain_id(String train_id) {
        this.train_id = train_id;
    }

    @Override
    public String toString() {
        return "QueryRequest{" +
                "carrier='" + carrier + '\'' +
                ", grade='" + grade + '\'' +
                ", price='" + price + '\'' +
                ", month='" + month + '\'' +
                ", to='" + to + '\'' +
                ", from='" + from + '\'' +
                ", haveChild='" + have_child + '\'' +
                ", bundleItem=" + Arrays.toString(bundle_item) +
                ", trainId='" + train_id + '\'' +
                '}';
    }
}

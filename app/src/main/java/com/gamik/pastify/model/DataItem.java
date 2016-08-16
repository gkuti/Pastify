package com.gamik.pastify.model;

public class DataItem {
    private String placeHolder;
    private String value;
    private int usage;
    private int id;
    private String date;
    private int status;

    public DataItem() {
    }

    public DataItem(String placeHolder, String value, int id, int usage, String date, int status) {
        this.placeHolder = placeHolder;
        this.value = value;
        this.id = id;
        this.usage = usage;
        this.date = date;
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public String getValue() {
        return value;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public int getId() {
        return id;
    }

    public int getUsage() {
        return usage;
    }

    public String getDate() {
        return date;
    }
}

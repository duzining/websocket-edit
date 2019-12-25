package com.du.websocketedit.entity;

public class Text {

    private int id;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Text(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public Text(){

    }
}

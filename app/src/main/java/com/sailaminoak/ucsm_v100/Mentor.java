package com.sailaminoak.ucsm_v100;

public class Mentor {
    String name,mark,image;
    public Mentor(){

    }
    public Mentor(String name,String mark,String image){
        this.name=name;
        this.mark=mark;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

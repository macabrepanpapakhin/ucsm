package com.sailaminoak.ucsm_v100;

public class Club {
    String name,about,image;
    public Club(){

    }
    public Club(String name, String about, String image){
        this.about=about;
        this.image=image;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

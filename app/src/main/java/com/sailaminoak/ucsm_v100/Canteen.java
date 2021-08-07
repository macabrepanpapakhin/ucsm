package com.sailaminoak.ucsm_v100;

public class Canteen {

    String name,availableItems,image;
    public Canteen(){

    }
    public Canteen(String name,String availableItems,String image){
        this.name=name;
        this.availableItems=availableItems;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(String availableItems) {
        this.availableItems = availableItems;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

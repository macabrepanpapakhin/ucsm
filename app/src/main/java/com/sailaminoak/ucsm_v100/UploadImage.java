package com.sailaminoak.ucsm_v100;

public class UploadImage {
    private String mImageUri;
    private String name;
    public UploadImage(){

    }
    public UploadImage(String name, String mImageUri){
        if(name.trim().equals("")){
            name="No Name";

        }
        this.name=name;
        this.mImageUri=mImageUri;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}

package com.sailaminoak.ucsm_v100;

public class User {
 String mkpt,name,username,email,major,batch,password,achievement,bio,participatedClub,image,verifyOrNot;
    public User(){

    }

    public String getMkpt() {
        return mkpt;
    }

    public void setMkpt(String mkpt) {
        this.mkpt = mkpt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getParticipatedClub() {
        return participatedClub;
    }

    public void setParticipatedClub(String participatedClub) {
        this.participatedClub = participatedClub;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVerifyOrNot() {
        return verifyOrNot;
    }

    public void setVerifyOrNot(String verifyOrNot) {
        this.verifyOrNot = verifyOrNot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String mkpt, String username, String name, String email, String major, String batch, String password, String achievement, String bio, String participatedClub, String image, String verifyOrNot){
        this.mkpt=mkpt;
        this.name=name;
        this.username=username;
        this.email=email;
        this.major=major;
        this.batch=batch;
        this.achievement=achievement;
        this.bio=bio;
        this.password=password;
        this.participatedClub=participatedClub;
        this.image=image;
        this.verifyOrNot=verifyOrNot;
    }


}

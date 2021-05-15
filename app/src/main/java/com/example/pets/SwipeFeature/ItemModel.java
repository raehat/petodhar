package com.example.pets.SwipeFeature;

public class ItemModel {
   String breed;
   String amount;
   String city;
   String description;
   String mobile;
   String imageID;

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ItemModel(String breed, String amount, String city, String description, String mobile, String imageID) {
        this.breed = breed;
        this.amount = amount;
        this.city = city;
        this.description = description;
        this.mobile = mobile;
        this.imageID = imageID;
    }
}

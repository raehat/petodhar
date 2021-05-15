package com.example.pets;

public class ProductModel {

    public String book;
    public String amount;
    public String UserIDBook;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String location;

    public ProductModel() {}

    public ProductModel(String book, String amount, String UserIDBook, String location)
    {
        this.book=book;
        this.amount=amount;
        this.UserIDBook= UserIDBook;
        this.location= location;
    }

    public String getUserIDBook() {
        return UserIDBook;
    }

    public void setUserIDBook(String userIDBook) {
        UserIDBook = userIDBook;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

package com.example.myapplication.dto;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Books extends LitePalSupport {
    @Column
    int id;
    @Column
    String name;
    @Column
    String author;
    @Column
    double price;
    @Column
    int pages;

    public Books(String name, String author, double price, int pages) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.pages = pages;
    }

    public Books() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}

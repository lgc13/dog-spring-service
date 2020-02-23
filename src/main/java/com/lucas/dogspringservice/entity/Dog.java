package com.lucas.dogspringservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "dog")
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    public Dog() {}

    public Dog(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }
}

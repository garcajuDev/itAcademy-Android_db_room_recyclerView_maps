package com.itacademy.juangarcia.database_animal.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "animal_table")
public class Animal implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "photo")
    private String photo;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "age")
    private int age;

    @ColumnInfo(name = "chip")
    private boolean chip;

    public Animal(String name, String photo, String type, String date, int age, boolean chip) {
        this.name = name;
        this.photo = photo;
        this.type = type;
        this.date = date;
        this.age = age;
        this.chip = chip;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public int getAge() {
        return age;
    }

    public boolean isChip() {
        return chip;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setChip(boolean chip) {
        this.chip = chip;
    }
}

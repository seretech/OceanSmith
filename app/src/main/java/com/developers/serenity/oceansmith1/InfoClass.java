package com.developers.serenity.oceansmith1;

public class InfoClass {

    public InfoClass(){}

    private String id, name, img, habitat;

    public InfoClass(String id, String name, String img, String habitat) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.habitat = habitat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }
}

package imd.ufrn.models;

public class Cat {
    private String id;
    private String name;
    private String color;
    private String owner;

    public Cat(String id, String name, String color, String owner) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.owner = owner;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}

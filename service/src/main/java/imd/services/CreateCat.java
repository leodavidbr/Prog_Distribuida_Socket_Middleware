package imd.services;

import java.util.List;

import imd.ufrn.models.Cat;

public class CreateCat {
    private static List<Cat> cats;

    public CreateCat(List<Cat> cats) {
        CreateCat.cats = cats;
    }

    public static String registerCat(String parameters) {
        String[] parts = parameters.split(";");
        Cat newCat = new Cat(parts[1], parts[2], parts[3], parts[4]);
        cats.add(newCat);
        return "Cat created: " + newCat.getName();
    }

    public static String getMessage() {
        return "CreateCat;String id % String name % String color % String owner";
    }
}

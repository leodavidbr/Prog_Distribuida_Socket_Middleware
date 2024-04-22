package imd.services;

import java.util.ArrayList;
import java.util.List;

import imd.ufrn.models.Cat;

public class CreateCat {
    private static List<Cat> cats = new ArrayList<>();

    public CreateCat(List<Cat> cats) {
        CreateCat.cats = cats;
    }

    public static void setCatsList(List<Cat> catsList) {
        CreateCat.cats = catsList;
    }

    public static String registerCat(List<String> parameters) {
        Cat newCat = new Cat(parameters.get(1), parameters.get(2), parameters.get(3), parameters.get(4));
        cats.add(newCat);
        return "Cat created: " + newCat.getName();
    }

    public static String getMessage() {
        return "CreateCat;String id % String name % String color % String owner";
    }
}

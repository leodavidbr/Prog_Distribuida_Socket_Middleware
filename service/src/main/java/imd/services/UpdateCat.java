package imd.services;

import java.util.ArrayList;
import java.util.List;

import imd.ufrn.models.Cat;

public class UpdateCat {
    private static List<Cat> cats = new ArrayList<>();

    public UpdateCat(List<Cat> cats) {
        UpdateCat.cats = cats;
    }

    public static void setCatsList(List<Cat> catsList) {
        UpdateCat.cats = catsList;
    }

    public static String updateCat(List<String> parameters) {
        for (Cat cat : cats) {
            if (cat.getId().equals(parameters.get(1))) {
                cat.setName(parameters.get(2));
                cat.setColor(parameters.get(3));
                cat.setOwner(parameters.get(4));
            }
        }
        return "Cat updated";
    }

    public String getMessage() {
        return "UpdateCat;String id % String name % String color % String owner";
    }
}

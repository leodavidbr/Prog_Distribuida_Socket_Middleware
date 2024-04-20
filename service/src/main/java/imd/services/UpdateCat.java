package imd.services;

import java.util.List;

import imd.ufrn.models.Cat;

public class UpdateCat {
    private static List<Cat> cats;

    public UpdateCat(List<Cat> cats) {
        UpdateCat.cats = cats;
    }

    public static String updateCat(String parameters) {
        String[] parts = parameters.split(";");
        for (Cat cat : cats) {
            if (cat.getId().equals(parts[1])) {
                cat.setName(parts[2]);
                cat.setColor(parts[3]);
                cat.setOwner(parts[4]);
            }
        }
        return "Cat updated";
    }

    public String getMessage() {
        return "UpdateCat;String id % String name % String color % String owner";
    }
}

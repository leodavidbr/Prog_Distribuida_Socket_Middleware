package imd.services;

import java.util.List;

import imd.ufrn.models.Cat;

public class ReadCat {
    private static List<Cat> cats;

    public ReadCat(List<Cat> cats) {
        ReadCat.cats = cats;
    }

    public static String readCat(String id) {
        for (int i = 0; i < cats.size(); i++) {
            if (cats.get(i).getId().equals(id)) {
                return "Cat Details - Name: " + cats.get(i).getName() + ", Color: " + cats.get(i).getColor()
                        + ", Owner: " + cats.get(i).getOwner();
            }
        }
        return "Cat not found";
    }

    public String getMessage() {
        return "ReadCat;String id";
    }
}

package imd.services;

import java.util.List;

import imd.ufrn.models.Cat;

public class DeleteCat {
    private static List<Cat> cats;

    public DeleteCat(List<Cat> cats) {
        DeleteCat.cats = cats;
    }

    public static String deleteCat(String id) {
        for (int i = 0; i < cats.size(); i++) {
            if (cats.get(i).getId().equals(id)) {
                cats.remove(i);
                return "Cat deleted";
            }
        }
        return "Cat not found";
    }

    public String getMessage() {
        return "DeleteCat;String id";
    }
}
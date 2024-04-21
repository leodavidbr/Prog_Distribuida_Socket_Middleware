package imd.services;

import java.util.ArrayList;
import java.util.List;

import imd.ufrn.models.Cat;

public class DeleteCat {
    private static List<Cat> cats = new ArrayList<>();

    public DeleteCat(List<Cat> cats) {
        DeleteCat.cats = cats;
    }

    public static void setCatsList(List<Cat> catsList) {
        DeleteCat.cats = catsList;
    }

    public static String deleteCat(List<String> parameters) {
        String id = parameters.get(0);

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
package com.Group6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CourseDictionaryCreator {
    public static Map<String, String> courseDictionary = new HashMap<>();
    public static boolean autosave;

    public static void main(String args[]) throws FileNotFoundException {
        String file = "Course2.txt";
        Scanner in = new Scanner(new File(file));

        while (in.hasNext()) {
            String key = in.next();
            System.out.println(key);
            String value = in.next();
            System.out.println(value);

            courseDictionary.put(key, value);
        }

        System.out.println(courseDictionary.get("g"));
        System.out.println(courseDictionary.get("mass_of_ball"));
        System.out.println(courseDictionary.get("mu"));
        System.out.println(courseDictionary.get("vmax"));
        System.out.println(courseDictionary.get("tol"));
        System.out.println(courseDictionary.get("startX"));
        System.out.println(courseDictionary.get("startY"));
        System.out.println(courseDictionary.get("goalX"));
        System.out.println(courseDictionary.get("goalY"));
        System.out.println(courseDictionary.get("heightX"));
        System.out.println(courseDictionary.get("heightX2"));
        System.out.println(courseDictionary.get("heightY"));
        System.out.println(courseDictionary.get("ballposX"));
        System.out.println(courseDictionary.get("ballposY"));
        System.out.println(courseDictionary.get("stroke"));

        if(courseDictionary.get("stroke").equals("0") ){
            autosave = false;
        }
        else{
            autosave = true;
        }
    }
}

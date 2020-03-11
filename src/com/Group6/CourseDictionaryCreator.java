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
        String file = "src/com/Group6/Course2.txt";
        Scanner in = new Scanner(new File(file));

        while (in.hasNext()) {
            String key = in.next();
            String value = in.next();

            System.out.println(key + ": " + value);
            courseDictionary.put(key, value);
        }

        if(courseDictionary.get("stroke").equals("0") ){
            autosave = false;
        }
        else{
            autosave = true;
        }
    }
}

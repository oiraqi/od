package org.galactis.od;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Util {
    
    private Util() {

    }

    public static String getDescription(String model) {
        String className = model.substring(model.indexOf(".") + 1);
        System.out.println(className);
        String[] tokens = className.split("[.]");
        StringBuilder classNameBuilder = new StringBuilder();
        for (String token : tokens) {
            System.out.println(token);
            token = String.valueOf(token.charAt(0)).toUpperCase() + token.substring(1);
            classNameBuilder.append(token + " ");
        }
        return classNameBuilder.toString().trim();
    }

    public static String getClassName(String model) {
        String className = model.substring(model.indexOf(".") + 1);
        System.out.println(className);
        String[] tokens = className.split("[.]");
        StringBuilder classNameBuilder = new StringBuilder();
        for (String token : tokens) {
            System.out.println(token);
            token = String.valueOf(token.charAt(0)).toUpperCase() + token.substring(1);
            classNameBuilder.append(token);
        }
        return classNameBuilder.toString();
    }

    public static void printToFile(String content, String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path)))) {
            pw.print(content);
            pw.flush();
        }
    }

    public static void appendToFile(String content, String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
            pw.print(content);
            pw.flush();
        }
    }

    public static String getViewPath(String model) {
        return "./views/" + model.substring(model.indexOf(".") + 1) + "_view.xml";
    }

    public static String getModelShortUnderscoredName(String model) {
        return model.substring(model.indexOf(".") + 1).replace(".", "_");
    }

    public static String getGroupShortName(String group) {
        return group.substring(group.indexOf("group_") + 6);
    }

    public static boolean alreadyExists(String content, String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = null;
            while((line = br.readLine()) != null) {
                if (line.indexOf(content) >= 0) {
                    return true;
                }
            }
            return false;
        }        
    }
}

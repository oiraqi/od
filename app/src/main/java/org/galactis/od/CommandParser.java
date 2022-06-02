package org.galactis.od;

import java.util.HashMap;
import java.util.Map;

public class CommandParser {

    private CommandParser() {

    }
    
    public static Map<String, String> parse(String[] args) throws ParseException {
        if (args == null || args.length < 2) {
            throw new ParseException("Incorrect command");
        }

        Map<String, String> argMap = new HashMap<>();
        String argsLine = String.join(" ", args);
        String[] argz = argsLine.split("-");
        for (int i=0; i < argz.length; i++) {
            argz[i] = argz[i].trim();
        }
        
        for (int i = 1; i < argz.length; i++) {
            if (argz[i].equals("v") || argz[i].equals("i")) {
                argMap.put(argz[i], "");
                continue;
            }

            if (argz[i].indexOf(' ') == -1) {
                throw new ParseException("Option " + argz[i] + " with no value");
            }

            String option = argz[i].substring(0, argz[i].indexOf(' ')).trim();
            String value = argz[i].substring(argz[i].indexOf(' ')).trim();
            if (option.equals("g")) {
                verifyGroups(value);
            }
            argMap.put(option, value);
        }

        return argMap;
    }

    private static void verifyGroups(String groups) throws ParseException {
        String[] groupz = groups.split(" ");
        for (String groupRight : groupz) {
            if (groupRight.split(":").length != 2) {
                System.out.println(groupRight + " doesn't contain ':'");
                throw new ParseException("Malformed groups");
            }
            String accessRight = groupRight.split(":")[1];
            if (accessRight == null || accessRight.length() != 4) {
                throw new ParseException("Malformed access rights");
            }
            for (int i = 0; i < 4; i++) {
                if (accessRight.charAt(i) != '0' && accessRight.charAt(i) != '1') {
                    System.out.println(accessRight + " should be in [1|0][1|0][1|0][1|0] format");
                    throw new ParseException("Malformed access rights");
                }
            }
        }
    }
}

package org.galactis.od;

import java.io.IOException;

public class MenuGenerator {

    private MenuGenerator() {

    }

    public static void create(String model, String menu) throws MenuException, IOException {
        String id = null, name = null, parent = null, action = null;
        for (String token : menu.split(" ")) {
            if (token.split(":")[0].equals("i")) {
                id = token.split(":")[1];
            } else if (token.split(":")[0].equals("n")) {
                name = token.split(":")[1];
            } else if (token.split(":")[0].equals("p")) {
                parent = token.split(":")[1];
            } else if (token.split(":")[0].equals("a")) {
                action = token.split(":")[1];
            }
        }
        
        if (id == null && model == null) {
            throw new MenuException("Unable to determinemenu item id");
        }

        if (name == null || name.isEmpty()) {
            throw new MenuException("Menu item name is lacking");
        }

        id = id != null ? id + "_menu" : model.replace(".", "_") + "_menu";
        if (Util.alreadyExists("<menuitem id=\"" + id + "\"", "./views/menu.xml")) {
            return;
        }
        if (action == null && model != null) {
            action = Util.getModelShortUnderscoredName(model);
        }
        StringBuilder menuItemBuilder = new StringBuilder("<menuitem id=\"%s\" name=\"%s\"");
        if (action != null) {
            menuItemBuilder.append(" action=\"action_" + action + "\"");
        }
        if (parent != null) {
            menuItemBuilder.append(" parent=\"" + parent + "\"");
        }
        menuItemBuilder.append(" sequence=\"50\"/>\n");
        String menuItem = String.format(menuItemBuilder.toString(), id, name);        

        Util.appendToFile(menuItem, "./views/menu.xml");
    }

}

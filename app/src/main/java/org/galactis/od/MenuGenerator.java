package org.galactis.od;

import java.io.File;
import java.io.IOException;

public class MenuGenerator {

    private MenuGenerator() {

    }

    public static void create(String model, String menu) throws MenuException, IOException {
        String id = null;
        String name = null;
        String parent = null;
        String action = null;
        for (String token : menu.split(" ")) {
            if (token.split(":")[0].equals("i")) {
                id = token.split(":")[1];
                if (id.contains(".")) {
                    throw new MenuException("Menuitem Id should not contain '.'");
                }
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

        String menuFile = "./views/" + Util.getModule() + "_menu.xml";

        id = id != null ? id + "_menu" : model.replace(".", "_") + "_menu";
        if (Util.exists(menuFile) && Util.in("id=\"" + id + "\"", menuFile)) {
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
            String parentId = null;
            String path = null;
            if (parent.contains(".")) {
                parentId = parent.substring(parent.indexOf(".") + 1);
                path = "../" + parent.substring(0, parent.indexOf(".")) + "./views/" + parent.substring(0, parent.indexOf(".")) + "_menu.xml";
            } else {
                parentId = parent;
                path = menuFile;
            }
            if (!Util.exists(path) || !Util.in("id=\"" + parentId + "_menu\"", path)) {
                throw new MenuException("The specified parent menu doesn't exist");
            }
            menuItemBuilder.append(" parent=\"" + parent + "_menu\"");
        }
        menuItemBuilder.append(" sequence=\"50\"/>");
        String menuItem = String.format(menuItemBuilder.toString(), id, name);
        if (!Util.exists("./views")) {
            new File("./views").mkdir();
        }
        if (parent == null) {
            Util.printToFile("<odoo>\n\t<data>\n\t\t" + menuItem + "\n\t</data>\n</odoo>", menuFile);
        } else {
            Util.insertIntoFileAfter(menuItem, "id=\"" + parent + "_menu\"", menuFile);
        }
    }

}

package org.galactis.od;

import java.io.File;
import java.io.IOException;

public class ViewGenerator {

    private ViewGenerator() {

    }

    public static void create(String model, String name) throws IOException {
        File views = new File("./views");
        if (!views.exists()) {
            views.mkdir();
        }
        File view = new File(Util.getViewPath(model)); // Path Traversal!!!
        if (view.exists()) {
            return;
        }

        if (name.isEmpty()) {
            name = Util.getDescription(model);
        }

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<odoo>\n\t<data>\n\t\t<record id=\"view_%s_form\" model=\"ir.ui.view\">\n\t\t\t<field name=\"name\">%s.form</field>\n\t\t\t<field name=\"model\">%s</field>\n\t\t\t<field name=\"arch\" type=\"xml\">\n\t\t\t\t<form string=\"%s\">\n\t\t\t\t\t<sheet>\n\t\t\t\t\t\t<h1><field name=\"name\" default_focus=\"1\" placeholder=\"Name...\"/></h1>\n\t\t\t\t\t\t<group name=\"info\" string=\"General Information\" col=\"4\">\n\t\t\t\t\t\t\n\t\t\t\t\t\t</group>\n\t\t\t\t\t\t<group name=\"log\" string=\"Log\" col=\"4\">\n\t\t\t\t\t\t\t<field name=\"create_uid\"/>\n\t\t\t\t\t\t\t<field name=\"create_date\"/>\n\t\t\t\t\t\t\t<field name=\"write_uid\"/>\n\t\t\t\t\t\t\t<field name=\"write_date\"/>\n\t\t\t\t\t\t</group>\n\t\t\t\t\t</sheet>\n\t\t\t\t</form>\n\t\t\t</field>\n\t\t</record>\n\t\t<record id=\"view_%s_tree\" model=\"ir.ui.view\">\n\t\t\t<field name=\"name\">%s.tree</field>\n\t\t\t<field name=\"model\">%s</field>\n\t\t\t<field eval=\"8\" name=\"priority\"/>\n\t\t\t<field name=\"arch\" type=\"xml\">\n\t\t\t\t<tree string=\"%ss\">\n\t\t\t\t\t<field name=\"name\"/>\n\t\t\t\t</tree>\n\t\t\t</field>\n\t\t</record>\n\t\t<record id=\"action_%s\" model=\"ir.actions.act_window\">\n\t\t\t<field name=\"name\">%ss</field>\n\t\t\t<field name=\"type\">ir.actions.act_window</field>\n\t\t\t<field name=\"res_model\">%s</field>\n\t\t\t<field name=\"view_mode\">tree,form</field>\n\t\t\t<field name=\"domain\">[]</field>\n\t\t\t<field name=\"context\">{}</field>\n\t\t\t<field name=\"help\" type=\"html\">\n\t\t\t\t<p class=\"o_view_nocontent_smiling_face\">\n\t\t\t\t\n\t\t\t\t</p>\n\t\t\t\t<p>\n\t\t\t\t\n\t\t\t\t</p>\n\t\t\t</field>\n\t\t</record>\n\t\t<record id=\"action_%s_form_view\" model=\"ir.actions.act_window.view\">\n\t\t\t<field name=\"sequence\" eval=\"2\"/>\n\t\t\t<field name=\"view_mode\">form</field>\n\t\t\t<field name=\"view_id\" ref=\"view_%s_form\"/>\n\t\t\t<field name=\"act_window_id\" ref=\"action_%s\"/>\n\t\t</record>\n\t\t<record id=\"action_%s_tree_view\" model=\"ir.actions.act_window.view\">\n\t\t\t<field name=\"sequence\" eval=\"1\"/>\n\t\t\t<field name=\"view_mode\">tree</field>\n\t\t\t<field name=\"view_id\" ref=\"view_%s_tree\"/>\n\t\t\t<field name=\"act_window_id\" ref=\"action_%s\"/>\n\t\t</record>\n\t</data>\n</odoo>";
        xml = String.format(xml, Util.getModelShortUnderscoredName(model), model, model, name,
                Util.getModelShortUnderscoredName(model), model, model, name, Util.getModelShortUnderscoredName(model), name, model,
                Util.getModelShortUnderscoredName(model), Util.getModelShortUnderscoredName(model),
                Util.getModelShortUnderscoredName(model), Util.getModelShortUnderscoredName(model),
                Util.getModelShortUnderscoredName(model), Util.getModelShortUnderscoredName(model));

        Util.printToFile(xml, view.getPath());
    }

}
